package at.ac.tuwien.cashtechthon.cep;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import at.ac.tuwien.cashtechthon.cep.event.AccountBalanceEvent;
import at.ac.tuwien.cashtechthon.cep.event.ClassificationEvent;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

public class EventProcessor {
	private final EPServiceProvider epService;
	private final EPAdministrator epAdmin;
	private final AtomicLong alertCounter = new AtomicLong();

	private EventProcessor() {
		Configuration configuration = new Configuration();
		configuration.addEventType("Classification", ClassificationEvent.class.getName());
		configuration.addEventType("Balance", AccountBalanceEvent.class.getName());
		configuration.addPlugInSingleRowFunction("millis", "at.ac.tuwien.cashtechthon.cep.TimeFunction", "calculateMilliseconds");
		configuration.addPlugInSingleRowFunction("isInCycle", "at.ac.tuwien.cashtechthon.cep.TimeFunction", "isInCycle");
		epService = EPServiceProviderManager.getProvider("TransactionEngine", configuration);
		epAdmin = epService.getEPAdministrator();
	}

	private static class InstanceHolder {
		private static EventProcessor INSTANCE = new EventProcessor();
	}

	public static EventProcessor getInstance() {
		return InstanceHolder.INSTANCE;
	}

	public void addEvent(ClassificationEvent event) {
		epService.getEPRuntime().sendEvent(event);
	}

	public void addEvent(AccountBalanceEvent event) {
		epService.getEPRuntime().sendEvent(event);
	}

	public Long createRelativeThreshold(RelativeThresholdParameter parameter) {
		StringBuilder thresholdQuery = new StringBuilder();
		thresholdQuery.append("select * from Classification(customerId=")
		.append(parameter.getCustomerId())
		.append(").win:keepall() c")
		.append(" where")
		.append(" (select sum(c2.amountInEur) from Classification(customerId=")
		.append(parameter.getCustomerId());
		if(parameter.getClassification() != null) {
			thresholdQuery.append(", classification='")
			.append(parameter.getClassification()).append("'");
		}
		thresholdQuery.append(").win:keepall() c2 where millis(c2.classificationDate) > ")
		.append(TimeFunction.calculateMilliseconds(LocalDateTime.now()));
		if(parameter.getDirection().equals("unidirectional")) {
			thresholdQuery.append(" and c.amountInEur ");
			if(parameter.getType().equals("positive")) {
				thresholdQuery.append(" > 0");
			} else if(parameter.getType().equals("negative")) {
				thresholdQuery.append(" < 0");
			} else {
				throw new IllegalArgumentException("Don't know what to do with type " + parameter.getType() + " expected positive or negative");
			}
		}
		if(parameter.getWindowSize() != null) {
			Long windowSize = parameter.getWindowSize();
			Long windowStart = TimeFunction.calculateMilliseconds(parameter.getWindowStart() != null ? parameter.getWindowStart() : LocalDateTime.now());
			thresholdQuery.append(" and isInCycle(classificationDate, ")
			.append(windowStart).append(", ").append(windowSize).append(")");
		}
		thresholdQuery.append(")");
		if(parameter.getType().equals("positive")) {
			thresholdQuery.append(" >= ").append(parameter.getThresholdInEur());
		} else {
			thresholdQuery.append(" <= ").append(parameter.getThresholdInEur().negate());
		}
		long alertId = alertCounter.incrementAndGet();
		epAdmin.createEPL(thresholdQuery.toString()).addListener(new AlertListener(alertId, parameter.getCallback()));
		return alertId;
	}

	public Long createAbsoluteThreshold(AbsoluteThresholdParameter parameter) {
		Long customerId = parameter.getAccountBalance().getCustomerId();
		StringBuilder thresholdQuery = new StringBuilder();
		thresholdQuery.append("select * from Classification(customerId=")
		.append(customerId)
		.append(").win:keepall() c, Balance(customerId=")
		.append(customerId)
		.append(").std:lastevent() b")
		.append(" where b.balanceInEur +")
		.append(" (select sum(c2.amountInEur) from Classification(customerId=")
		.append(customerId)
		.append(").win:keepall() c2 where millis(c2.classificationDate) > millis(b.determinedAt))");
		if(parameter.getType().equals("positive")) {
			thresholdQuery.append(" >= ").append(parameter.getThresholdInEur());
		} else {
			thresholdQuery.append(" <= ").append(parameter.getThresholdInEur().negate());
		}
		long alertId = alertCounter.incrementAndGet();
		epAdmin.createEPL(thresholdQuery.toString()).addListener(new AlertListener(alertId, parameter.getCallback()));;
		addEvent(parameter.getAccountBalance());
		return alertId;
	}

	private static class AlertListener implements StatementAwareUpdateListener {
		private final Long alertId;
		private final AlertCallback callback;

		public AlertListener(Long alertId, AlertCallback callback) {
			this.alertId = alertId;
			this.callback = callback;
		}

		@Override
		public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPServiceProvider epServiceProvider) {
			System.out.println("--------------------------AlertListener update for alert " + alertId + "--------------------------");
			callback.onAlert(alertId);
		}
	}
}

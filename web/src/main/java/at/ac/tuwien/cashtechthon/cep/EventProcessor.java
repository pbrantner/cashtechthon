package at.ac.tuwien.cashtechthon.cep;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import at.ac.tuwien.cashtechthon.cep.event.AccountBalanceEvent;
import at.ac.tuwien.cashtechthon.cep.event.ClassificationEvent;
import at.ac.tuwien.cashtechthon.dtos.ClassificationComparison;

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
		configuration.addPlugInSingleRowFunction("age", "at.ac.tuwien.cashtechthon.cep.AgeFunction", "calculateAge");
		configuration.addPlugInSingleRowFunction("millis", "at.ac.tuwien.cashtechthon.cep.TimeFunction", "calculateMilliseconds");
		configuration.addPlugInSingleRowFunction("startOfMonth", "at.ac.tuwien.cashtechthon.cep.TimeFunction", "getStartOfMonth");
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

	public Long createRelativeThreshold(Long customerId, BigDecimal thresholdInEur, String type, String direction, AlertCallback callback) {
		return createRelativeThreshold(customerId, thresholdInEur, null, type, direction, callback);
	}
	
	public Long createRelativeThreshold(Long customerId, BigDecimal thresholdInEur, String classification, String type, String direction, AlertCallback callback) {
		StringBuilder thresholdQuery = new StringBuilder();
		thresholdQuery.append("select * from Classification(customerId=")
		.append(customerId)
		.append(").win:keepall() c")
		.append(" where")
		.append(" (select sum(c2.amountInEur) from Classification(customerId=")
		.append(customerId);
		if(classification != null) {
			thresholdQuery.append(", classification='")
			.append(classification).append("'");
		}
		thresholdQuery.append(").win:keepall() c2 where millis(c2.classificationDate) > ")
		.append(TimeFunction.calculateMilliseconds(LocalDateTime.now()));
//		if(classification != null) {
//			thresholdQuery.append(" and c.classification = '")
//			.append(classification).append("'");
//		}
		if(direction.equals("unidirectional")) {
			thresholdQuery.append(" and c.amountInEur ");
			if(type.equals("positive")) {
				thresholdQuery.append(" > 0");
			} else if(type.equals("negative")) {
				thresholdQuery.append(" < 0");
			} else {
				throw new IllegalArgumentException("Don't know what to do with type " + type + " expected positive or negative");
			}
		}
		thresholdQuery.append(")");
		if(type.equals("positive")) {
			thresholdQuery.append(" >= ").append(thresholdInEur);
		} else {
			thresholdQuery.append(" <= ").append(thresholdInEur.negate());
		}
		long alertId = alertCounter.incrementAndGet();
		epAdmin.createEPL(thresholdQuery.toString()).addListener(new AlertListener(alertId, callback));
		return alertId;
	}

	public Long createAbsoluteThreshold(BigDecimal thresholdInEur, String type, AccountBalanceEvent balance, AlertCallback callback) {
		StringBuilder thresholdQuery = new StringBuilder();
		thresholdQuery.append("select * from Classification(customerId=")
		.append(balance.getCustomerId())
		.append(").win:keepall() c, Balance(customerId=")
		.append(balance.getCustomerId())
		.append(").std:lastevent() b")
		.append(" where b.balanceInEur +")
		.append(" (select sum(c2.amountInEur) from Classification(customerId=")
		.append(balance.getCustomerId())
		.append(").win:keepall() c2 where millis(c2.classificationDate) > millis(b.determinedAt))");
		if(type.equals("positive")) {
			thresholdQuery.append(" >= ").append(thresholdInEur);
		} else {
			thresholdQuery.append(" <= ").append(thresholdInEur.negate());
		}
		long alertId = alertCounter.incrementAndGet();
		epAdmin.createEPL(thresholdQuery.toString()).addListener(new AlertListener(alertId, callback));;
		addEvent(balance);
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
			System.out.println("--------------------------AlertListener update for alert " + alertId);
			callback.onAlert(alertId);
		}
	}

	public ClassificationComparison filterClassifications(ClassificationFilter filter) {
		int pastMonths = filter.getPastMonths();
		LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withHour(0).withMinute(0).withSecond(0).withNano(0).minusMonths(pastMonths);
		ResultListener resultListener = new ResultListener(pastMonths);
		CustomerResultListener customerResultListener = new CustomerResultListener();
		String customerQuery = "select millis(startOfMonth(c.classificationDate)) as monthInMillis, avg(amountInEur) as avgAmount"
				+ " from Classification c where c.customerId = " + filter.getCustomerId()
				+ " group by millis(startOfMonth(c.classificationDate))";
		epAdmin.createEPL(customerQuery).addListener(customerResultListener);
		do {
			LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1L);
			StringBuilder query = new StringBuilder();
			query.append("select " + TimeFunction.calculateMilliseconds(startOfMonth) + " as monthInMillis, avg(amountInEur) as avgAmount from Classification as c where millis(c.classificationDate)"
					+ " between ").append(TimeFunction.calculateMilliseconds(startOfMonth)).append(" and ").append(TimeFunction.calculateMilliseconds(startOfNextMonth));
			if(filter.getAgeFrom() != null) {
				query.append(" and age(c.birthday) >= ").append(filter.getAgeFrom());
			}
			if(filter.getAgeTill() != null) {
				query.append(" and age(c.birthday) <= ").append(filter.getAgeTill());
			}
			if(filter.getSex() != null) {
				query.append(" and c.sex = ")
				.append(filter.getSex());
			}
			if(filter.getLocations() != null && !filter.getLocations().isEmpty()) {
				query.append(" and c.location in (")
				.append(String.join(",", filter.getLocations()))
				.append(")");
			}
			if(filter.getClassifications() != null && !filter.getClassifications().isEmpty()) {
				query.append(" and c.classification in (")
				.append(String.join(",", filter.getClassifications()))
				.append(")");
			}
			epAdmin.createEPL(query.toString()).addListener(resultListener);
			startOfMonth = startOfNextMonth;
		} while(startOfMonth.isBefore(LocalDateTime.now()));
		epService.getEPRuntime().sendEvent(
				new ClassificationEvent(23L, LocalDateTime.of(1944, 5, 22, 0, 0), "Poysdorf", false, "Transportation", LocalDateTime.of(2015, 6, 2, 17, 48, 33), Currency.getInstance("EUR"), new BigDecimal("-26.977"), new BigDecimal("-26.977")));
		Map<Long, BigDecimal> customerResults = customerResultListener.awaitResults();
		Map<Long, BigDecimal> results = resultListener.awaitResults();

		Object[] data = new Object[results.size()];
		int dataIndex = 0;
		for (Map.Entry<Long, BigDecimal> result : results.entrySet()) {
			Object[] nestedData = new Object[3];
			nestedData[0] = result.getKey();
			nestedData[1] = customerResults.get(result.getKey());
			nestedData[2] = result.getValue();
			data[dataIndex] = nestedData;
			dataIndex++;
		}
		ClassificationComparison comparison = new ClassificationComparison();
		comparison.setColumns(Arrays.asList("Month", "Customer", "Group"));
		comparison.setData(data);
		comparison.setStart((((Object[])data[0])[0]).toString());
		comparison.setEnd((((Object[])data[dataIndex - 1])[0]).toString());
		return comparison;
	}
	/*
    income-from (optional)
        Values: 15000, 20100, 70200, ...
        Description: Limits the comparision to customers who have a min. monthly income of income-from.
    income-till (optional)
        Values: 15000, 20100, 70200, ...
        Description: Limits the comparision to customers who have a max. monthly income of income-till.

	 */

	private static class ResultListener implements StatementAwareUpdateListener {
		private final int expectedResults;
		private int resultCount = 0;
		private final Semaphore sem;
		private final ConcurrentMap<Long, BigDecimal> results;

		public ResultListener(int expectedResults) {
			this.expectedResults = expectedResults;
			this.sem = new Semaphore(0);
			this.results = new ConcurrentHashMap<Long, BigDecimal>(expectedResults);
		}

		@Override
		public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPServiceProvider epServiceProvider) {
			System.out.println("triggered event");
			resultCount++;
			for (EventBean eventBean : newEvents) {
				Long monthInMillis = (Long)eventBean.get("monthInMillis");
				BigDecimal averageAmount = (BigDecimal)eventBean.get("avgAmount");
				System.out.println("month: " + monthInMillis + ", avgAmount: " + averageAmount);
				results.put(monthInMillis, averageAmount);
			}

			if(resultCount == expectedResults) {
				statement.destroy();
				sem.release();
			}
		}

		public Map<Long, BigDecimal> awaitResults() {
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return results;
		}
	}

	private static class CustomerResultListener implements StatementAwareUpdateListener {
		private final Map<Long, BigDecimal> results = new HashMap<>();
		private final Semaphore sem = new Semaphore(0);

		@Override
		public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPServiceProvider epServiceProvider) {
			for (EventBean eventBean : newEvents) {
				Long monthInMillis = (Long)eventBean.get("monthInMillis");
				BigDecimal avgAmount = (BigDecimal)eventBean.get("avgAmount");
				System.out.println("month: " + monthInMillis + ", avgAmount: " + avgAmount);
				results.put(monthInMillis, avgAmount);
			}
			statement.destroy();
			sem.release();
		}

		public Map<Long, BigDecimal> awaitResults() {
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return results;
		}
	}
}

package at.ac.tuwien.cashtechthon.cep;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

import at.ac.tuwien.cashtechthon.dtos.Classification;

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

	private EventProcessor() {
		Configuration configuration = new Configuration();
		configuration.addEventType("Classification", CustomerClassification.class.getName());
		configuration.addPlugInSingleRowFunction("age", "at.ac.tuwien.cashtechthon.cep.EventProcessor.AgeFunction", "calculateAge");
		epService = EPServiceProviderManager.getProvider("TransactionEngine", configuration);
		epAdmin = epService.getEPAdministrator();
	}

	private static class InstanceHolder {
		private static EventProcessor INSTANCE = new EventProcessor();
	}

	public static EventProcessor getInstance() {
		return InstanceHolder.INSTANCE;
	}

	public void addEvent(Classification event) {
		epService.getEPRuntime().sendEvent(event);
	}

	public List<Classification> filterClassifications(ClassificationFilter filter) {
		int pastMonths = filter.getPastMonths();
		LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withHour(0).withSecond(0).withNano(0).minusMonths(pastMonths);
		ResultListener resultListener = new ResultListener(pastMonths);
		do {
			LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1L);
			ChronoUnit.YEARS.between(LocalDateTime.now(), LocalDateTime.now());
			StringBuilder query = new StringBuilder();
			query.append("select " + startOfMonth.toString() + " as month, avg(amount) as avgAmount from Classification as c where c.classificationDate.between(")
			.append(startOfMonth).append(", ").append(startOfNextMonth).append(", true, false)");
			if(filter.getAgeFrom() != null) {
				query.append(" and age(c.birthday) >= ").append(filter.getAgeFrom());
			}
			if(filter.getAgeTill() != null) {
				query.append(" and age(c.birthday) <= ").append(filter.getAgeTill());
			}
			if(filter.getSex() != null) {
				query.append(" and c.sex");
				if(filter.getSex()) {
					query.append(" = 1");
				} else {
					query.append(" = 0");
				}
			}
			if(filter.getLocations() != null && !filter.getLocations().isEmpty()) {
				query.append(" and c.location IN (")
				.append(String.join(",", filter.getLocations()))
				.append(")");
			}
			if(filter.getClassifications() != null && !filter.getClassifications().isEmpty()) {
				query.append(" and c.classification IN (")
				.append(String.join(",", filter.getClassifications()))
				.append(")");
			}
			EPStatement statement = epAdmin.createEPL(query.toString());
			statement.addListener(resultListener);
			startOfMonth = startOfNextMonth;
		} while(startOfMonth.isBefore(LocalDateTime.now()));
		Map<String, Double> results = resultListener.awaitResults();
		return null;
	}

	/*
	 * 
	 * {"start":"2015-11"
        ,"end":"2016-02"
        ,"columns":['Month', 'Customer', 'Group']
        ,"data":
            [['2015-11', 630.77 , 722.10 ]
            ,['2015-12', 1050.23, 1802.12]
            ,['2016-01', 303.55 , 280.78 ]
            ,['2016-02', 405.20 , 480.10 ]
            ]
        }
	 * 
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
		private final ConcurrentMap<String, Double> results;
		
		public ResultListener(int expectedResults) {
			this.expectedResults = expectedResults;
			this.sem = new Semaphore(-1);
			this.results = new ConcurrentHashMap<String, Double>(expectedResults);
		}
		
		@Override
		public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPServiceProvider epServiceProvider) {
			resultCount++;
			for (EventBean eventBean : newEvents) {
				String month = (String)eventBean.get("month");
				Double averageAmount = (Double)eventBean.get("avgAmount");
				System.out.println("month: " + month + ", avgAmount: " + averageAmount);
				results.put(month, averageAmount);
			}
			
			if(resultCount == expectedResults) {
				statement.destroy();
				sem.release();
			}
		}
		
		public Map<String, Double> awaitResults() {
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return results;
		}
	}
	
	public static class AgeFunction {
		public static int calculateAge(LocalDateTime birthday) {
			return (int)ChronoUnit.YEARS.between(birthday, LocalDateTime.now());
		}
	}
}

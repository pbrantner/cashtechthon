package at.ac.tuwien.cashtechthon.cep;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ac.tuwien.cashtechthon.PLAYfulSavingApiApplication;
import at.ac.tuwien.cashtechthon.cep.event.AccountBalanceEvent;
import at.ac.tuwien.cashtechthon.cep.event.ClassificationEvent;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PLAYfulSavingApiApplication.class)
@WebAppConfiguration
public class EventProcessorTest {
	private static EventProcessor eventProcessor;

	@BeforeClass
	public static void init() {
		eventProcessor = EventProcessor.getInstance();
	}

	@Test
	public void testAbsoluteThresholdWithZeroBalance() throws InterruptedException {
		AccountBalanceEvent balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(BigDecimal.ZERO);
		balanceEvent.setCustomerId(1L);
		LocalDateTime now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		CountingAlertCallback callback = new CountingAlertCallback();
		Long alertId = eventProcessor.createAbsoluteThreshold(BigDecimal.TEN, "positive", balanceEvent, callback);
		eventProcessor.addEvent(new ClassificationEvent(1L, "Transportation", now.plusSeconds(5L), new BigDecimal("4")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(10L), new BigDecimal("6")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Restaurants", now.plusSeconds(15L), new BigDecimal("1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(20L), new BigDecimal("-1")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(25L), new BigDecimal("-1")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(30L), new BigDecimal("1")));
		assertEquals(4, callback.getNumberOfCalls(alertId));
	}

	@Test
	public void testPositiveAbsoluteThresholdWithNonZeroBalance() {
		AccountBalanceEvent balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(new BigDecimal("5"));
		balanceEvent.setCustomerId(1L);
		LocalDateTime now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		CountingAlertCallback callback = new CountingAlertCallback();
		Long alertId = eventProcessor.createAbsoluteThreshold(BigDecimal.TEN, "positive", balanceEvent, callback);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(5L), new BigDecimal("4")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(10L), new BigDecimal("1")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
	}

	@Test
	public void testPositiveAbsoluteThresholdWithUpdatedBalance() {
		AccountBalanceEvent balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(BigDecimal.ZERO);
		balanceEvent.setCustomerId(1L);
		LocalDateTime now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		CountingAlertCallback callback = new CountingAlertCallback();
		Long alertId = eventProcessor.createAbsoluteThreshold(BigDecimal.TEN, "positive", balanceEvent, callback);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(5L), new BigDecimal("11")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(10L), new BigDecimal("9")));
		assertEquals(2, callback.getNumberOfCalls(alertId));

		// new balance event triggers alert!!!!
		balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(new BigDecimal("20"));
		balanceEvent.setCustomerId(1L);
		now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		eventProcessor.createAbsoluteThreshold(BigDecimal.TEN, "positive", balanceEvent, callback);
		assertEquals(3, callback.getNumberOfCalls(alertId));
	}

	@Test
	public void testPositiveAbsoluteThresholdWithoutClassifications() {
		AccountBalanceEvent balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(BigDecimal.TEN);
		balanceEvent.setCustomerId(1L);
		LocalDateTime now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		CountingAlertCallback callback = new CountingAlertCallback();
		Long alertId = eventProcessor.createAbsoluteThreshold(BigDecimal.TEN, "positive", balanceEvent, callback);
		// query cannot join balance event with classifications if there are no classifications for a customer
		// therefore no alert is triggered even though absolute threshold would have been reached
		assertEquals(0, callback.getNumberOfCalls(alertId));
	}

	@Test
	public void testNegativeAbsoluteThresholdWithZeroBalance() {
		AccountBalanceEvent balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(BigDecimal.ZERO);
		balanceEvent.setCustomerId(1L);
		LocalDateTime now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		CountingAlertCallback callback = new CountingAlertCallback();
		Long alertId = eventProcessor.createAbsoluteThreshold(BigDecimal.TEN, "negative", balanceEvent, callback);
		eventProcessor.addEvent(new ClassificationEvent(1L, "Transportation", now.plusSeconds(5L), new BigDecimal("-4")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(10L), new BigDecimal("-6")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Restaurants", now.plusSeconds(15L), new BigDecimal("-1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(20L), new BigDecimal("1")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(25L), new BigDecimal("1")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(30L), new BigDecimal("-1")));
		assertEquals(4, callback.getNumberOfCalls(alertId));
	}

	@Test
	public void testNegativeAbsoluteThresholdWithNonZeroBalance() {
		AccountBalanceEvent balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(new BigDecimal("-5"));
		balanceEvent.setCustomerId(1L);
		LocalDateTime now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		CountingAlertCallback callback = new CountingAlertCallback();
		Long alertId = eventProcessor.createAbsoluteThreshold(BigDecimal.TEN, "negative", balanceEvent, callback);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(5L), new BigDecimal("-4")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(10L), new BigDecimal("-1")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
	}

	@Test
	public void testNegativeAbsoluteThresholdWithUpdatedBalance() {
		AccountBalanceEvent balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(BigDecimal.ZERO);
		balanceEvent.setCustomerId(1L);
		LocalDateTime now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		CountingAlertCallback callback = new CountingAlertCallback();
		Long alertId = eventProcessor.createAbsoluteThreshold(BigDecimal.TEN, "negative", balanceEvent, callback);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(5L), new BigDecimal("-11")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(10L), new BigDecimal("-9")));
		assertEquals(2, callback.getNumberOfCalls(alertId));

		// new balance event triggers alert!!!!
		balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(new BigDecimal("-20"));
		balanceEvent.setCustomerId(1L);
		now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		eventProcessor.createAbsoluteThreshold(BigDecimal.TEN, "negative", balanceEvent, callback);
		assertEquals(3, callback.getNumberOfCalls(alertId));
	}

	@Test
	public void testNegativeAbsoluteThresholdWithoutClassifications() {
		AccountBalanceEvent balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(BigDecimal.TEN.negate());
		balanceEvent.setCustomerId(1L);
		LocalDateTime now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		CountingAlertCallback callback = new CountingAlertCallback();
		Long alertId = eventProcessor.createAbsoluteThreshold(BigDecimal.TEN, "negative", balanceEvent, callback);
		// query cannot join balance event with classifications if there are no classifications for a customer
		// therefore no alert is triggered even though absolute threshold would have been reached
		assertEquals(0, callback.getNumberOfCalls(alertId));
	}

	@Test
	public void testPositiveRelativeThreshold() {
		CountingAlertCallback callback = new CountingAlertCallback();
		RelativeThresholdParameter parameter = RelativeThresholdParameter.newInstance()
				.customerId(1L).thresholdInEur(BigDecimal.TEN).type("positive").direction("bidirectional")
				.callback(callback).build();
		Long alertId = eventProcessor.createRelativeThreshold(parameter);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		LocalDateTime now = LocalDateTime.now();
		eventProcessor.addEvent(new ClassificationEvent(1L, "Transportation", now.plusSeconds(5L), new BigDecimal("4")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(10L), new BigDecimal("6")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Restaurants", now.plusSeconds(15L), new BigDecimal("1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(20L), new BigDecimal("-1")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(25L), new BigDecimal("-1")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(30L), new BigDecimal("1")));
		assertEquals(4, callback.getNumberOfCalls(alertId));
	}
	
	@Test
	public void testNegativeRelativeThreshold() {
		CountingAlertCallback callback = new CountingAlertCallback();
		RelativeThresholdParameter parameter = RelativeThresholdParameter.newInstance()
				.customerId(1L).thresholdInEur(BigDecimal.TEN).type("negative").direction("bidirectional")
				.callback(callback).build();
		Long alertId = eventProcessor.createRelativeThreshold(parameter);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		LocalDateTime now = LocalDateTime.now();
		eventProcessor.addEvent(new ClassificationEvent(1L, "Transportation", now.plusSeconds(5L), new BigDecimal("-4")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(10L), new BigDecimal("-6")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Restaurants", now.plusSeconds(15L), new BigDecimal("-1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(20L), new BigDecimal("1")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(25L), new BigDecimal("1")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(30L), new BigDecimal("-1")));
		assertEquals(4, callback.getNumberOfCalls(alertId));
	}
	
	@Test
	public void testUnidirectionalPositiveRelativeThreshold() {
		CountingAlertCallback callback = new CountingAlertCallback();
		RelativeThresholdParameter parameter = RelativeThresholdParameter.newInstance()
				.customerId(1L).thresholdInEur(BigDecimal.TEN).type("positive").direction("unidirectional")
				.callback(callback).build();
		Long alertId = eventProcessor.createRelativeThreshold(parameter);
		LocalDateTime now = LocalDateTime.now();
		eventProcessor.addEvent(new ClassificationEvent(1L, "Transportation", now.plusSeconds(5L), new BigDecimal("4")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(10L), new BigDecimal("6")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Restaurants", now.plusSeconds(15L), new BigDecimal("1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(20L), new BigDecimal("-1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(25L), new BigDecimal("-1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(30L), new BigDecimal("1")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
	}
	
	@Test
	public void testUnidirectionalNegativeRelativeThreshold() {
		CountingAlertCallback callback = new CountingAlertCallback();
		RelativeThresholdParameter parameter = RelativeThresholdParameter.newInstance()
				.customerId(1L).thresholdInEur(BigDecimal.TEN).type("negative").direction("unidirectional")
				.callback(callback).build();
		Long alertId = eventProcessor.createRelativeThreshold(parameter);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		LocalDateTime now = LocalDateTime.now();
		eventProcessor.addEvent(new ClassificationEvent(1L, "Transportation", now.plusSeconds(5L), new BigDecimal("-4")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(10L), new BigDecimal("-6")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Restaurants", now.plusSeconds(15L), new BigDecimal("-1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(20L), new BigDecimal("1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(25L), new BigDecimal("1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(30L), new BigDecimal("-1")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
	}
	
	@Test
	public void testPositiveRelativeThresholdWithClassification() {
		CountingAlertCallback callback = new CountingAlertCallback();
		RelativeThresholdParameter parameter = RelativeThresholdParameter.newInstance()
				.customerId(1L).thresholdInEur(BigDecimal.TEN).type("positive").direction("bidirectional")
				.callback(callback).classification("Bars").build();
		Long alertId = eventProcessor.createRelativeThreshold(parameter);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		LocalDateTime now = LocalDateTime.now();
		eventProcessor.addEvent(new ClassificationEvent(1L, "Transportation", now.plusSeconds(5L), new BigDecimal("4")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(10L), new BigDecimal("6")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Restaurants", now.plusSeconds(15L), new BigDecimal("1")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(20L), new BigDecimal("8")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(25L), new BigDecimal("-4")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(30L), new BigDecimal("2")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
	}
	
	@Test
	public void testNegativeRelativeThresholdWithClassification() {
		CountingAlertCallback callback = new CountingAlertCallback();
		RelativeThresholdParameter parameter = RelativeThresholdParameter.newInstance()
				.customerId(1L).thresholdInEur(BigDecimal.TEN).type("negative").direction("bidirectional")
				.callback(callback).classification("Travel").build();
		Long alertId = eventProcessor.createRelativeThreshold(parameter);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		LocalDateTime now = LocalDateTime.now();
		eventProcessor.addEvent(new ClassificationEvent(1L, "Transportation", now.plusSeconds(5L), new BigDecimal("4")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(10L), new BigDecimal("-6")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Restaurants", now.plusSeconds(15L), new BigDecimal("1")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(20L), new BigDecimal("8")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(25L), new BigDecimal("-4")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Bars", now.plusSeconds(30L), new BigDecimal("2")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(35L), new BigDecimal("-4")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
	}
	
	@Test
	public void testUnidirectionalPositiveRelativeThresholdWithClassification() {
		CountingAlertCallback callback = new CountingAlertCallback();
		RelativeThresholdParameter parameter = RelativeThresholdParameter.newInstance()
				.customerId(1L).thresholdInEur(BigDecimal.TEN).type("positive").direction("unidirectional")
				.callback(callback).classification("System").build();
		Long alertId = eventProcessor.createRelativeThreshold(parameter);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		LocalDateTime now = LocalDateTime.now();
		eventProcessor.addEvent(new ClassificationEvent(1L, "Transportation", now.plusSeconds(5L), new BigDecimal("4")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(10L), new BigDecimal("6")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Restaurants", now.plusSeconds(15L), new BigDecimal("1")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "System", now.plusSeconds(20L), new BigDecimal("8")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(25L), new BigDecimal("-4")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "System", now.plusSeconds(30L), new BigDecimal("3")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "System", now.plusSeconds(35L), new BigDecimal("-1")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "System", now.plusSeconds(40L), new BigDecimal("1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
	}
	
	@Test
	public void testUnidirectionalNegativeRelativeThresholdWithClassification() {
		CountingAlertCallback callback = new CountingAlertCallback();
		RelativeThresholdParameter parameter = RelativeThresholdParameter.newInstance()
				.customerId(1L).thresholdInEur(BigDecimal.TEN).type("negative").direction("unidirectional")
				.callback(callback).classification("System").build();
		Long alertId = eventProcessor.createRelativeThreshold(parameter);
		assertEquals(0, callback.getNumberOfCalls(alertId));
		LocalDateTime now = LocalDateTime.now();
		eventProcessor.addEvent(new ClassificationEvent(1L, "Transportation", now.plusSeconds(5L), new BigDecimal("-4")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Travel", now.plusSeconds(10L), new BigDecimal("-6")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Restaurants", now.plusSeconds(15L), new BigDecimal("1")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "System", now.plusSeconds(20L), new BigDecimal("-8")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Cloths", now.plusSeconds(25L), new BigDecimal("4")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "System", now.plusSeconds(30L), new BigDecimal("-3")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "System", now.plusSeconds(35L), new BigDecimal("1")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "System", now.plusSeconds(40L), new BigDecimal("-1")));
		assertEquals(2, callback.getNumberOfCalls(alertId));
	}
	
	@Test
	public void testPositiveRelativeThresholdWithCycle() throws InterruptedException {
		CountingAlertCallback callback = new CountingAlertCallback();
		RelativeThresholdParameter parameter = RelativeThresholdParameter.newInstance()
				.customerId(1L).thresholdInEur(BigDecimal.TEN).type("negative").direction("unidirectional")
				.callback(callback).windowSize(Duration.ofSeconds(5L).toMillis()).build();
		Long alertId = eventProcessor.createRelativeThreshold(parameter);
		assertEquals(0, callback.getNumberOfCalls(alertId));		
		Thread.sleep(1_000L);
		eventProcessor.addEvent(new ClassificationEvent(1L, "Misc", LocalDateTime.now(), new BigDecimal("-5")));
		assertEquals(0, callback.getNumberOfCalls(alertId));
		
		Thread.sleep(5_000L);
		eventProcessor.addEvent(new ClassificationEvent(1L, "Misc", LocalDateTime.now(), new BigDecimal("-5")));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Misc", LocalDateTime.now(), new BigDecimal("-5")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		
		Thread.sleep(5_000L);
		eventProcessor.addEvent(new ClassificationEvent(1L, "Misc", LocalDateTime.now(), new BigDecimal("-5")));
		assertEquals(1, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Misc", LocalDateTime.now(), new BigDecimal("-5")));
		Thread.sleep(5_000L);
		
		assertEquals(2, callback.getNumberOfCalls(alertId));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Misc", LocalDateTime.now(), new BigDecimal("-10")));
		assertEquals(3, callback.getNumberOfCalls(alertId));
	}
	
	@Test
	public void testMultipleAbsoluteThresholds() {
		AccountBalanceEvent balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(BigDecimal.TEN);
		balanceEvent.setCustomerId(1L);
		LocalDateTime now = LocalDateTime.now();
		balanceEvent.setDeterminedAt(now);
		CountingAlertCallback callback = new CountingAlertCallback();
		Long savingsAlertId1 = eventProcessor.createAbsoluteThreshold(new BigDecimal("100"), "positive", balanceEvent, callback);
		eventProcessor.addEvent(new ClassificationEvent(1L, "Income", now.plusSeconds(5L), new BigDecimal("35")));
		assertEquals(0, callback.getNumberOfCalls(savingsAlertId1));
		balanceEvent = new AccountBalanceEvent();
		balanceEvent.setBalanceInEur(new BigDecimal("45"));
		balanceEvent.setCustomerId(1L);
		now = LocalDateTime.now().plusSeconds(6L);
		balanceEvent.setDeterminedAt(now);
		Long savingsAlertId2 = eventProcessor.createAbsoluteThreshold(new BigDecimal("200"), "positive", balanceEvent, callback);
		assertEquals(0, callback.getNumberOfCalls(savingsAlertId1));
		assertEquals(0, callback.getNumberOfCalls(savingsAlertId2));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Income", now.plusSeconds(10L), new BigDecimal("50")));
		assertEquals(0, callback.getNumberOfCalls(savingsAlertId1));
		assertEquals(0, callback.getNumberOfCalls(savingsAlertId2));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Income", now.plusSeconds(15L), new BigDecimal("5")));
		assertEquals(1, callback.getNumberOfCalls(savingsAlertId1));
		assertEquals(0, callback.getNumberOfCalls(savingsAlertId2));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Income", now.plusSeconds(20L), new BigDecimal("30")));
		assertEquals(2, callback.getNumberOfCalls(savingsAlertId1));
		assertEquals(0, callback.getNumberOfCalls(savingsAlertId2));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Income", now.plusSeconds(25L), new BigDecimal("65")));
		assertEquals(3, callback.getNumberOfCalls(savingsAlertId1));
		assertEquals(0, callback.getNumberOfCalls(savingsAlertId2));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Income", now.plusSeconds(30L), new BigDecimal("5")));
		assertEquals(4, callback.getNumberOfCalls(savingsAlertId1));
		assertEquals(1, callback.getNumberOfCalls(savingsAlertId2));
		eventProcessor.addEvent(new ClassificationEvent(1L, "Income", now.plusSeconds(35L), new BigDecimal("50")));
		assertEquals(5, callback.getNumberOfCalls(savingsAlertId1));
		assertEquals(2, callback.getNumberOfCalls(savingsAlertId2));
	}
	
//	@Test
//	public void testMultipleRelativeThresholds() {
//		CountingAlertCallback callback = new CountingAlertCallback();
//		RelativeThresholdParameter parameter = RelativeThresholdParameter.newInstance()
//				.customerId(1L).thresholdInEur(new BigDecimal("50")).type("positive").direction("bidirectional")
//				.callback(callback).build();
//		Long alertId1 = eventProcessor.createRelativeThreshold(parameter);
//	}

	private static class CountingAlertCallback implements AlertCallback {
		private Map<Long, Integer> alerts = new HashMap<>();

		@Override
		public void onAlert(Long alertId) {
			synchronized(alerts) {
				Integer alertCounter = alerts.get(alertId);
				if(alertCounter == null) {
					alerts.put(alertId, 1);
				} else {
					alerts.put(alertId, ++alertCounter);
				}
			}
		}

		public int getNumberOfCalls(Long alertId) {
			return alerts.getOrDefault(alertId, 0);
		}
	}

	
}

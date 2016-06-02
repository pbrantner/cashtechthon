package at.ac.tuwien.cashtechthon.cep;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ac.tuwien.cashtechthon.PLAYfulSavingApiApplication;
import at.ac.tuwien.cashtechthon.dtos.Classification;

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
	public void testEventProcessor() {
		Classification classification = new Classification();
		classification.setCustomerId(1L);
		classification.setFirstName("Bob");
		classification.setLastName("Dillan");
		classification.setClassifications(Arrays.asList("food"));
		eventProcessor.addEvent(classification);
	}
}

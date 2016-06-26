package at.ac.tuwien;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TransactionGeneratorApplication.class)
@WebAppConfiguration
@Ignore
public class TransactionGeneratorApplicationTests {

	@Test
	public void contextLoads() {
	}

}

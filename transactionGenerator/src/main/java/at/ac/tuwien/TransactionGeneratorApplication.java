package at.ac.tuwien;

import at.ac.tuwien.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

//@SpringBootApplication
@Component
public class TransactionGeneratorApplication {
	@Autowired
	private GeneratorService generatorService;

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx =
				new AnnotationConfigApplicationContext("at.ac.tuwien");

		TransactionGeneratorApplication main = ctx.getBean(TransactionGeneratorApplication.class);
		main.generatorService.start();
	}
}

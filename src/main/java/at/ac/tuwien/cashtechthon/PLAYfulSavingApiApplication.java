package at.ac.tuwien.cashtechthon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class PLAYfulSavingApiApplication {

	/*
	@RequestMapping("/")
	String home() {
		return "/app/index";
	}
*/
	public static void main(String[] args) {
		SpringApplication.run(PLAYfulSavingApiApplication.class, args);
	}
}

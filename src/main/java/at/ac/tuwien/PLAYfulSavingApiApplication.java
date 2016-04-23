package at.ac.tuwien;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class PLAYfulSavingApiApplication {

	@RequestMapping("/")
	String home() {
		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(PLAYfulSavingApiApplication.class, args);
	}
}

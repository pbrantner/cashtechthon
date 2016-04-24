package at.ac.tuwien.cashtechthon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@EntityScan(
		basePackageClasses = { PLAYfulSavingApiApplication.class, Jsr310JpaConverters.class }
)
public class PLAYfulSavingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PLAYfulSavingApiApplication.class, args);
	}
}

package at.ac.tuwien.cashtechthon.cep;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AgeFunction {
	public static int calculateAge(LocalDateTime birthday) {
		return (int)ChronoUnit.YEARS.between(birthday, LocalDateTime.now());
	}
}

package at.ac.tuwien.cashtechthon.cep;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeFunction {
	public static long calculateMilliseconds(LocalDateTime time) {
		return time.toEpochSecond(ZoneOffset.UTC);
	}
	
	public static LocalDateTime getStartOfMonth(LocalDateTime time) {
		return time.withDayOfMonth(1).withHour(0).withHour(0).withMinute(0).withSecond(0).withNano(0);
	}
}

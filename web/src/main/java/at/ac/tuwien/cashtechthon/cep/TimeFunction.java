package at.ac.tuwien.cashtechthon.cep;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeFunction {
	public static long calculateMilliseconds(LocalDateTime time) {
		return time.toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
	public static LocalDateTime getStartOfMonth(LocalDateTime time) {
		return time.withDayOfMonth(1).withHour(0).withHour(0).withMinute(0).withSecond(0).withNano(0);
	}
	
	public static long calculateWindowEnd(long windowStart, long windowSize) {
		long windowEnd = windowStart;
		long now = calculateMilliseconds(LocalDateTime.now());
		while(windowEnd < now) {
			windowEnd += windowSize;
		}
		return windowEnd;
	}
	
	public static boolean isInCycle(LocalDateTime timestamp, long windowStart, long windowSize) {
		long millis = calculateMilliseconds(timestamp);
		long windowEnd = calculateWindowEnd(windowStart, windowSize);
		return (millis > (windowEnd - windowSize)) && (millis <= windowEnd);
	}
}

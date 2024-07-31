package me.mourjo.utils.datetime;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DatetimeConverter {

	public static OffsetDateTime localDateTimeToUTC(LocalDateTime dt) {
		return OffsetDateTime.of(dt, ZoneOffset.UTC);
	}

	public static OffsetDateTime utcOffsetDateTime(int year, int month, int dayOfMonth, int hour,
			int minute, int second) {
		return localDateTimeToUTC(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second));
	}

	public static OffsetDateTime amsterdamOffsetDateTime(int year, int month, int dayOfMonth,
			int hour,
			int minute, int second) {
		return ZonedDateTime.of(
						LocalDateTime.of(year, month, dayOfMonth, hour, minute, second),
						ZoneId.of("Europe/Amsterdam"))
				.toOffsetDateTime();
	}
}

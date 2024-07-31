package me.mourjo.utils.datetime;

import static java.util.function.Predicate.not;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RelativeDatetimeFormat {

	public static String inWords(long number, ChronoUnit unit) {
		if (number == 0) {
			return null;
		}
		String unitNamePlural = unit.name().toLowerCase();
		String unitNameSingular = unitNamePlural.substring(0, unitNamePlural.length() - 1);
		String unitInWords = number == 1 ? unitNameSingular : unitNamePlural;
		return number + " " + unitInWords;
	}

	public static String formatDatetime(Clock clock, OffsetDateTime dt) {
		var now = ZonedDateTime.now(clock);

		if (ChronoUnit.MILLIS.between(dt, now) < 1000) {
			return "Right now";
		}

		long nDays = ChronoUnit.DAYS.between(dt, now);
		now = now.minusDays(nDays);

		long nHours = ChronoUnit.HOURS.between(dt, now);
		now = now.minusHours(nHours);

		long nMins = ChronoUnit.MINUTES.between(dt, now);
		now = now.minusMinutes(nMins);

		long nSecs = ChronoUnit.SECONDS.between(dt, now);

		return Stream.of(
						inWords(nDays, ChronoUnit.DAYS),
						inWords(nHours, ChronoUnit.HOURS),
						inWords(nMins, ChronoUnit.MINUTES),
						inWords(nSecs, ChronoUnit.SECONDS)
				).filter(not(Objects::isNull))
				.collect(Collectors.joining(", "))
				+ " ago";
	}
}

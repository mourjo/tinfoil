package me.mourjo.usecases;

import static java.util.function.Predicate.not;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import me.mourjo.services.CustomerRepository;

public class ViewVisitsUseCase {

	CustomerRepository repository;
	Clock clock;

	public ViewVisitsUseCase(CustomerRepository repository, Clock clock) {
		this.repository = repository;
		this.clock = clock;
	}

	private static String inWords(long number, ChronoUnit unit) {
		if (number == 0) {
			return null;
		}
		String unitNamePlural = unit.name().toLowerCase();
		String unitNameSingular = unitNamePlural.substring(0, unitNamePlural.length() - 1);
		String unitInWords = number == 1 ? unitNameSingular : unitNamePlural;
		return number + " " + unitInWords;
	}

	public Map<Store, List<String>> viewVisits(Customer customer) {
		Map<Store, List<String>> result = new HashMap<>();
		var storeVisits = new HashMap<>(repository.getAllVisits(customer));

		for (Entry<Store, List<ZonedDateTime>> visit : storeVisits.entrySet()) {
			visit.getValue().sort(ChronoZonedDateTime::compareTo);
			var strings = visit.getValue().stream().map(this::dateFormat).toList();
			result.put(visit.getKey(), strings);
		}

		return result;
	}

	private String dateFormat(ZonedDateTime dt) {
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

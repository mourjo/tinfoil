package me.mourjo.usecases;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.mourjo.entities.Customer;
import me.mourjo.services.CustomerRepository;

public class ViewVisitsUseCase {

	CustomerRepository repository;
	Clock clock;

	public ViewVisitsUseCase(CustomerRepository repository, Clock clock) {
		this.repository = repository;
		this.clock = clock;
	}

	public List<String> viewVisits(Customer customer) {
		var visits = new ArrayList<>(repository.getVisits(customer));

		visits.sort(ChronoZonedDateTime::compareTo);

		return visits.stream().map(dt -> dateFormat(dt)).toList();
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
						nDays != 0 ? (nDays == 1 ? (nDays + " day") : (nDays + " days")) : "",
						nHours != 0 ? (nHours == 1 ? (nHours + " hour") : (nHours + " hours")) : "",
						nMins != 0 ? (nMins == 1 ? (nMins + " minute") : (nMins + " minutes")) : "",
						nSecs != 0 ? (nSecs == 1 ? (nSecs + " second") : (nSecs + " seconds")) : ""
				).filter(s -> !s.isBlank())
				.collect(Collectors.joining(", "))
				+ " ago";
	}

}

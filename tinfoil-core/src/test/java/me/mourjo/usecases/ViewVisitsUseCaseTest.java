package me.mourjo.usecases;

import static me.mourjo.utils.datetime.DatetimeConverter.amsterdamOffsetDateTime;
import static me.mourjo.utils.datetime.DatetimeConverter.utcOffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import me.mourjo.services.VisitRepository;
import me.mourjo.services.InMemoryVisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewVisitsUseCaseTest {

	final OffsetDateTime fixedTime = utcOffsetDateTime(2024, 7, 31, 1, 2, 28);
	final Clock clock = Clock.fixed(fixedTime.toInstant(), ZoneId.of("Etc/UTC"));

	Customer customer;
	VisitRepository repo;

	Store store;
	ViewVisitsUseCase useCase;

	@BeforeEach
	void startup() {
		repo = new InMemoryVisitRepository();
		useCase = new ViewVisitsUseCase(repo, clock);
		store = new Store("A store");
		customer = new Customer("A customer");
	}

	@Test
	void multipleVisitsTest() {
		var firstVisitTime = amsterdamOffsetDateTime(2024, 7, 20, 11, 12, 0);
		var secondVisitTime = fixedTime.minusDays(3).minusHours(8).minusSeconds(2);

		repo.recordVisit(store, customer, firstVisitTime);
		repo.recordVisit(store, customer, secondVisitTime);

		assertEquals(
				Map.of(store, List.of(
						"10 days, 15 hours, 50 minutes, 28 seconds ago",
						"3 days, 8 hours, 2 seconds ago")),
				useCase.viewVisits(customer));
	}

	@Test
	void singularWordsTest() {
		var firstVisitTime = fixedTime.minusDays(1);
		var secondVisitTime = fixedTime.minusMinutes(1);

		repo.recordVisit(store, customer, firstVisitTime);
		repo.recordVisit(store, customer, secondVisitTime);

		assertEquals(
				Map.of(store, List.of("1 day ago", "1 minute ago")),
				useCase.viewVisits(customer));
	}

	@Test
	void pluralWordsTest() {
		var firstVisitTime = fixedTime.minusDays(2);
		var secondVisitTime = fixedTime.minusSeconds(3);

		repo.recordVisit(store, customer, firstVisitTime);
		repo.recordVisit(store, customer, secondVisitTime);

		assertEquals(
				Map.of(store, List.of("2 days ago", "3 seconds ago")),
				useCase.viewVisits(customer));
	}

	@Test
	void viewVisitsFromFutureTest() {
		var firstVisitTime = utcOffsetDateTime(2050, 12, 2, 1, 1, 0);
		repo.recordVisit(store, customer, firstVisitTime);

		assertEquals(
				Map.of(store, List.of("Right now")),
				useCase.viewVisits(customer));
	}

}

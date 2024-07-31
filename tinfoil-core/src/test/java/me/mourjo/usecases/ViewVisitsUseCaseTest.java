package me.mourjo.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import me.mourjo.services.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewVisitsUseCaseTest {

	final ZonedDateTime fixedTime = ZonedDateTime.of(
			LocalDateTime.of(2024, 7, 31, 1, 2, 28),
			ZoneOffset.UTC);
	final Clock clock = Clock.fixed(fixedTime.toInstant(), ZoneId.of("Etc/UTC"));
	Customer customer;
	CustomerRepository repo;
	Store store;
	ViewVisitsUseCase sut;

	@BeforeEach
	void startup() {
		repo = new CustomerRepoImpl();
		sut = new ViewVisitsUseCase(repo, clock);
		store = new Store("A store");
		customer = new Customer("A customer");
	}

	@Test
	void multipleVisitsTest() {
		var firstVisitLocalTime = LocalDateTime.of(2024, 7, 20, 11, 12, 0);
		var firstVisitTime = ZonedDateTime.of(firstVisitLocalTime, ZoneId.of("Europe/Amsterdam"));
		var secondVisitTime = fixedTime.minusDays(3).minusHours(8).minusSeconds(2);

		repo.recordVisit(store, customer, firstVisitTime);
		repo.recordVisit(store, customer, secondVisitTime);

		assertEquals(
				Map.of(store, List.of(
						"10 days, 15 hours, 50 minutes, 28 seconds ago",
						"3 days, 8 hours, 2 seconds ago")),
				sut.viewVisits(customer));
	}

	@Test
	void singularWordsTest() {
		var firstVisitTime = fixedTime.minusDays(1);
		var secondVisitTime = fixedTime.minusMinutes(1);

		repo.recordVisit(store, customer, firstVisitTime);
		repo.recordVisit(store, customer, secondVisitTime);

		assertEquals(
				Map.of(store, List.of("1 day ago", "1 minute ago")),
				sut.viewVisits(customer));
	}

	@Test
	void pluralWordsTest() {
		var firstVisitTime = fixedTime.minusDays(2);
		var secondVisitTime = fixedTime.minusSeconds(3);

		repo.recordVisit(store, customer, firstVisitTime);
		repo.recordVisit(store, customer, secondVisitTime);

		assertEquals(
				Map.of(store, List.of("2 days ago", "3 seconds ago")),
				sut.viewVisits(customer));
	}

	@Test
	void viewVisitsFromFutureTest() {
		var localTime = LocalDateTime.of(2030, 12, 2, 1, 1, 0);
		var firstVisitTime = ZonedDateTime.of(localTime, ZoneId.of("Europe/Amsterdam"));

		repo.recordVisit(store, customer, firstVisitTime);

		assertEquals(
				Map.of(store, List.of("Right now")),
				sut.viewVisits(customer));
	}

	private static class CustomerRepoImpl implements CustomerRepository {

		Map<Store, List<ZonedDateTime>> visits = new HashMap<>();

		@Override
		public void recordVisit(Store store, Customer customer, ZonedDateTime timestamp) {
			visits.putIfAbsent(store, new ArrayList<>());
			visits.get(store).add(timestamp);
		}

		@Override
		public Map<Store, List<ZonedDateTime>> getAllVisits(Customer customer) {
			return visits;
		}

		@Override
		public List<ZonedDateTime> getStoreVisits(Store store, Customer customer) {
			return visits.get(store);
		}

		@Override
		public void delete(Customer customer) {

		}
	}
}

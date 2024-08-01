package me.mourjo.services;

import static me.mourjo.utils.datetime.DatetimeConverter.utcOffsetDateTime;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostgresVisitRepositoryTest {

	PostgresVisitRepository repo = new PostgresVisitRepository("justin", "hat",
			"tinfoil_test_db");

	@BeforeEach
	void setUp() {
		repo.truncate();
	}

	@Test
	public void visitsFromDB() {
		Store store = new Store("Albert Heijn");
		Customer customer = new Customer("Dustin");

		var firstVisitTime = utcOffsetDateTime(2024, 6, 9, 10, 2, 30);
		var secondVisitTime = utcOffsetDateTime(2024, 7, 10, 1, 8, 38);

		repo.recordVisit(store, customer, firstVisitTime);
		repo.recordVisit(store, customer, secondVisitTime);

		Map<Store, List<OffsetDateTime>> visits = repo.visitsInChronologicalOrder(customer);
		Assertions.assertEquals(Set.of(store), visits.keySet());
		Assertions.assertEquals(
				List.of(
						firstVisitTime.toInstant(),
						secondVisitTime.toInstant()
				),
				visits.get(store).stream().map(OffsetDateTime::toInstant).toList());
	}
}

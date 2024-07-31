package me.mourjo.services;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostgresCustomerRepositoryTest {

	PostgresCustomerRepository repo = new PostgresCustomerRepository("justin", "hat",
			"tinfoil_test_db");

	@BeforeEach
	void setUp() {
		repo.truncate();
	}

	@Test
	public void visitsFromDB() {
		Store s = new Store("Albert Heijn");
		Customer c = new Customer("Dustin");

		var firstVisitTime = OffsetDateTime.of(LocalDateTime.of(2024, 6, 9, 10, 2, 30),
				ZoneOffset.UTC);
		var secondVisitTime = OffsetDateTime.of(LocalDateTime.of(2024, 7, 10, 1, 8, 38),
				ZoneOffset.UTC);

		repo.recordVisit(s, c, firstVisitTime);
		repo.recordVisit(s, c, secondVisitTime);

		Map<Store, List<OffsetDateTime>> visits = repo.getAllVisits(c);
		Assertions.assertEquals(Set.of(s), visits.keySet());
		Assertions.assertEquals(
				List.of(
						firstVisitTime.toInstant(),
						secondVisitTime.toInstant()
				),
				visits.get(s).stream().map(OffsetDateTime::toInstant).toList());
	}
}

package me.mourjo.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import me.mourjo.entities.Customer;
import me.mourjo.services.CustomerRepository;
import org.junit.jupiter.api.Test;

class ViewVisitsUseCaseTest {

	final ZonedDateTime fixedTime = ZonedDateTime.of(
			LocalDateTime.of(2024, 7, 31, 1, 2, 28),
			ZoneOffset.UTC);
	final Clock clock = Clock.fixed(fixedTime.toInstant(), ZoneId.of("Etc/UTC"));
	final Customer customer = new Customer("A customer");

	@Test
	void multipleVisitsTest() {
		var firstVisitLocalTime = LocalDateTime.of(2024, 7, 20, 11, 12, 0);
		var firstVisitTime = ZonedDateTime.of(firstVisitLocalTime, ZoneId.of("Europe/Amsterdam"));

		var secondVisitTime = fixedTime.minusDays(3).minusHours(8).minusSeconds(2);

		var repo = new CustomerRepoImpl(List.of(firstVisitTime, secondVisitTime));
		var sut = new ViewVisitsUseCase(repo, clock);

		assertEquals(
				List.of(
						"10 days, 15 hours, 50 minutes, 28 seconds ago",
						"3 days, 8 hours, 2 seconds ago"),
				sut.viewVisits(customer));
	}

	@Test
	void singularWordsTest() {
		var firstVisitTime = fixedTime.minusDays(1);
		var secondVisitTime = fixedTime.minusMinutes(1);
		var repo = new CustomerRepoImpl(List.of(firstVisitTime, secondVisitTime));
		var sut = new ViewVisitsUseCase(repo, clock);

		assertEquals(
				List.of("1 day ago", "1 minute ago"),
				sut.viewVisits(customer));
	}

	@Test
	void pluralWordsTest() {
		var firstVisitTime = fixedTime.minusDays(2);
		var secondVisitTime = fixedTime.minusSeconds(3);
		var repo = new CustomerRepoImpl(List.of(firstVisitTime, secondVisitTime));
		var sut = new ViewVisitsUseCase(repo, clock);

		assertEquals(
				List.of("2 days ago", "3 seconds ago"),
				sut.viewVisits(customer));
	}

	@Test
	void viewVisitsFromFutureTest() {
		var localTime = LocalDateTime.of(2030, 12, 2, 1, 1, 0);
		var firstVisitTime = ZonedDateTime.of(localTime, ZoneId.of("Europe/Amsterdam"));

		var repo = new CustomerRepoImpl(List.of(firstVisitTime));
		var sut = new ViewVisitsUseCase(repo, clock);

		assertEquals(
				List.of("Right now"),
				sut.viewVisits(customer));
	}

	private static class CustomerRepoImpl implements CustomerRepository {

		List<ZonedDateTime> visits;

		public CustomerRepoImpl(List<ZonedDateTime> visits) {
			this.visits = visits;
		}

		@Override
		public void recordVisit(Customer customer) {

		}

		@Override
		public List<ZonedDateTime> getVisits(Customer customer) {
			return visits;
		}

		@Override
		public void delete(Customer customer) {

		}
	}

}

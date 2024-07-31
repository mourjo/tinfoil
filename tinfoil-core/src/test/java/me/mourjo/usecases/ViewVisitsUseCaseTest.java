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

	@Test
	void viewVisitsTest() {
		var customer = new Customer("A customer");
		var localTime = LocalDateTime.of(2024, 8, 20, 11, 12, 0);
		var july20 = ZonedDateTime.of(localTime, ZoneId.of("Europe/Amsterdam"));
		var aMinuteAgo = fixedTime.minusMinutes(1);
		var repo = new CustomerRepoImpl(List.of(july20, aMinuteAgo));
		var sut = new ViewVisitsUseCase(repo, clock);

		assertEquals(
				List.of("10 days, 15 hours, 50 minutes, 28 seconds ago", "1 minute ago"),
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

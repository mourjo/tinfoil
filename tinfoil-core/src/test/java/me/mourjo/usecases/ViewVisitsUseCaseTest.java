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

	final ZonedDateTime fixedTime = ZonedDateTime.of(LocalDateTime.of(2024, 7, 31, 1, 2, 28),
			ZoneOffset.UTC);
	final Clock clock = Clock.fixed(fixedTime.toInstant(), ZoneId.of("Etc/UTC"));

	@Test
	void viewVisitsTest() {
		var ts = ZonedDateTime.of(LocalDateTime.of(2024, 7, 20, 11, 12, 0),
				ZoneId.of("Europe/Amsterdam"));
		var aMinuteAgo = fixedTime.minusMinutes(1);
		var repo = new TestRepo(List.of(ts, aMinuteAgo));
		var actual = new ViewVisitsUseCase(repo, clock).viewVisits(new Customer("A customer"));
		assertEquals(List.of("10 days, 15 hours, 50 minutes, 28 seconds ago", "1 minute ago"),
				actual);
	}

	private class TestRepo implements CustomerRepository {

		List<ZonedDateTime> visits;

		public TestRepo(List<ZonedDateTime> visits) {
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

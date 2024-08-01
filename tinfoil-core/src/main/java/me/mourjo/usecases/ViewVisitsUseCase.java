package me.mourjo.usecases;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import me.mourjo.services.VisitRepository;
import me.mourjo.utils.datetime.RelativeDatetimeFormat;

public class ViewVisitsUseCase {

	VisitRepository repository;
	Clock clock;

	public ViewVisitsUseCase(VisitRepository repository, Clock clock) {
		this.repository = repository;
		this.clock = clock;
	}

	public ViewVisitsUseCase(VisitRepository repository) {
		this.repository = repository;
		this.clock = Clock.systemDefaultZone();
	}

	public Map<Store, List<String>> viewVisits(Customer customer) {
		Map<Store, List<String>> result = new HashMap<>();
		var storeVisits = repository.visitsInChronologicalOrder(customer);

		for (Entry<Store, List<OffsetDateTime>> visit : storeVisits.entrySet()) {
			var strings = visit.getValue().stream()
					.map(dateTime -> RelativeDatetimeFormat.formatDatetime(clock, dateTime))
					.toList();
			result.put(visit.getKey(), strings);
		}

		return result;
	}

}

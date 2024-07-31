package me.mourjo.usecases;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import me.mourjo.services.CustomerRepository;
import me.mourjo.utils.formatting.RelativeDatetimeFormat;

public class ViewVisitsUseCase {

	CustomerRepository repository;
	Clock clock;

	public ViewVisitsUseCase(CustomerRepository repository, Clock clock) {
		this.repository = repository;
		this.clock = clock;
	}

	public Map<Store, List<String>> viewVisits(Customer customer) {
		Map<Store, List<String>> result = new HashMap<>();
		var storeVisits = repository.getAllVisits(customer);

		for (Entry<Store, List<OffsetDateTime>> visit : storeVisits.entrySet()) {
			visit.getValue().sort(OffsetDateTime::compareTo);
			var strings = visit.getValue().stream()
					.map(dateTime -> RelativeDatetimeFormat.formatDatetime(clock, dateTime))
					.toList();
			result.put(visit.getKey(), strings);
		}

		return result;
	}

}

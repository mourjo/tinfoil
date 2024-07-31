package me.mourjo.services;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;

public class InMemoryCustomerRepository implements CustomerRepository {

	Map<Store, List<OffsetDateTime>> visits = new HashMap<>();

	@Override
	public void recordVisit(Store store, Customer customer, OffsetDateTime timestamp) {
		visits.putIfAbsent(store, new ArrayList<>());
		visits.get(store).add(timestamp);
		visits.get(store).sort(OffsetDateTime::compareTo);
	}

	@Override
	public Map<Store, List<OffsetDateTime>> visitsInChronologicalOrder(Customer customer) {
		return visits;
	}

	@Override
	public void delete(Customer customer) {

	}

}

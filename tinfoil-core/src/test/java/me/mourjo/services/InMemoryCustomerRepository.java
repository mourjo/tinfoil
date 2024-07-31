package me.mourjo.services;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;

public class InMemoryCustomerRepository implements CustomerRepository {

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

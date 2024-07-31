package me.mourjo.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;

public interface CustomerRepository {

	void recordVisit(Store store, Customer customer, ZonedDateTime time);

	Map<Store, List<ZonedDateTime>> getAllVisits(Customer customer);

	List<ZonedDateTime> getStoreVisits(Store store, Customer customer);

	void delete(Customer customer);
}

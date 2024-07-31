package me.mourjo.services;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;

public interface CustomerRepository {

	void recordVisit(Store store, Customer customer, OffsetDateTime time);

	Map<Store, List<OffsetDateTime>> getAllVisits(Customer customer);

	List<OffsetDateTime> getStoreVisits(Store store, Customer customer);

	void delete(Customer customer);
}

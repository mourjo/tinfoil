package me.mourjo.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;

public interface VisitRepository {

	void recordVisit(Store store, Customer customer, OffsetDateTime time);

	Map<Store, List<OffsetDateTime>> visitsInChronologicalOrder(Customer customer);

	void delete(Customer customer);
}

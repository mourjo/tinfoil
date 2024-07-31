package me.mourjo.services;

import java.time.ZonedDateTime;
import java.util.List;
import me.mourjo.entities.Customer;

public interface CustomerRepository {

	void recordVisit(Customer customer);

	List<ZonedDateTime> getVisits(Customer customer);

	void delete(Customer customer);
}

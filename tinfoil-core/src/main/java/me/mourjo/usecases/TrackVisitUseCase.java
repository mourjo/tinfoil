package me.mourjo.usecases;

import java.time.ZonedDateTime;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import me.mourjo.services.CustomerRepository;

public class TrackVisitUseCase {

	CustomerRepository repository;

	public String visit(Store store, Customer customer) {
		repository.recordVisit(store, customer, ZonedDateTime.now());
		return "Welcome, " + customer.getName();
	}
}

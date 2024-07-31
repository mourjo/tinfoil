package me.mourjo.usecases;

import me.mourjo.entities.Customer;
import me.mourjo.services.CustomerRepository;

public class TrackVisitUseCase {

	CustomerRepository repository;

	public String visit(Customer customer) {
		repository.recordVisit(customer);
		return "Welcome, " + customer.getName();
	}
}

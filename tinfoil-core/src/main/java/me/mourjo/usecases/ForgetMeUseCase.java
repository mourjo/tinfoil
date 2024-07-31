package me.mourjo.usecases;

import me.mourjo.entities.Customer;
import me.mourjo.services.CustomerRepository;

public class ForgetMeUseCase {

	CustomerRepository repository;

	public String forgetMe(Customer customer) {
		repository.delete(customer);
		return "Sorry to see you go";
	}
}

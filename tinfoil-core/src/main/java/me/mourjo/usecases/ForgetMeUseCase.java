package me.mourjo.usecases;

import me.mourjo.entities.Customer;
import me.mourjo.services.VisitRepository;

public class ForgetMeUseCase {

	VisitRepository repository;

	public String forgetMe(Customer customer) {
		repository.delete(customer);
		return "Sorry to see you go";
	}
}

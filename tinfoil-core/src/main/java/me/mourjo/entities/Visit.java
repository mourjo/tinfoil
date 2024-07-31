package me.mourjo.entities;

import java.util.Objects;

public class Visit {

	Customer customer;
	Store store;

	@Override
	public String toString() {
		return "Visit{" +
				"customer=" + customer +
				", store=" + store +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Visit visit = (Visit) o;
		return Objects.equals(customer, visit.customer) && Objects.equals(store,
				visit.store);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customer, store);
	}
}

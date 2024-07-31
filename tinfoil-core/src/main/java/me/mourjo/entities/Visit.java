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
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		Visit visit = (Visit) other;
		return Objects.equals(customer, visit.customer) && Objects.equals(store,
				visit.store);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customer, store);
	}
}

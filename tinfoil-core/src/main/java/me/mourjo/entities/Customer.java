package me.mourjo.entities;

import java.util.Objects;

public class Customer {

	String name;

	public Customer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		Customer customer = (Customer) other;
		return Objects.equals(name, customer.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "Customer{%s}".formatted(name);
	}
}

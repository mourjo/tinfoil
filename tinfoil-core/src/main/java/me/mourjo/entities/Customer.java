package me.mourjo.entities;

import java.util.Objects;

public class Customer {

	String name;
	String country;

	public Customer(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Customer customer = (Customer) o;
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

package me.mourjo.entities;

import java.util.Objects;

public class Store {

	String name;
	String country;

	public Store(String name) {
		this.name = name;
	}

	public Store(String name, String country) {
		this.name = name;
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Store{%s}".formatted(name);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		Store store = (Store) other;
		return Objects.equals(name, store.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}

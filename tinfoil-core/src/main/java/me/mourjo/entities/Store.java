package me.mourjo.entities;

import java.util.Objects;

public class Store {

	String name;

	public Store(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Store{%s}".formatted(name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Store store = (Store) o;
		return Objects.equals(name, store.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}

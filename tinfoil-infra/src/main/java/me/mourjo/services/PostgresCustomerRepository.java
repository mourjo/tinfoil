package me.mourjo.services;

import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import org.jooq.Asterisk;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;

@Slf4j
public class PostgresCustomerRepository implements CustomerRepository {

	private final String username;
	private final String password;
	private final String connectionString;
	private Table<Record> customerTable = table("customer");
	private Table<Record> visitTable = table("visit");
	private Table<Record> storeTable = table("store");
	private Field<String> nameField = field("name", String.class);
	private Field<String> customerField = field("customer", String.class);
	private Field<String> storeField = field("store", String.class);
	private Field<OffsetDateTime> visitedAtField = field("visited_at", OffsetDateTime.class);
	private Asterisk star = asterisk();
	public PostgresCustomerRepository(String host, String port, String database, String username,
			String password) {
		this.username = username;
		this.password = password;
		connectionString = "jdbc:postgresql://%s:%s/%s".formatted(host, port, database);
	}
	public PostgresCustomerRepository(String username, String password, String database) {
		this("localhost", "5432", database, "justin", "hat");
	}

	@SneakyThrows
	private Connection getConnection() {
		return DriverManager.getConnection(connectionString, username, password);
	}

	@SneakyThrows
	@Override
	public void recordVisit(Store store, Customer customer, OffsetDateTime time) {
		try (Connection conn = getConnection()) {
			DSL.using(conn, SQLDialect.POSTGRES)
					.insertInto(customerTable)
					.columns(nameField)
					.values(customer.getName())
					.onConflictDoNothing()
					.execute();
		}

		try (Connection conn = getConnection()) {
			DSL.using(conn, SQLDialect.POSTGRES)
					.insertInto(storeTable)
					.columns(nameField)
					.values(store.getName())
					.onConflictDoNothing()
					.execute();
		}

		try (Connection conn = getConnection()) {
			DSL.using(conn, SQLDialect.POSTGRES)
					.insertInto(visitTable)
					.columns(customerField, storeField, visitedAtField)
					.values(customer.getName(), store.getName(), time)
					.execute();
		}
	}

	@SneakyThrows
	@Override
	public Map<Store, List<OffsetDateTime>> getAllVisits(Customer customer) {
		Map<Store, List<OffsetDateTime>> visits = new HashMap<>();
		try (Connection conn = getConnection()) {

			var rows = DSL.using(conn, SQLDialect.POSTGRES)
					.select(star)
					.from(visitTable)
					.where(customerField.equal(customer.getName()))
					.fetch();

			for (var visit : rows) {
				String storeName = visit.get(storeField);
				Store store = new Store(storeName);

				var ts = visit.get(visitedAtField);
				visits.putIfAbsent(store, new ArrayList<>());
				visits.get(store).add(ts);
			}
		}
		return visits;
	}

	@Override
	public List<OffsetDateTime> getStoreVisits(Store store, Customer customer) {
		return null;
	}

	@Override
	public void delete(Customer customer) {

	}

	@SneakyThrows
	void truncate() {
		try (Connection conn = getConnection()) {
			DSL.using(conn, SQLDialect.POSTGRES)
					.delete(visitTable)
					.execute();

			DSL.using(conn, SQLDialect.POSTGRES)
					.delete(customerTable)
					.execute();

			DSL.using(conn, SQLDialect.POSTGRES)
					.delete(storeTable)
					.execute();


		}
	}
}

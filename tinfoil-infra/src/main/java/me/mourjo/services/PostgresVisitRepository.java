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
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;

@Slf4j
public class PostgresVisitRepository implements VisitRepository {

	private final String username;
	private final String password;
	private final String connectionString;

	private final Table<Record> customerTable = table("customer");
	private final Table<Record> visitTable = table("visit");
	private final Table<Record> storeTable = table("store");
	private final Field<String> nameField = field("name", String.class);
	private final Field<String> customerField = field("customer", String.class);
	private final Field<String> storeField = field("store", String.class);
	private final Field<OffsetDateTime> visitedAtField = field("visited_at", OffsetDateTime.class);
	private final Asterisk star = asterisk();

	public PostgresVisitRepository(String host, String port, String database, String username,
			String password) {
		this.username = username;
		this.password = password;
		connectionString = "jdbc:postgresql://%s:%s/%s".formatted(host, port, database);
	}

	public PostgresVisitRepository(String username, String password, String database) {
		this("localhost", "5432", database, "justin", "hat");
	}

	@SneakyThrows
	private Connection getConnection() {
		return DriverManager.getConnection(connectionString, username, password);
	}

	private DSLContext using(Connection conn) {
		return DSL.using(conn, SQLDialect.POSTGRES);
	}

	@SneakyThrows
	@Override
	public void recordVisit(Store store, Customer customer, OffsetDateTime time) {
		try (Connection conn = getConnection()) {
			using(conn)
					.insertInto(customerTable)
					.columns(nameField)
					.values(customer.getName())
					.onConflictDoNothing()
					.execute();

			using(conn)
					.insertInto(storeTable)
					.columns(nameField)
					.values(store.getName())
					.onConflictDoNothing()
					.execute();

			using(conn)
					.insertInto(visitTable)
					.columns(customerField, storeField, visitedAtField)
					.values(customer.getName(), store.getName(), time)
					.execute();
		}
	}

	@SneakyThrows
	@Override
	public Map<Store, List<OffsetDateTime>> visitsInChronologicalOrder(Customer customer) {
		Map<Store, List<OffsetDateTime>> visits = new HashMap<>();
		try (Connection conn = getConnection()) {

			var rows = using(conn)
					.select(star)
					.from(visitTable)
					.where(customerField.equal(customer.getName()))
					.orderBy(visitedAtField.asc())
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
	public void delete(Customer customer) {

	}

	@SneakyThrows
	public void truncate() {
		try (Connection conn = getConnection()) {
			using(conn)
					.delete(visitTable)
					.execute();

			using(conn)
					.delete(customerTable)
					.execute();

			using(conn)
					.delete(storeTable)
					.execute();


		}
	}
}

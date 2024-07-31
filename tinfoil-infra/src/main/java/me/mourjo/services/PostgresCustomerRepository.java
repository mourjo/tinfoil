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
	private Table<Record> customerTable = table("customer");
	private Table<Record> visitTable = table("visit");
	private Table<Record> storeTable = table("store");
	private Field<String> nameField = field("name", String.class);
	private Field<String> customerField = field("customer", String.class);
	private Field<String> storeField = field("store", String.class);
	private Field<OffsetDateTime> visitedAtField = field("visited_at", OffsetDateTime.class);
	private Asterisk star = asterisk();

	@SneakyThrows
	@Override
	public void recordVisit(Store store, Customer customer, OffsetDateTime time) {
		try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tinfoil_db", "justin", "hat")) {
			DSL.using(conn, SQLDialect.POSTGRES)
					.insertInto(customerTable)
					.columns(nameField)
					.values(customer.getName())
					.onConflictDoNothing()
					.execute();
		}

		try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tinfoil_db", "justin", "hat")) {
			DSL.using(conn, SQLDialect.POSTGRES)
					.insertInto(storeTable)
					.columns(nameField)
					.values(store.getName())
					.onConflictDoNothing()
					.execute();
		}

		try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tinfoil_db", "justin", "hat")) {
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
		try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tinfoil_db", "justin", "hat")) {

			var rows = DSL.using(conn, SQLDialect.POSTGRES)
					.select(star)
					.from(visitTable)
					.where(customerField.equal(customer.getName()))
					.fetch();

			for (var visit :  rows) {
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

	public static void main(String[] args) {
		Store s = new Store("Albert Heijn");
		Customer c = new Customer("Justin");
		var repo = new PostgresCustomerRepository();
		repo.recordVisit(s, c, OffsetDateTime.now());
		System.out.println(repo.getAllVisits(c));

	}
}

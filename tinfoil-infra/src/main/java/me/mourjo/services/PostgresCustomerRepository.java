package me.mourjo.services;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
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
	private Field<Object> nameField = field("name");
	private Field<Object> customerField = field("customer");
	private Field<Object> storeField = field("store");
	private Field<Object> visitedAtField = field("visited_at");

	@SneakyThrows
	@Override
	public void recordVisit(Store store, Customer customer, ZonedDateTime time) {
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
					.values(customer.getName(), store.getName(), time.toInstant())
					.execute();
		}

	}

	@Override
	public Map<Store, List<ZonedDateTime>> getAllVisits(Customer customer) {
		return null;
	}

	@Override
	public List<ZonedDateTime> getStoreVisits(Store store, Customer customer) {
		return null;
	}

	@Override
	public void delete(Customer customer) {

	}

	public static void main(String[] args) {
		Store s = new Store("Albert Heijn");
		Customer c = new Customer("Justin");
		new PostgresCustomerRepository().recordVisit(s, c, ZonedDateTime.now());
	}
}

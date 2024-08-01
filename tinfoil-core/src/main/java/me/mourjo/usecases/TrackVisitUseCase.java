package me.mourjo.usecases;

import java.time.Clock;
import java.time.OffsetDateTime;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import me.mourjo.services.VisitRepository;

public class TrackVisitUseCase {

	private final VisitRepository repository;
	private final Clock clock;

	public TrackVisitUseCase(VisitRepository repository) {
		this.repository = repository;
		clock = Clock.systemDefaultZone();
	}

	public TrackVisitUseCase(VisitRepository repository, Clock clock) {
		this.repository = repository;
		this.clock = clock;
	}

	public void visit(Store store, Customer customer) {
		repository.recordVisit(store, customer, OffsetDateTime.now(clock));
	}
}

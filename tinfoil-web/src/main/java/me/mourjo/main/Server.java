package me.mourjo.main;

import me.mourjo.services.PostgresVisitRepository;
import me.mourjo.services.VisitRepository;
import me.mourjo.web.App;
import me.mourjo.web.controller.VisitController;

public class Server {

	public static void main(String[] args) {

		VisitRepository repository = new PostgresVisitRepository(
				"localhost",
				"5432",
				"tinfoil_db",
				"justin",
				"hat"
		);

		VisitController controller = new VisitController(repository);

		new App(controller).start(7002);
	}
}

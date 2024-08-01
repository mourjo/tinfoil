package me.mourjo.web;

import static me.mourjo.utils.datetime.DatetimeConverter.utcOffsetDateTime;

import com.fasterxml.jackson.core.type.TypeReference;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.testtools.JavalinTest;
import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import me.mourjo.services.PostgresVisitRepository;
import me.mourjo.usecases.TrackVisitUseCase;
import me.mourjo.usecases.ViewVisitsUseCase;
import me.mourjo.utils.ModifiableClock;
import me.mourjo.web.controller.VisitController;
import me.mourjo.web.dto.ViewVisitResponse;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VisitIntegrationTest {

	final OffsetDateTime fixedTime = utcOffsetDateTime(2024, 7, 31, 1, 2, 28);
	final ModifiableClock clock = ModifiableClock.fixed(fixedTime);
	final JavalinJackson jackson = new JavalinJackson();
	PostgresVisitRepository repo = new PostgresVisitRepository("justin", "hat",
			"tinfoil_test_db");
	final ViewVisitsUseCase viewVisitsUseCase = new ViewVisitsUseCase(repo, clock);
	final TrackVisitUseCase trackVisitUseCase = new TrackVisitUseCase(repo, clock);
	final VisitController visitController = new VisitController(viewVisitsUseCase,
			trackVisitUseCase);
	final Javalin app = new App(visitController).getApp();

	@BeforeEach
	void setup() {
		repo.truncate();
	}

	@Test
	void relativeTimeResponses() {
		JavalinTest.test(app, (server, client) -> {
			Assertions.assertEquals(200, client.get("/visits/all/msen").code());
			Assertions.assertEquals(201, client.post("/visit/albert-heijn/msen").code());
			Assertions.assertEquals(200, client.get("/visits/all/msen").code());

			Assertions.assertEquals(
					List.of(new ViewVisitResponse("albert-heijn", List.of("Right now"))),
					toViewVisitResponse(client.get("/visits/all/msen")));

			clock.tickForward(Duration.ofHours(1));

			Assertions.assertEquals(
					List.of(new ViewVisitResponse("albert-heijn", List.of("1 hour ago"))),
					toViewVisitResponse(client.get("/visits/all/msen")));
		});
	}

	@Test
	void multiStoreResponses() {
		JavalinTest.test(app, (server, client) -> {

			client.post("/visit/albert-heijn/msen");
			clock.tickForward(Duration.ofMinutes(48));
			client.post("/visit/jumbo/msen");

			Assertions.assertEquals(
					List.of(
							new ViewVisitResponse("jumbo", List.of("Right now")),
							new ViewVisitResponse("albert-heijn", List.of("48 minutes ago"))
					),
					toViewVisitResponse(client.get("/visits/all/msen")));
		});
	}

	private List<ViewVisitResponse> toViewVisitResponse(Response s) throws IOException {
		var typeRef = new TypeReference<List<ViewVisitResponse>>() {
		};
		return jackson.fromJsonString(s.body().string(), typeRef.getType());
	}
}

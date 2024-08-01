package me.mourjo.web.controller;

import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;
import me.mourjo.entities.Customer;
import me.mourjo.entities.Store;
import me.mourjo.services.VisitRepository;
import me.mourjo.usecases.TrackVisitUseCase;
import me.mourjo.usecases.ViewVisitsUseCase;
import me.mourjo.web.dto.ErrorResponse;
import me.mourjo.web.dto.ViewVisitResponse;

public class VisitController {

	private final ViewVisitsUseCase viewVisitsUseCase;
	private final TrackVisitUseCase trackVisitUseCase;

	public VisitController(VisitRepository repository) {
		viewVisitsUseCase = new ViewVisitsUseCase(repository);
		trackVisitUseCase = new TrackVisitUseCase(repository);
	}

	public VisitController(ViewVisitsUseCase viewVisitsUseCase,
			TrackVisitUseCase trackVisitUseCase) {
		this.viewVisitsUseCase = viewVisitsUseCase;
		this.trackVisitUseCase = trackVisitUseCase;
	}

	@OpenApi(
			summary = "View visits of a customer to all stores",
			operationId = "viewVisits",
			path = "/visits/all/{customerName}",
			pathParams = {
					@OpenApiParam(name = "customerName", type = String.class, description = "Name of customer")
			},
			methods = HttpMethod.GET,
			responses = {
					@OpenApiResponse(status = "200"),
					@OpenApiResponse(status = "400", content = {
							@OpenApiContent(from = ErrorResponse.class)})
			}
	)
	public void viewVisits(Context ctx) {
		String customerName = ctx.pathParam("customerName");
		var data = viewVisitsUseCase.viewVisits(new Customer(customerName));

		ctx.json(ViewVisitResponse.from(data));
		ctx.status(200);
	}

	@OpenApi(
			summary = "Track a customer's visit to a store",
			operationId = "viewVisits",
			path = "/visit/{storeName}/{customerName}",
			pathParams = {
					@OpenApiParam(name = "storeName", type = String.class, description = "Name of store"),
					@OpenApiParam(name = "customerName", type = String.class, description = "Name of customer")
			},
			methods = HttpMethod.POST,
			responses = {
					@OpenApiResponse(status = "200"),
					@OpenApiResponse(status = "400", content = {
							@OpenApiContent(from = ErrorResponse.class)})
			}
	)
	public void trackVisit(Context ctx) {
		String customerName = ctx.pathParam("customerName");
		String storeName = ctx.pathParam("storeName");

		trackVisitUseCase.visit(new Store(storeName), new Customer(customerName));
		ctx.status(201);
	}
}

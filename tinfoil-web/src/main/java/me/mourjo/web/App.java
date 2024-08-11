package me.mourjo.web;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import me.mourjo.web.controller.VisitController;

public class App {

	private final Javalin app;

	public App(VisitController controller) {
		this.app = Javalin.create(config -> {
			config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
				pluginConfig.withDefinitionConfiguration((version, definition) -> {
					definition.withInfo(info -> info.setTitle("Tinfoil"));
				}).withDocumentationPath("/mydocs");
			}));

			config.registerPlugin(new SwaggerPlugin(pluginConfig -> {
				pluginConfig.setUiPath("/swagger-ui");
				pluginConfig.setDocumentationPath("/mydocs");

			}));

			config.router.apiBuilder(() -> {
				ApiBuilder.path("visits/all/{customerName}", () -> {
					ApiBuilder.get(controller::viewVisits);
				});

				ApiBuilder.path("visit/{storeName}/{customerName}", () -> {
					ApiBuilder.post(controller::trackVisit);
				});
			});
		});
	}

	public void start(int port) {
		app.start(port);
	}

	Javalin getApp() {
		return app;
	}
}

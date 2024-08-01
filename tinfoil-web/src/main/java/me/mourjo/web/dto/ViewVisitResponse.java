package me.mourjo.web.dto;

import java.util.List;
import java.util.Map;
import me.mourjo.entities.Store;

public record ViewVisitResponse(String storeName, List<String> visits) {

	public static List<ViewVisitResponse> from(Map<Store, List<String>> data) {
		return data.entrySet().stream()
				.map(entry -> new ViewVisitResponse(entry.getKey().getName(), entry.getValue()))
				.toList();
	}
}

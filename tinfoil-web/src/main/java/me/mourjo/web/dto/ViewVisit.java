package me.mourjo.web.dto;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import me.mourjo.entities.Store;

public record ViewVisit(String storeName, List<String> visits) {

	public static List<ViewVisit> from(Map<Store, List<String>> data) {
		return data.entrySet().stream()
				.sorted(Comparator.comparing(o -> o.getKey().getName()))
				.map(entry -> new ViewVisit(entry.getKey().getName(), entry.getValue()))
				.toList();
	}
}

package me.mourjo.web.dto;


import java.util.List;
import java.util.Map;
import me.mourjo.entities.Store;

public record ViewVisitResponse(List<ViewVisit> data) {

	public static ViewVisitResponse from(Map<Store, List<String>> data) {
		return new ViewVisitResponse(ViewVisit.from(data));
	}
}

package com.wordpress.ciusthedracohenas.googlesheet;

import java.util.List;

import com.google.api.services.sheets.v4.model.Request;

public interface BatchUpdateService {
	public List<Request> addRequest(List<Request> requests, int sheetId);
}

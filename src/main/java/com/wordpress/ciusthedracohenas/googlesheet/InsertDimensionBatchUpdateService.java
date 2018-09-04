package com.wordpress.ciusthedracohenas.googlesheet;

import java.util.List;

import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.InsertDimensionRequest;
import com.google.api.services.sheets.v4.model.Request;

public class InsertDimensionBatchUpdateService implements BatchUpdateService {

	private String dimension = "ROWS";
	private int start = 0;
	private int end = 0;
	
	public InsertDimensionBatchUpdateService(String dimension, int start, int end) {
		this.start = start;
		this.end = end;
		this.dimension = dimension;
	}
	
	public InsertDimensionBatchUpdateService() {}
	
	public List<Request> addRequest(List<Request> requests, int sheetId) {
		requests.add(new Request()
				.setInsertDimension(new InsertDimensionRequest()
        				.setRange(new DimensionRange()
        						.setDimension(dimension)
        						.setSheetId(sheetId)
        						.setStartIndex(start)
        						.setEndIndex(end))
        				.setInheritFromBefore(false)));
		return requests;
	}
	
	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

}

package com.wordpress.ciusthedracohenas.googlesheet;

import java.util.List;

import com.google.api.services.sheets.v4.model.CopyPasteRequest;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;

public class CopyPasteBatchUpdateService implements BatchUpdateService {
	private String dimension = "ROWS";
	private int startSourceColumn = 0;
	private int startSourceRow = 0;
	private int sourceCount = 0;
	private int startDestinationColumn = 0;
	private int startDestinationRow = 0;
	private int count = 0;
	
	public CopyPasteBatchUpdateService(String dimension, int startSourceColumn, int startSourceRow, int sourceCount, int startDestinationColumn, int startDestinationRow, int count) {
		this.dimension = dimension;
		this.startSourceColumn = startSourceColumn;
		this.startSourceRow = startSourceRow;
		this.sourceCount = sourceCount;
		this.startDestinationColumn = startDestinationColumn;
		this.startDestinationRow = startDestinationRow;
		this.count = count;
	}

	public List<Request> addRequest(List<Request> requests, int sheetId) {
		int endSourceColumn = dimension.equals("ROWS") ? startSourceColumn + 1 + sourceCount : startSourceColumn + 1;
		int endSourceRow = dimension.equals("ROWS") ? startSourceRow + 1 : startSourceRow + 1 + sourceCount;
		
		int endDestinationColumn = dimension.equals("ROWS") ? startDestinationColumn + 1 + sourceCount : startDestinationColumn + count;
		int endDestinationRow = dimension.equals("ROWS") ? startDestinationRow + count : startDestinationRow + 1 + sourceCount;
 		
		requests.add(new Request()
				.setCopyPaste(new CopyPasteRequest()
						.setSource(new GridRange()
								.setSheetId(sheetId)
								.setStartColumnIndex(startSourceColumn)
								.setStartRowIndex(startSourceRow)
								.setEndColumnIndex(endSourceColumn)
								.setEndRowIndex(endSourceRow))
						.setDestination(new GridRange()
								.setSheetId(sheetId)
								.setStartColumnIndex(startDestinationColumn)
								.setStartRowIndex(startDestinationRow)
								.setEndColumnIndex(endDestinationColumn)
								.setEndRowIndex(endDestinationRow))
						.setPasteType("PASTE_NORMAL")
						.setPasteOrientation("NORMAL")));
//		requests.add(new Request()
//				.setCopyPaste(new CopyPasteRequest()
//						.setSource(new GridRange()
//								.setSheetId(newSheetId)
//								.setStartColumnIndex(6)
//								.setStartRowIndex(1)
//								.setEndColumnIndex(7)
//								.setEndRowIndex(8))
//						.setDestination(new GridRange()
//								.setSheetId(newSheetId)
//								.setStartColumnIndex(7)
//								.setStartRowIndex(1)
//								.setEndColumnIndex(6 + menu.getSuspects().size())
//								.setEndRowIndex(8))
//						.setPasteType("PASTE_NORMAL")
//						.setPasteOrientation("NORMAL")));
		return requests;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public int getStartSourceColumn() {
		return startSourceColumn;
	}

	public void setStartSourceColumn(int startSourceColumn) {
		this.startSourceColumn = startSourceColumn;
	}

	public int getStartSourceRow() {
		return startSourceRow;
	}

	public void setStartSourceRow(int startSourceRow) {
		this.startSourceRow = startSourceRow;
	}

	public int getSourceCount() {
		return sourceCount;
	}

	public void setSourceCount(int sourceCount) {
		this.sourceCount = sourceCount;
	}

	public int getStartDestinationColumn() {
		return startDestinationColumn;
	}

	public void setStartDestinationColumn(int startDestinationColumn) {
		this.startDestinationColumn = startDestinationColumn;
	}

	public int getStartDestinationRow() {
		return startDestinationRow;
	}

	public void setStartDestinationRow(int startDestinationRow) {
		this.startDestinationRow = startDestinationRow;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}

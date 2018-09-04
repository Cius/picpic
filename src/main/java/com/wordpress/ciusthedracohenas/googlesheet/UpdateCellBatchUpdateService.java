package com.wordpress.ciusthedracohenas.googlesheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;

public class UpdateCellBatchUpdateService implements BatchUpdateService {
	private List<List<ExtendedValue>> datas;
	private int startRow = 0;
	private int startColumn = 0;
	
	public UpdateCellBatchUpdateService(List<List<ExtendedValue>> datas, int startRow, int startColumn) {
		this.datas = datas;
		this.startColumn = startColumn;
		this.startRow = startRow;
	}

	public List<Request> addRequest(List<Request> requests, int sheetId) {
		List<RowData> rowDatas = new ArrayList<RowData>();
		int rowCount = 0;
		int columnCount = 0;
		rowCount = datas.size();
		for(Iterator<List<ExtendedValue>> iterator = datas.iterator(); iterator.hasNext();) {
			List<CellData> data = new ArrayList<CellData>();
			for(Iterator<ExtendedValue> iterator2 = iterator.next().iterator(); iterator2.hasNext();) {
				data.add(new CellData().setUserEnteredValue(iterator2.next()));
			}
			columnCount = data.size();
			rowDatas.add(new RowData().setValues(data));
		}
		
		requests.add(new Request()
				.setUpdateCells(new UpdateCellsRequest()
						.setRows(rowDatas)
						.setRange(new GridRange()
								.setSheetId(sheetId)
								.setStartColumnIndex(startColumn)
								.setStartRowIndex(startRow)
								.setEndColumnIndex(startColumn + columnCount)
								.setEndRowIndex(startRow + rowCount))
						.setFields("UserEnteredValue")));
//		requests.add(new Request()
//				.setUpdateCells(new UpdateCellsRequest()
//						.setRows(rowDatas)
//						.setRange(new GridRange()
//								.setSheetId(newSheetId)
//								.setStartColumnIndex(6)
//								.setStartRowIndex(1)
//								.setEndColumnIndex(7 + menu.getSuspects().size())
//								.setEndRowIndex(2))
//						.setFields("UserEnteredValue")));
		return requests;
	}

	public List<List<ExtendedValue>> getDatas() {
		return datas;
	}

	public void setDatas(List<List<ExtendedValue>> datas) {
		this.datas = datas;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

}

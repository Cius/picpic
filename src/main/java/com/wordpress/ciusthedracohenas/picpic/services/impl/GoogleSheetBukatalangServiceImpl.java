package com.wordpress.ciusthedracohenas.picpic.services.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Get;
import com.google.api.services.sheets.v4.model.AddNamedRangeRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.DuplicateSheetRequest;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.NamedRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.UpdateSheetPropertiesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.wordpress.ciusthedracohenas.googlesheet.BatchUpdateService;
import com.wordpress.ciusthedracohenas.googlesheet.CopyPasteBatchUpdateService;
import com.wordpress.ciusthedracohenas.googlesheet.InsertDimensionBatchUpdateService;
import com.wordpress.ciusthedracohenas.googlesheet.UpdateCellBatchUpdateService;
import com.wordpress.ciusthedracohenas.picpic.Constant;
import com.wordpress.ciusthedracohenas.picpic.models.Menu;
import com.wordpress.ciusthedracohenas.picpic.models.MenuItem;
import com.wordpress.ciusthedracohenas.picpic.services.BukatalangService;
import com.wordpress.ciusthedracohenas.picpic.services.GoogleAuthService;

public class GoogleSheetBukatalangServiceImpl implements BukatalangService {
	final static Logger logger = Logger.getLogger(GoogleSheetBukatalangServiceImpl.class);
	
	private List<BatchUpdateService> batchUpdateServices = new ArrayList<BatchUpdateService>();
    private List<BatchUpdateService> batchUpdateServicesTracker = new ArrayList<BatchUpdateService>();

	public void submit(Menu menu) {
		List<Request> requests = new ArrayList<Request>();
        requests.add(new Request()
    		.setDuplicateSheet(new DuplicateSheetRequest()
        		.setSourceSheetId(Constant.SHEET_TEMPLATE_ID)
        		.setInsertSheetIndex(Constant.SHEET_TEMPLATE_POSITION + 1)
        		.setNewSheetName(menu.getMenuName())));
        
        BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        BatchUpdateSpreadsheetResponse batchResponse;
		try {
			batchResponse = getSheetService().spreadsheets().batchUpdate(Constant.SPREADSHEET_ID, batchRequest).execute();
	        requests.clear();
	        List<Request> trackerRequests = new ArrayList<Request>();
	        
	        int newSheetId = batchResponse.getReplies().get(0).getDuplicateSheet().getProperties().getSheetId();
			int additionalRow = menu.getMenuItems().size() > Constant.SHEET_TEMPLATE_NUMBER_OF_DISHES ? menu.getMenuItems().size() - Constant.SHEET_TEMPLATE_NUMBER_OF_DISHES : 0;
	        int startRow = Constant.SHEET_TEMPLATE_NUMBER_OF_DISHES + Constant.SHEET_TEMPLATE_NUMBER_OF_HEADER_ROWS;

	        requests = setSheetProperties(requests, newSheetId);
        	addSuspects(menu);
	        if(additionalRow > 0) {
	        	addRows(startRow, additionalRow);
	        	copyFormula(menu, startRow, additionalRow);
	        }
	        insertMenu(menu, startRow, additionalRow);
	        requests = addNamedRanges(requests, menu, newSheetId, startRow, additionalRow);
	        
        	for (Iterator<BatchUpdateService> iterator = batchUpdateServices.iterator(); iterator.hasNext();) {
				BatchUpdateService service = iterator.next();
				requests = service.addRequest(requests, newSheetId);
			}

        	addTrackerRow(menu, startRow, additionalRow);
        	List<Request> newTrackerRequests = new ArrayList<Request>();
        	for (Iterator<BatchUpdateService> iterator = batchUpdateServicesTracker.iterator(); iterator.hasNext();) {
				BatchUpdateService service = iterator.next();
				newTrackerRequests = service.addRequest(trackerRequests, Constant.SHEET_TRACKER_ID);
			}
        	requests.addAll(newTrackerRequests);
	        batchRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);
	        batchResponse = getSheetService().spreadsheets().batchUpdate(Constant.SPREADSHEET_ID, batchRequest).execute();	        
		} catch (IOException e) {
			logger.info(e.getMessage());
		} finally {
			batchUpdateServices.clear();
			batchUpdateServicesTracker.clear();
		}
	}
	
	private List<Request> setSheetProperties(List<Request> requests, int newSheetId) {
		requests.add(new Request()
				.setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
						.setProperties(new SheetProperties()
								.setSheetId(newSheetId)
								.setHidden(false)
								.setTabColor(null))
						.setFields("Hidden,TabColor")));
		return requests;
	}
	
	private void addSuspects(Menu menu) {
		batchUpdateServices.add(new InsertDimensionBatchUpdateService("COLUMNS", 7, 6 + menu.getSuspects().size()));
		batchUpdateServices.add(new CopyPasteBatchUpdateService("COLUMNS", 6, 1, 6, 7, 1, menu.getSuspects().size()));

		List<List<ExtendedValue>> datas = new ArrayList<List<ExtendedValue>>();
		List<ExtendedValue> data = new ArrayList<ExtendedValue>();
		for (Iterator<String> iterator = menu.getSuspects().iterator(); iterator.hasNext();) {
			data.add(new ExtendedValue().setStringValue(iterator.next()));
		}
		datas.add(data);
		batchUpdateServices.add(new UpdateCellBatchUpdateService(datas, 1, 6));
	}
	
	private void addRows(int startRow, int count) {
        batchUpdateServices.add(new InsertDimensionBatchUpdateService("ROWS", startRow, startRow + count));
	}
	
	private void copyFormula(Menu menu, int startRow, int count) {
        batchUpdateServices.add(new CopyPasteBatchUpdateService("ROWS", 2, startRow - 1, 2, 2, startRow, count));
        batchUpdateServices.add(new CopyPasteBatchUpdateService("ROWS", 8 + menu.getSuspects().size(), startRow - 1, 3, 8 + menu.getSuspects().size(), startRow, count));
	}
	
	private void insertMenu(Menu menu, int startRow, int count) {
        int num = 1;
        List<List<ExtendedValue>> datas = new ArrayList<List<ExtendedValue>>();
        List<List<ExtendedValue>> datas2 = new ArrayList<List<ExtendedValue>>();
        for(Iterator<MenuItem> iterator = menu.getMenuItems().iterator(); iterator.hasNext();) {
        	MenuItem menuItem = iterator.next();
            List<ExtendedValue> data = new ArrayList<ExtendedValue>();
        	data.add(new ExtendedValue().setNumberValue(num + 0.0));
        	data.add(new ExtendedValue().setStringValue(menuItem.getName()));
        	data.add(new ExtendedValue().setNumberValue(menuItem.getPricePerItem() * menuItem.getCount()));
        	data.add(new ExtendedValue().setNumberValue(menuItem.getCount() * 1.0));
        	datas.add(data);

            List<ExtendedValue> data2 = new ArrayList<ExtendedValue>();
        	data2.add(new ExtendedValue().setNumberValue(menuItem.isShareable() ? 1.0 : 0.0));
        	datas2.add(data2);
        	num++;
        }
        batchUpdateServices.add(new UpdateCellBatchUpdateService(datas, 2, 0));
        batchUpdateServices.add(new UpdateCellBatchUpdateService(datas2, 2, 9 + menu.getSuspects().size()));
	}
	
	private List<Request> addNamedRanges(List<Request> requests, Menu menu, int newSheetId, int startRow, int count) {
		requests.add(new Request()
				.setAddNamedRange(new AddNamedRangeRequest()
						.setNamedRange(new NamedRange()
								.setName(menu.getMenuName() + "_table")
								.setRange(new GridRange()
										.setSheetId(newSheetId)
										.setStartColumnIndex(6)
										.setStartRowIndex(1)
										.setEndColumnIndex(7 + menu.getSuspects().size())
										.setEndRowIndex(startRow + count + 5)))));
		return requests;
	}
	
	private void addTrackerRow(Menu menu, int startRow, int count) {
		try {
			Get getRequest = getSheetService().spreadsheets().values().get(Constant.SPREADSHEET_ID, Constant.SHEET_TRACKER_LAST_ROW);
			ValueRange valueRange = getRequest.execute();
			
			String lastRowS = valueRange.getValues().get(0).get(0).toString();
			int lastRow = Integer.parseInt(lastRowS);
			batchUpdateServicesTracker.add(new InsertDimensionBatchUpdateService("ROWS", lastRow - 1, lastRow));
			batchUpdateServicesTracker.add(new CopyPasteBatchUpdateService("ROWS", 0, lastRow - 2, 5 + Constant.NUMBER_OF_PEOPLE, 0, lastRow - 1, 1));

			List<List<ExtendedValue>> datas = new ArrayList<List<ExtendedValue>>();
			List<ExtendedValue> data = new ArrayList<ExtendedValue>();
			
			data.add(new ExtendedValue().setStringValue(menu.getMenuName() + "_table"));
			data.add(new ExtendedValue().setStringValue("-"));
			data.add(new ExtendedValue().setNumberValue(startRow + count + 4.0));
			datas.add(data);
			batchUpdateServicesTracker.add(new UpdateCellBatchUpdateService(datas, lastRow - 1,  2 + Constant.NUMBER_OF_PEOPLE));
			
			List<List<ExtendedValue>> datas2 = new ArrayList<List<ExtendedValue>>();
			List<ExtendedValue> data2 = new ArrayList<ExtendedValue>();
			data2.add(new ExtendedValue().setStringValue(menu.getDonor()));
			datas2.add(data2);
			batchUpdateServicesTracker.add(new UpdateCellBatchUpdateService(datas2, lastRow - 1, 1));

			List<List<ExtendedValue>> datas3 = new ArrayList<List<ExtendedValue>>();
			List<ExtendedValue> data3 = new ArrayList<ExtendedValue>();
			data3.add(new ExtendedValue().setNumberValue(lastRow + 1.0));
			datas3.add(data3);
			batchUpdateServicesTracker.add(new UpdateCellBatchUpdateService(datas3, Constant.SHEET_TRACKER_LAST_ROW_ROW - 1, Constant.SHEET_TRACKER_LAST_ROW_COLUMN - 1));
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	private Sheets getSheetService() {
		GoogleAuthService service = new GoogleAuthServiceImpl();
		try {
			return service.getSheet();
		} catch (GeneralSecurityException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return null;
	}
}

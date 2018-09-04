package com.wordpress.ciusthedracohenas.picpic.services.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Get;
import com.google.api.services.sheets.v4.model.GridData;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.wordpress.ciusthedracohenas.picpic.Constant;
import com.wordpress.ciusthedracohenas.picpic.exception.PeopleNotFoundException;
import com.wordpress.ciusthedracohenas.picpic.models.Debt;
import com.wordpress.ciusthedracohenas.picpic.models.DebtDetail;
import com.wordpress.ciusthedracohenas.picpic.models.DebtItem;
import com.wordpress.ciusthedracohenas.picpic.services.GoogleAuthService;
import com.wordpress.ciusthedracohenas.picpic.services.PeopleService;

public class GoogleSheetPeopleServiceImpl implements PeopleService {
	final static Logger logger = Logger.getLogger(GoogleSheetPeopleServiceImpl.class);

	public List<String> getPeople() {
		List<String> peoples = new ArrayList<String>();
		try {
			Get getRequest = getSheetService().spreadsheets().values().get(Constant.SPREADSHEET_ID, Constant.SHEET_DATA_PEOPLE);
			ValueRange valueRange = getRequest.execute();
			
			for(Iterator<List<Object>> iterator = valueRange.getValues().iterator(); iterator.hasNext();) {
				String name = iterator.next().get(0).toString();
				peoples.add(name);
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		
		return peoples;
	}

	public void updatePeople() {
		// TODO Auto-generated method stub

	}
	
	public Debt getDebt(String username) throws PeopleNotFoundException {
		DecimalFormat decimalFormat = new DecimalFormat("0,000.00");
		decimalFormat.setMaximumFractionDigits(2);
		Debt debt = new Debt();
		List<DebtDetail> debtDetails = new ArrayList<DebtDetail>();
		Map<String, DebtDetail> debtDetailsMap = new HashMap<>();
		try {
			com.google.api.services.sheets.v4.Sheets.Spreadsheets.Get getRequest = getSheetService().spreadsheets().get(Constant.SPREADSHEET_ID);
			getRequest.setIncludeGridData(true);
			List<String> range = new ArrayList<String>();
			range.add(Constant.SHEET_DATA_PEOPLE_DETAIL);
			range.add(Constant.SHEET_DATA_TRACKER_DETAIL);
			getRequest.setRanges(range);
			Spreadsheet spreadsheet = getRequest.execute();
			
			Sheet dataSheet = spreadsheet.getSheets().get(0);
			Sheet trackerSheet = spreadsheet.getSheets().get(1);
			int peopleSize = 0;
			int columnNum = 0;
			for(Iterator<GridData> iterator = dataSheet.getData().iterator(); iterator.hasNext();) {
				GridData gridData = iterator.next();
				peopleSize = gridData.getRowData().size();
				boolean found = false;
				for(Iterator<RowData> iterator2 = gridData.getRowData().iterator(); iterator2.hasNext();) {
					RowData rowData = iterator2.next();
					String usernameValue = rowData.getValues().get(1).getFormattedValue();
					if(usernameValue != null && usernameValue.equalsIgnoreCase(username)) {
						String name = rowData.getValues().get(0).getFormattedValue();
						String columnNumS = rowData.getValues().get(2).getFormattedValue();
						columnNum = Integer.valueOf(columnNumS);
						
						debt.setDebtor(name);
						found = true;
						break;
					}
				}
				if(found) {
					break;
				}
			}
			
			if(columnNum == 0) {
				throw new PeopleNotFoundException();
			}
			
			GridData gridData = trackerSheet.getData().get(0);
			for(int i = 7; i < 7 + peopleSize; i++) {
				RowData rowData = gridData.getRowData().get(i);
				String amountS = rowData.getValues().get(1 + columnNum).getFormattedValue();
				
				if(amountS != null) {
					double amount = decimalFormat.parse(amountS).doubleValue();
					if(amount != 0.0) {
						String donor = rowData.getValues().get(1).getFormattedValue();
						DebtDetail debtDetail = new DebtDetail();
						debtDetail.setAmount(amount);
						debtDetail.setDonor(donor);
						debtDetailsMap.put(donor, debtDetail);
					}
				}
			}
			
			RowData rowData = gridData.getRowData().get(6 + columnNum);
			for(int i = 2; i <= 2 + peopleSize; i++) {
				String amountS = rowData.getValues().get(i).getFormattedValue();
				if(amountS != null) {
					double amount = decimalFormat.parse(amountS).doubleValue();
					
					if(amount != 0.0) {
						String donor = gridData.getRowData().get(6).getValues().get(i).getFormattedValue();
						
						DebtDetail debtDetail = debtDetailsMap.get(donor);
						if(debtDetail == null) {
							debtDetail = new DebtDetail();
						}
						
						amount = debtDetail.getAmount() == 0.0 ? amount * -1.0 : debtDetail.getAmount() - amount;
						amount = Math.round(amount * 100.00) / 100.00;
						debtDetail.setAmount(amount);
						debtDetail.setDonor(donor);
						debtDetailsMap.put(donor, debtDetail);
					}
				}
			}
			
			String lastRowS = gridData.getRowData().get(3).getValues().get(1).getFormattedValue();
			int lastRow = Integer.valueOf(lastRowS);
			for(DebtDetail d: debtDetailsMap.values()) {
				d.setDebtItems(new ArrayList<DebtItem>());
				debtDetails.add(d);
				for(int i = 7 + peopleSize + 5; i < lastRow - 1; i++) {
					DebtItem debtItem = new DebtItem();
					RowData rowData2 = gridData.getRowData().get(i);
					
					String donor = rowData2.getValues().get(1).getFormattedValue();
					if(d.getDonor().equalsIgnoreCase(donor)) {
						String amountS = rowData2.getValues().get(1 + columnNum).getFormattedValue();
						if(amountS == null || amountS.equalsIgnoreCase("-") || amountS.equalsIgnoreCase("o")) {
							continue;
						}else if(amountS.equalsIgnoreCase("x")) {
							amountS = rowData2.getValues().get(4 + peopleSize).getFormattedValue();
						}
						
						double amount = decimalFormat.parse(amountS).doubleValue();
						if(amount != 0.0) {
							debtItem.setName(rowData2.getValues().get(2 + peopleSize).getFormattedValue());
							debtItem.setAmount(amount);
							d.getDebtItems().add(debtItem);
						}
					}
				}
			}
			
			debt.setDebtDetails(debtDetails);
		}catch(PeopleNotFoundException e) {
			throw e;
		}catch(Exception e) {
			logger.info(e.getMessage());
		}
		
		return debt;
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

package com.wordpress.ciusthedracohenas.picpic.models;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Debt {
	private String debtor;
	private List<DebtDetail> debtDetails;
	
	public String getDebtor() {
		return debtor;
	}
	public void setDebtor(String debtor) {
		this.debtor = debtor;
	}
	public List<DebtDetail> getDebtDetails() {
		return debtDetails;
	}
	public void setDebtDetails(List<DebtDetail> debtDetails) {
		this.debtDetails = debtDetails;
	}
	
	public String toString() {
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(new Locale("in", "ID"));
		DecimalFormat decimalFormat = new DecimalFormat("\u00A40,000.##", decimalFormatSymbols);
		decimalFormat.setMinimumFractionDigits(2);
		decimalFormat.setMaximumFractionDigits(2);
		String s = "Halo " + debtor + ",\n";
		for(DebtDetail debtDetail: debtDetails) {
			if(debtDetail.getAmount() > 0.0) {
				s += "kamu berhutang ke " + debtDetail.getDonor() + " sebanyak " + decimalFormat.format(debtDetail.getAmount()) + " karena ";
				for(DebtItem debtItem: debtDetail.getDebtItems()) {
					s += debtItem.getName() + " sebesar " + decimalFormat.format(debtItem.getAmount()) + ", ";
				}
				s = s.substring(0, s.length() - 2);
			}else {
				s += debtDetail.getDonor() + " berhutang ke kamu sebanyak " + decimalFormat.format(debtDetail.getAmount() * -1.0);
			}
			s += ";\n";
		}
		
		return s;
	}
}

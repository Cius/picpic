package com.wordpress.ciusthedracohenas.picpic.models;

import java.util.List;

public class DebtDetail {
	private String donor;
	private double amount = 0.0;
	private List<DebtItem> debtItems;
	
	public String getDonor() {
		return donor;
	}
	public void setDonor(String donor) {
		this.donor = donor;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public List<DebtItem> getDebtItems() {
		return debtItems;
	}
	public void setDebtItems(List<DebtItem> debtItems) {
		this.debtItems = debtItems;
	}
}

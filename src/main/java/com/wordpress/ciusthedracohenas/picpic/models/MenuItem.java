package com.wordpress.ciusthedracohenas.picpic.models;

public class MenuItem {

	private String name;
	private int count;
	private double pricePerItem;
	private boolean shareable;
	
	public MenuItem(String name, int count, double price, boolean shareable) {
		this.name = name;
		this.count = count;
		this.pricePerItem = price;
		this.shareable = shareable;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getPricePerItem() {
		return pricePerItem;
	}
	public void setPricePerItem(double pricePerItem) {
		this.pricePerItem = pricePerItem;
	}

	public boolean isShareable() {
		return shareable;
	}

	public void setShareable(boolean shareable) {
		this.shareable = shareable;
	}
}

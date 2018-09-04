package com.wordpress.ciusthedracohenas.picpic.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Menu {
	private List<MenuItem> menuItems;
	private String placeName;
	private Date date;
	private String donor;
	private List<String> suspects;
	
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}
	
	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getMenuName() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return getPlaceName() + "_" + dateFormat.format(getDate());
	}

	public String getDonor() {
		return donor;
	}

	public void setDonor(String donor) {
		this.donor = donor;
	}

	public List<String> getSuspects() {
		return suspects;
	}

	public void setSuspects(List<String> suspects) {
		this.suspects = suspects;
	}
}

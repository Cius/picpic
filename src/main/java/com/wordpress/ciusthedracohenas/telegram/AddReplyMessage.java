package com.wordpress.ciusthedracohenas.telegram;

import com.wordpress.ciusthedracohenas.picpic.models.Menu;
import com.wordpress.ciusthedracohenas.telegram.BukatalangBot.Status;

public class AddReplyMessage extends MenuReplyMessage {	
	public String getText() {
		return "Abis dari mana?";
	}
	
	public Menu operate(Menu menu, String message) {
		return menu;
	}
	
	public Status nextStatus() {
		return Status.place;
	}
}

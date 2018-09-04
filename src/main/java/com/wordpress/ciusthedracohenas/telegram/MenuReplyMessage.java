package com.wordpress.ciusthedracohenas.telegram;

import com.wordpress.ciusthedracohenas.picpic.models.Menu;
import com.wordpress.ciusthedracohenas.telegram.BukatalangBot.Status;

public class MenuReplyMessage extends ReplyMessage {	
	public String getText() {
		return "Hai";
	}
	
	public Menu operate(Menu menu, String message) {
		return menu;
	}
	
	public Status nextStatus() {
		return Status.place;
	}
}

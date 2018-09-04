package com.wordpress.ciusthedracohenas.telegram;

import com.google.common.base.CaseFormat;
import com.wordpress.ciusthedracohenas.picpic.models.Menu;
import com.wordpress.ciusthedracohenas.telegram.BukatalangBot.Status;

public class PlaceReplyMessage extends MenuReplyMessage {
	@Override
	public String getText() {
		return "Kapan tuh? (pakai 'yyyyMMdd' ya)";
	}
	
	@Override
	public Menu operate(Menu menu, String message) {
		String placeName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, message.replaceAll(" ", ""));
		menu.setPlaceName(placeName);
		return menu;
	}
	
	@Override
	public Status nextStatus() {
		return Status.date;
	}
}

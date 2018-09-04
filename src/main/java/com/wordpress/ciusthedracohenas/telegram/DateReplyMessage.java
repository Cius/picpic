package com.wordpress.ciusthedracohenas.telegram;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.wordpress.ciusthedracohenas.picpic.models.Menu;
import com.wordpress.ciusthedracohenas.picpic.models.MenuItem;
import com.wordpress.ciusthedracohenas.telegram.BukatalangBot.Status;

public class DateReplyMessage extends MenuReplyMessage {
	@Override
	public String getText() {
		return "Siapa aja yang ikut? Pisahin pakai spasi ya biar ga kangen, gunakan /people untuk lihat siapa saja temanmu (lanjutin pakai /continue ya).";
	}
	
	@Override
	public Menu operate(Menu menu, String message) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			menu.setDate(dateFormat.parse(message));
		} catch (ParseException e) {
			menu.setDate(new Date());
			logger.info(e.getMessage());
		}
		menu.setMenuItems(new ArrayList<MenuItem>());
		return menu;
	}
	
	@Override
	public Status nextStatus() {
		return Status.suspects;
	}
}

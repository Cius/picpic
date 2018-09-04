package com.wordpress.ciusthedracohenas.telegram;

import com.wordpress.ciusthedracohenas.picpic.models.Menu;
import com.wordpress.ciusthedracohenas.picpic.models.MenuItem;
import com.wordpress.ciusthedracohenas.telegram.BukatalangBot.Status;

public class MenuItemReplyMessage extends MenuReplyMessage {
	@Override
	public String getText() {
		return "Okee. Lanjutin masukin menu ya, pakai '[item];[jumlah];[harga per item];[shareable]'. Gunakan command /done jika sudah selesai.";
	}
	
	@Override
	public Menu operate(Menu menu, String message) {
		String[] menuElements = message.split(";");
		MenuItem menuItem = new MenuItem(menuElements[0], Integer.parseInt(menuElements[1]), Double.parseDouble(menuElements[2]), Boolean.parseBoolean(menuElements[3]));
		menu.getMenuItems().add(menuItem);
		return menu;
	}
	
	@Override
	public Status nextStatus() {
		return Status.menu;
	}
}

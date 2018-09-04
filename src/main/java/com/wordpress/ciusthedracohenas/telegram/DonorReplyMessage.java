package com.wordpress.ciusthedracohenas.telegram;

import com.google.common.base.CaseFormat;
import com.wordpress.ciusthedracohenas.picpic.models.Menu;
import com.wordpress.ciusthedracohenas.telegram.BukatalangBot.Status;

public class DonorReplyMessage extends MenuReplyMessage {
	@Override
	public String getText() {
		return "Okee. Lanjutin masukin menu ya, pakai '[item];[jumlah];[harga per item];[shared]'. Gunakan command /done jika sudah selesai.";
	}
	
	@Override
	public Menu operate(Menu menu, String message) {
		menu.setDonor(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, message));
		return menu;
	}
	
	@Override
	public Status nextStatus() {
		return Status.menu;
	}
}

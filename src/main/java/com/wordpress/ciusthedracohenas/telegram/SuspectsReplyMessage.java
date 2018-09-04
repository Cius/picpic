package com.wordpress.ciusthedracohenas.telegram;

import java.util.ArrayList;

import com.google.common.base.CaseFormat;
import com.wordpress.ciusthedracohenas.picpic.models.Menu;
import com.wordpress.ciusthedracohenas.telegram.BukatalangBot.Status;

public class SuspectsReplyMessage extends MenuReplyMessage {
	@Override
	public String getText() {
		return "Siapa yang nalangin? Pake /people untuk lihat pilihan nama temanmu, trus jangan lupa lanjutin pakai /continue.";
	}
	
	@Override
	public Menu operate(Menu menu, String message) {
		String[] suspects = message.split(" ");
		menu.setSuspects(new ArrayList<String>());
		for(String suspect: suspects) {
			menu.getSuspects().add(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, suspect));
		}
		return menu;
	}
	
	@Override
	public Status nextStatus() {
		return Status.donor;
	}
}

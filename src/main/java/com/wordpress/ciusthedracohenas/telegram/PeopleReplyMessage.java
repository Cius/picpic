package com.wordpress.ciusthedracohenas.telegram;

import java.util.List;

public class PeopleReplyMessage extends ReplyMessage {	
	public void setPeople(List<String> people) {
		String text = "";
		for(String person: people) {
			text += person + " ";
		}
		setText(text.trim());
	}
}

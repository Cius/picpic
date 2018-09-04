package com.wordpress.ciusthedracohenas.telegram;

public class FailedDoneReplyMessage extends DoneReplyMessage {
	@Override
	public String getText() {
		return "Gagal nih. Maaf ya :(";
	}
}

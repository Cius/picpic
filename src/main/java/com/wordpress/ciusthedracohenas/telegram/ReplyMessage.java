package com.wordpress.ciusthedracohenas.telegram;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ReplyMessage {
	final static Logger logger = Logger.getLogger(ReplyMessage.class);

	private String text;

	public SendMessage getMessage(long chatId, int replyMessageId) {
		logger.info("Replying " + chatId + " with " + getText());
		return new SendMessage().setChatId(chatId).setText(getText()).setReplyToMessageId(replyMessageId);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

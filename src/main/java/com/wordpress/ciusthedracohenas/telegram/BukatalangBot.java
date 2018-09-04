package com.wordpress.ciusthedracohenas.telegram;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.wordpress.ciusthedracohenas.picpic.exception.PeopleNotFoundException;
import com.wordpress.ciusthedracohenas.picpic.models.Debt;
import com.wordpress.ciusthedracohenas.picpic.models.Menu;
import com.wordpress.ciusthedracohenas.picpic.services.BukatalangService;
import com.wordpress.ciusthedracohenas.picpic.services.PeopleService;
import com.wordpress.ciusthedracohenas.picpic.utils.PropertyUtil;

public class BukatalangBot extends TelegramLongPollingBot {
	final static Logger logger = Logger.getLogger(BukatalangBot.class);
	private static Map<Long, Menu> menus = new HashMap<Long, Menu>();
	private static Map<Long, Status> statuses = new HashMap<Long, Status>();
	private BukatalangService bukatalangService;
	private PeopleService peopleService;
	private PropertyUtil propertyUtil = PropertyUtil.getInstance();
	
	enum Status {
		place,
		date,
		suspects,
		donor,
		menu
	}
	

	public String getBotUsername() {
		return "pica_pica_bot";
	}

	public void onUpdateReceived(Update update) {
		if(update.hasMessage() && update.getMessage().hasText()) {
			Message message = update.getMessage();
			if(message.getText().equalsIgnoreCase("/add")) {
				start(message);
				updateStatus(message.getChatId(), message, false);
			}else if(message.getText().equalsIgnoreCase("/done")) {
				updateStatus(message.getChatId(), message, true);
			}else if(message.getText().equalsIgnoreCase("/continue")) {
				Status status = statuses.get(message.getChatId());
				if(status == null) {
					ReplyMessage replyMessage = new ReplyMessage();
					replyMessage.setText("Tidak ada yang perlu kita lanjutkan lagi.");
				}else {
					switch(status) {
					case place: start(message); break;
					case date: statuses.put(message.getChatId(), Status.place); break;
					case suspects: statuses.put(message.getChatId(), Status.date); break;
					case donor: statuses.put(message.getChatId(), Status.suspects); break;
					case menu: statuses.put(message.getChatId(), Status.menu); break;
					}
					updateStatus(message.getChatId(), message, false);
				}
			}else if(message.getText().equalsIgnoreCase("/people")) {
				PeopleReplyMessage replyMessage = new PeopleReplyMessage();
				replyMessage.setPeople(peopleService.getPeople());
				
				SendMessage sendMessage = replyMessage.getMessage(message.getChatId(), message.getMessageId());
				sendMessage(sendMessage);
			}else if(message.getText().equalsIgnoreCase("/my_debts")) {
				MyDebtsReplyMessage replyMessage = new MyDebtsReplyMessage();
				try {
					Debt debt = peopleService.getDebt("@" + message.getFrom().getUserName());
					replyMessage.setDebt(debt);
				}catch(PeopleNotFoundException e) {
					replyMessage = new PeopleNotFoundReplyMessage();
				}

				SendMessage sendMessage = replyMessage.getMessage(message.getChatId(), message.getMessageId());
				sendMessage(sendMessage);
			}else {
				if(statuses.containsKey(message.getChatId())) {
					updateStatus(message.getChatId(), message, false);
				}
			}
		}
	}

	@Override
	public String getBotToken() {
		return propertyUtil.getProperty("PICA_TOKEN");
	}

	public void setBukatalangService(BukatalangService bukatalangService) {
		this.bukatalangService = bukatalangService;
	}
	
	public void setPeopleService(PeopleService peopleService) {
		this.peopleService = peopleService;
	}
	
	private void start(Message message) {
		Status status = statuses.get(message.getChatId());
		if(status != null) {
			statuses.remove(message.getChatId());
			menus.remove(message.getChatId());
		}
	}
	
	private void updateStatus(long chatId, Message message, boolean done) {
		Status status = statuses.get(chatId);
		MenuReplyMessage replyMessage = new MenuReplyMessage();
		String text = message.getText();
		if(done) {
			replyMessage = new DoneReplyMessage();
			try {
				Menu menu = menus.get(chatId);
				if(menu != null) {
					bukatalangService.submit(menu);
				}
			}catch(Exception e) {
				replyMessage = new FailedDoneReplyMessage();
				logger.info(e.getMessage());
			}finally {
				statuses.remove(chatId);
				menus.remove(chatId);
			}
		}else if(status == null) {
			replyMessage = new AddReplyMessage();
			Menu menu = new Menu();
			menus.put(chatId, menu);
			statuses.put(chatId, replyMessage.nextStatus());
		}else {
			Menu menu = menus.get(chatId);
			if(menu != null) {
				switch(status) {
				case place: replyMessage = new PlaceReplyMessage(); break;
				case date: replyMessage = new DateReplyMessage(); break;
				case suspects: replyMessage = new SuspectsReplyMessage(); break;
				case donor: replyMessage = new DonorReplyMessage(); break;
				case menu: replyMessage = new MenuItemReplyMessage(); break;
				}
				
				replyMessage.operate(menu, text);
				statuses.put(chatId, replyMessage.nextStatus());
			}
		}
		
		SendMessage sendMessage = replyMessage.getMessage(chatId, message.getMessageId());
		sendMessage(sendMessage);
	}
	
	private void sendMessage(SendMessage sendMessage) {
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			logger.info(e.getMessage());
		}
	}
}

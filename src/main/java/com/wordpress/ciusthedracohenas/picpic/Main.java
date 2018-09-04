package com.wordpress.ciusthedracohenas.picpic;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import com.wordpress.ciusthedracohenas.picpic.services.impl.GoogleSheetBukatalangServiceImpl;
import com.wordpress.ciusthedracohenas.picpic.services.impl.GoogleSheetPeopleServiceImpl;
import com.wordpress.ciusthedracohenas.telegram.BukatalangBot;

public class Main {
	final static Logger logger = Logger.getLogger(Main.class);
		
	public static void main(String[] args) throws GeneralSecurityException, IOException, ParseException {
		ApiContextInitializer.init();
		TelegramBotsApi api = new TelegramBotsApi();
		BukatalangBot bot = new BukatalangBot();
		bot.setBukatalangService(new GoogleSheetBukatalangServiceImpl());
		bot.setPeopleService(new GoogleSheetPeopleServiceImpl());
		
		try {
			api.registerBot(bot);
		} catch (TelegramApiRequestException e) {
			logger.error(e.getMessage());
		}
		
//		BukatalangService bukatalangService = new GoogleSheetBukatalangServiceImpl();
//		bukatalangService.submit(getMenu());
//		
//		PeopleService peopleService = new GoogleSheetPeopleServiceImpl();
//		peopleService.getDebt("@dracius");
//		peopleService.getPeople();
		
//		String s = "1,517,100.00";
//		DecimalFormat decimalFormat = new DecimalFormat("0,000.00");
//		System.out.println(decimalFormat.parse(s));
	}
	
//	private static Menu getMenu() {
//		Menu menu = new Menu();
//		menu.setPlaceName(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, ("Bala Bala" + new Random().nextInt(50)).replaceAll(" ", "")));
//		menu.setDate(new Date());
//		
//		List<MenuItem> menuItems = new ArrayList<MenuItem>();
//		for(int i = 0; i <= 13; i++) {
//			MenuItem menuItem = new MenuItem("Test " + i, 2, 2000.0, true);
//			menuItems.add(menuItem);
//		}
//		System.out.println(menuItems.size());
//		menu.setMenuItems(menuItems);
//		menu.setSuspects(new ArrayList<String>());
//		menu.getSuspects().add("Lia");
//		menu.getSuspects().add("Cius");
//		menu.getSuspects().add("Anis");
//		menu.getSuspects().add("Rahmi");
//		menu.setDonor("Cius");
//		return menu;
//	}
}

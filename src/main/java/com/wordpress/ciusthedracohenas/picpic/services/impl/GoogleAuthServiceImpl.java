package com.wordpress.ciusthedracohenas.picpic.services.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.wordpress.ciusthedracohenas.picpic.services.GoogleAuthService;
import com.wordpress.ciusthedracohenas.picpic.utils.GoogleAuthorizeUtil;

public class GoogleAuthServiceImpl implements GoogleAuthService {
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String APPLICATION_NAME = "Bukatalang";

	public Sheets getSheet() throws GeneralSecurityException, IOException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleAuthorizeUtil.authorize())
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        return service;
	}
}

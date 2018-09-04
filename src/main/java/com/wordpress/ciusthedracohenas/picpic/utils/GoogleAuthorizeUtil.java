package com.wordpress.ciusthedracohenas.picpic.utils;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.SheetsScopes;

public class GoogleAuthorizeUtil {
	private static final String CREDENTIAL_PATH = "/credential.json";
	
    public static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = GoogleAuthorizeUtil.class.getResourceAsStream(CREDENTIAL_PATH);
        GoogleCredential credential = GoogleCredential.fromStream(in)
        	    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));	
 
        return credential;
    }
}

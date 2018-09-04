package com.wordpress.ciusthedracohenas.picpic.services;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.services.sheets.v4.Sheets;

public interface GoogleAuthService {
	public Sheets getSheet() throws GeneralSecurityException, IOException;
}

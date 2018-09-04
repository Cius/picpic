package com.wordpress.ciusthedracohenas.picpic.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	private static final String PROPERTIES_FILE = "/config.properties";
	
	private Properties property;
	private static PropertyUtil instance;
	
	private PropertyUtil() {
		load();
	}
	
	private void load() {
		InputStream inputStream = PropertyUtil.class.getResourceAsStream(PROPERTIES_FILE);
		property = new Properties();
		try {
			property.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getProperty(String name) {
		return property.getProperty(name);
	}
	
	public static PropertyUtil getInstance() {
		if(instance == null) {
			instance = new PropertyUtil();
		}
		
		return instance;
	}
}

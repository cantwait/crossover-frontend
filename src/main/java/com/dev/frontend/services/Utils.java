package com.dev.frontend.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Utils
{	
	public static String getProperty(String key){
		Properties prop = new Properties();
		InputStream input = null;
		String value = null;
		try {
	 
			input = Utils.class.getResourceAsStream("/app.properties");
			System.out.println(input == null);
			prop.load(input);
			
			value = prop.getProperty(key);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
	
	public static Double parseDouble(String value)
	{
		if(value == null||value.isEmpty())
			return 0D;
		try
		{
			return Double.parseDouble(value);
		}
		catch(Exception e)
		{
			return 0D;
		}
	}
}

package com.mf.PaymentProcessor.support;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropLoaderSupport {

	public String getProperty(String key) {
		InputStream inputStream = null;

		try {
			Properties prop = new Properties();
			String propFileName = System.getProperty("config.file");
			inputStream = new FileInputStream(propFileName);
			prop.load(inputStream);
			
			return prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}
}

package com.jeta.locker.config;

import java.io.FileInputStream;
import java.util.Properties;

import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.LogUtils;
import com.jeta.locker.common.StringUtils;

public class LockerProperties {

	public static final String DATA_DIRECTORY = "data.directory";
	public static final String API_KEY = "api.key";
	public static final String PASSWORD = "password";
	
	private static Properties m_properties = null;
	
	static {
		try {
			initialize();
		} catch( Exception e ) {
			LogUtils.error("Unable to initialize properties file", e);
		}
	}

	public static String getDataDirectory() {
		return m_properties.getProperty( DATA_DIRECTORY );
	}
	
	public static String getDataFile() {
		return StringUtils.buildFilePath( LockerProperties.getDataDirectory(), "locker.data" );
	}
	
	public static String getAPIKey() {
		return m_properties.getProperty(API_KEY);
	}
	
	public static String getConfigDirectory() {
		String configPath = StringUtils.buildFilePath(System.getProperty("user.home"), ".locker");
		return configPath;
	}

	public static String getConfigFile() {
		String configFile = StringUtils.buildFilePath( getConfigDirectory(), "locker.config");
		return configFile;
	}
	/**
	 * Load properties from file
	 * @param servletContext 
	 */
	public static void initialize() throws LockerException {
		try {
			if ( m_properties != null ) {
				return;
			}
			m_properties = new Properties();
			FileInputStream is = new FileInputStream( getConfigFile() );
			m_properties.clear();
			m_properties.load(is);
			is.close();

			System.out.println( "loaded properties file.  data file: " + getDataFile() );
			
		} catch( Exception e ) {
			throw LockerException.create(e);
		}
	}


}

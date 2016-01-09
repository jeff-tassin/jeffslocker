package com.jeta.locker.config;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.LogUtils;
import com.jeta.locker.common.StringUtils;

/**
 *   
 * @author Jeff Tassin
 */
public class AppConfig {

		
	private static Properties m_properties = null;
	
	static {
		try {
			initialize();
		} catch( Exception e ) {
			LogUtils.error("Unable to initialize properties file", e);
		}
	}

	/**
	 * Get property by its key
	 */
	public static String getProperty(String key) {
		String prop = StringUtils.safeTrim(m_properties.getProperty(key));
		return prop;
	}

	public static int getIntProperty(String key, int defaultValue) {
		return StringUtils.safeParseInt(getProperty(key), defaultValue);
	}
	
	
	public static List<String> getMostRecentFiles() {
		List<String> files = new ArrayList<String>();
		files.add("/home/tiger/Dropbox/Accounts/locker.data");
		return files;
	}
		
	/**
	 * @return the path to the config directory.  This is $USER_HOME/.jlocker
	 */
	public static String getConfigDirectory() {
		String configPath = StringUtils.buildFilePath(System.getProperty("user.home"), ".jlocker");
		return configPath;
	}

	/**
	 * @return the path to the config file.  This is $USER_HOME/.locker/locker.config
	 */
	public static String getPropertiesFile() {
		String configFile = StringUtils.buildFilePath( getConfigDirectory(), "jlocker.properties");
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
			FileInputStream is = new FileInputStream( getPropertiesFile() );
			m_properties.clear();
			m_properties.load(is);
			is.close();

		} catch( Exception e ) {
			throw LockerException.create(e);
		}
	}


}

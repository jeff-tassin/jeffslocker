package com.jeta.locker.config;

import java.io.FileInputStream;
import java.util.Properties;

import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.LogUtils;
import com.jeta.locker.common.StringUtils;

public class LockerConfig {

	public static final String DATA_DIRECTORY = "data.directory";
	public static final String KEY_SIZE = "key.size";
	public static final String INIT_VECTOR = "init.vector";
	public static final String SALT = "salt";
	public static final String PASSWORD_DECORATION = "password.decoration";
	
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
		String prop = m_properties.getProperty(key);
		return prop;
	}

	public static int getIntProperty(String key, int defaultValue) {
		return StringUtils.safeParseInt(getProperty(key), defaultValue);
	}
	
	
	public static String getDataDirectory() {
		return m_properties.getProperty( DATA_DIRECTORY );
	}
	
	public static String getDataFile() {
		return StringUtils.buildFilePath( LockerConfig.getDataDirectory(), "locker.data" );
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
	 * This is normally 128 bits. 
	 * Java default install only supports 128 bit keys.  For 256 you must install the
	 * Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files:
	 * http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
	 * Copy the jars to $JAVA_HOME/jre/lib/security. On OSX this is currently located at:
	 * /Library/Java/JavaVirtualMachines/jdk1.x.x.jdk/Contents/Home/jre/lib/security
	 */
	public static int getKeySize() {
		return getIntProperty( KEY_SIZE, 128 );
	}
	
	public static String getInitVector() {
		return getProperty( INIT_VECTOR );
	}
	
	public static String getSalt() {
		return getProperty( SALT );
	}
	
	public static String getPasswordDecoration() {
		return getProperty( PASSWORD_DECORATION );
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

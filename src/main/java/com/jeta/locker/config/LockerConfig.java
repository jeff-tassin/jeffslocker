package com.jeta.locker.config;

import java.io.FileInputStream;
import java.util.Properties;

import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.LogUtils;
import com.jeta.locker.common.StringUtils;

/**
 * ************* Important note *******************************************
 *  1. Keep you locker config file separate from your locker.data file. 
 *  2. Protect your locker.config. It contains some, but not all, keys needed for authentication.
 *  3. Make a backup of your locker.config.  If you lose this file, you will not be able to access your encrypted data in locker.data
 *  4. The locker.config is stored in under $USER_HOME/.locker
 *
 *  The locker config stores the following:  
 *  1. AES Salt
 *  2. AES IV
 *  3. Password decorator
 *  4. Locker data file path
 *   
 *   
 * @author Jeff Tassin
 */
public class LockerConfig {

	/**
	 * property names found in locker.config
	 */
	public static final String DATA_DIRECTORY = "data.directory";
	public static final String KEY_SIZE = "key.size";
	public static final String INIT_VECTOR = "init.vector";
	public static final String SALT = "salt";
	public static final String PASSWORD_PEPPER = "password.pepper";
	
	
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
	
	/**
	 * The data directory is where your locker.data is stored.  The locker.data contains your encrypted passwords.
	 */
	public static String getDataDirectory() {
		return getProperty( DATA_DIRECTORY );
	}
	/**
	 * @return the full path to locker.data.  This file contains your encrypted passwords.
	 */
	public static String getDataFile() {
		return StringUtils.buildFilePath( LockerConfig.getDataDirectory(), "locker.data" );
	}
	
	/**
	 * @return the path to the config directory.  This is $USER_HOME/.locker
	 */
	public static String getConfigDirectory() {
		String configPath = StringUtils.buildFilePath(System.getProperty("user.home"), ".locker");
		return configPath;
	}

	/**
	 * @return the path to the config file.  This is $USER_HOME/.locker/locker.config
	 */
	public static String getConfigFile() {
		String configFile = StringUtils.buildFilePath( getConfigDirectory(), "locker.config");
		return configFile;
	}
	
	/**
	 * This is normally 128 bits, as the Java default install only supports 128 bit keys. 
	 * For 256 bit support, you must install the Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files:
	 * http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
	 * Copy the jars to $JAVA_HOME/jre/lib/security. On OSX this is currently located at:
	 * /Library/Java/JavaVirtualMachines/jdk1.x.x.jdk/Contents/Home/jre/lib/security
	 */
	public static int getKeySize() {
		return getIntProperty( KEY_SIZE, 128 );
	}
	
	/**
	 * @see https://en.wikipedia.org/wiki/Initialization_vector
	 */
	public static String getInitVector() {
		return getProperty( INIT_VECTOR );
	}
	
	/**
	 * @see https://en.wikipedia.org/wiki/Salt_(cryptography)
	 */
	public static String getSalt() {
		return getProperty( SALT );
	}
	
	/**
	 * The password decoration is prepended to your password during encrypt/decrypt.  This adds another factor
	 * to your authentication.
	 */
	public static String getPasswordPepper() {
		return getProperty( PASSWORD_PEPPER );
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

		} catch( Exception e ) {
			throw LockerException.create(e);
		}
	}


}

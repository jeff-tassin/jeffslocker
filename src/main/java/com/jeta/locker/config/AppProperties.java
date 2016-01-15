package com.jeta.locker.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jeta.locker.common.FileUtils;
import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.LogUtils;
import com.jeta.locker.common.StringUtils;

/**
 *   
 * @author Jeff Tassin
 */
public class AppProperties {

		
	private static Properties m_properties = null;
	
	static {
		try {
			initialize();
		} catch( Exception e ) {
			LogUtils.error("Unable to initialize properties file", e);
		}
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
			// eat the exception and hope the app can continue
		}
	}

	public static void addMostRecentFile( String path ) {
		if ( !new File(path).isFile() ) {
			return;
		}
		List<String> files = getMostRecentFiles();
		files.remove(path);
		files.add( 0, path );
		for( int index=0; index < 10; index++ ) {
			String propname = "recent.locker." + index;
			if ( index < files.size() ) {
				m_properties.put( propname, files.get(index) );
			} else {
				m_properties.put( propname, "" );
			}
		}
		save();
	}
	
	public static void save() {
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(getPropertiesFile());
			m_properties.store( fos, "" );
			fos.flush();
		} catch( Exception e ) {
			LogUtils.debug("Unable to store property file.", e);
		} finally {
			FileUtils.close( fos );
		}
	}

	public static List<String> getMostRecentFiles() {
		List<String> files = new ArrayList<String>();
		for( int index=0; index < 10; index++ ) {
			String file = m_properties.getProperty("recent.locker." + index );
			if ( file != null && new File(file).isFile() ) {
				files.add( file );
			}
		}
		return files;
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
	
	public static void setProperty( String key, String value ) {
		m_properties.put( key, value );
	}
	public static void setProperty( String key, int ival ) {
		m_properties.put( key, String.valueOf(ival));
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
	

}

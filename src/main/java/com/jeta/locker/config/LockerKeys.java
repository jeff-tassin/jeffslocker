package com.jeta.locker.config;


import java.io.FileInputStream;
import java.util.Properties;

import org.json.JSONObject;

import com.jeta.locker.common.LockerException;
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
public class LockerKeys {

	/**
	 * property names found in locker.config
	 */
	public static final String DATA_DIRECTORY = "data.directory";
	public static final String INIT_VECTOR = "init.vector";
	public static final String SALT = "salt";
	public static final String PEPPER = "pepper";
	
	
	private String m_id;
	private int m_keySize;
	private Properties m_keys;
	
	public LockerKeys( String id, int keySize ) throws LockerException {
		m_id = id;
		m_keySize = keySize;
		String keyFile = StringUtils.buildFilePath( AppConfig.getConfigDirectory(), "jlocker." + id + ".keys" );
		try {
			Properties props = new Properties();
			FileInputStream is = new FileInputStream( keyFile );
			props.load(is);
			is.close();
			m_keys = props;
		} catch( Exception e ) {
			e.printStackTrace();
			throw LockerException.create(e);
		}
	}
	
	public String getId() {
		return m_id;
	}
	/**
	 * This is normally 128 bits, as the Java default install only supports 128 bit keys. 
	 * For 256 bit support, you must install the Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files:
	 * http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
	 * Copy the jars to $JAVA_HOME/jre/lib/security. On OSX this is currently located at:
	 * /Library/Java/JavaVirtualMachines/jdk1.x.x.jdk/Contents/Home/jre/lib/security
	 */
	public int getKeySize() {
		return m_keySize;
	}
	
	/**
	 * @see https://en.wikipedia.org/wiki/Initialization_vector
	 */
	public String getInitVector() {
		return m_keys.getProperty( INIT_VECTOR );
	}
	
	/**
	 * @see https://en.wikipedia.org/wiki/Salt_(cryptography)
	 */
	public String getSalt() {
		return m_keys.getProperty( SALT );
	}
	
	/**
	 * The password decoration is prepended to your password during encrypt/decrypt.  This adds another factor
	 * to your authentication.
	 */
	public  String getPepper() {
		return m_keys.getProperty( PEPPER );
	}
	
}

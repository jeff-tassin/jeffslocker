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
	private String m_iv;
	private String m_salt;
	private String m_pepper;
	

	public LockerKeys( String id, int keySize ) throws LockerException {
		m_id = id;
		m_keySize = keySize;
		String keyFile = StringUtils.buildFilePath( AppProperties.getConfigDirectory(), id + ".jkeys" );
		try {
			Properties props = new Properties();
			FileInputStream is = new FileInputStream( keyFile );
			props.load(is);
			is.close();
			m_iv = StringUtils.safeTrim(props.getProperty( INIT_VECTOR ));
			m_salt = StringUtils.safeTrim(props.getProperty( SALT ));
			m_pepper = StringUtils.safeTrim(props.getProperty( PEPPER ));
		} catch( Exception e ) {
			e.printStackTrace();
			throw LockerException.create(e);
		}
	}
	
	public LockerKeys( String id, int keySize, String iv, String salt, String pepper ) {
		m_id = id;
		m_keySize = keySize;
		m_iv = iv;
		m_salt = salt;
		m_pepper = pepper;
	}
	
	public String getFilePath() {
		return StringUtils.buildFilePath( AppProperties.getConfigDirectory(), m_id + ".jkeys" );
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
		return m_iv;
	}
	
	/**
	 * @see https://en.wikipedia.org/wiki/Salt_(cryptography)
	 */
	public String getSalt() {
		return m_salt;
	}
	
	/**
	 * The password decoration is prepended to your password during encrypt/decrypt.  This adds another factor
	 * to your authentication.
	 */
	public  String getPepper() {
		return m_pepper;
	}
	
}

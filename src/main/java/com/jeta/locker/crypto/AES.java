package com.jeta.locker.crypto;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.jeta.locker.config.LockerProperties;

public class AES {

	/**
	 * @TODO - this is a work in progress.
	 */
	private static String m_salt = "AKE)(#$JCL#J-Sas931a#!4K)89SDee";
	private static int ITERATOR_COUNT = 65536;
	/**
	 * Java default install only supports 128 bit keys.  For 256 you must install the
	 * Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files:
	 * http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
	 * Copy the jars to $JAVA_HOME/jre/lib/security. On OSX this is currently located at:
	 * /Library/Java/JavaVirtualMachines/jdk1.x.x.jdk/Contents/Home/jre/lib/security
	 */
	private static int KEY_SIZE = 256;
	private static String IV_BYTES = "341#$*^FGafdgf#%";
	
	/**
	 * this can never change
	 */
	
	public static byte[] _encrypt( String password, String plainText) throws Exception {
		/**
		 * prepend API key to strengthen password 
		 * The API key is stored in a local config file, which should be kept separate from your locker.data file which should be stored in cloud somewhere.
		 */
		password = LockerProperties.getAPIKey() + password;

		//get salt
		byte[] saltBytes = m_salt.getBytes("UTF-8");
		
		// Derive the key
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(
				password.toCharArray(), 
				saltBytes, 
				ITERATOR_COUNT, 
				KEY_SIZE
				);
		
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		
		//encrypt the message
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final IvParameterSpec ivSpec = new IvParameterSpec(IV_BYTES.getBytes("UTF-8"));

		cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
		return encryptedBytes;
	}
	
	
	public static byte[] encrypt( String password, String data) throws Exception {
		return Base64.getEncoder().encode( _encrypt( password, data ) );
	}
	

	public static String decrypt( String password, byte[] encryptedText) throws Exception {
		byte[] data = Base64.getDecoder().decode(encryptedText);
		return _decrypt( password, data );
	}

	public static String _decrypt( String password, byte[] encryptedText) throws Exception {
		/**
		 * prepend API key to strengthen password 
		 * The API key is stored in a local config file, which should be kept separate from your locker.data file.
		 */
		password = LockerProperties.getAPIKey() + password;

		byte[] saltBytes = m_salt.getBytes("UTF-8");
		// Derive the key
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(
				password.toCharArray(), 
				saltBytes, 
				ITERATOR_COUNT, 
				KEY_SIZE
				);
		
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		
		// Decrypt the message
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final IvParameterSpec ivSpec = new IvParameterSpec( IV_BYTES.getBytes("UTF-8"));
		cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
		
		
		byte[] decryptedTextBytes = null;
		decryptedTextBytes = cipher.doFinal(encryptedText);
		return new String(decryptedTextBytes);
	}
	
		
}
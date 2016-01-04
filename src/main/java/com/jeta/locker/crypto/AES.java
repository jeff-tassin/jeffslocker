package com.jeta.locker.crypto;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.jeta.locker.config.LockerConfig;

public class AES {

	private static int ITERATOR_COUNT = 65536;

	public static byte[] encrypt( String password, String data) throws Exception {
		return Base64.getEncoder().encode( _encrypt( password, data ) );
	}
	

	public static String decrypt( String password, byte[] encryptedText) throws Exception {
		byte[] data = Base64.getDecoder().decode(encryptedText);
		return _decrypt( password, data );
	}


	public static byte[] _encrypt( String password, String plainText) throws Exception {
		/**
		 * prepend password decoration to strengthen password 
		 * The password decoration is a series of random digits stored in a local config file. 
		 * The config file stores some keys (but not your password) for multi-factor authentication and therefore must be kept separate from your locker.data file.
		 */
		password = LockerConfig.getPasswordPepper() + password;

		//get salt
		byte[] saltBytes = LockerConfig.getSalt().getBytes("UTF-8");
		
		// Derive the key
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(
				password.toCharArray(), 
				saltBytes, 
				ITERATOR_COUNT, 
				LockerConfig.getKeySize()
				);
		
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		
		//encrypt the message
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final IvParameterSpec ivSpec = new IvParameterSpec( LockerConfig.getInitVector().getBytes("UTF-8"));

		cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
		return encryptedBytes;
	}
	
	
	public static String _decrypt( String password, byte[] encryptedText) throws Exception {
	
		/**
		 * prepend password decoration to strengthen password 
		 * The password decoration is a series of random digits stored in a local config file. 
		 * The config file stores some keys  (but not your password) for multi-factor authentication and therefore must be kept separate from your locker.data file.
		 */
		password = LockerConfig.getPasswordPepper() + password;

		byte[] saltBytes = LockerConfig.getSalt().getBytes("UTF-8");
		// Derive the key
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(
				password.toCharArray(), 
				saltBytes, 
				ITERATOR_COUNT, 
				LockerConfig.getKeySize()
				);
		
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		
		// Decrypt the message
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final IvParameterSpec ivSpec = new IvParameterSpec( LockerConfig.getInitVector().getBytes("UTF-8"));
		cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
		
		
		byte[] decryptedTextBytes = null;
		decryptedTextBytes = cipher.doFinal(encryptedText);
		return new String(decryptedTextBytes);
	}
	
		
}
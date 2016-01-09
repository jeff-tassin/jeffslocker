package com.jeta.locker.crypto;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.jeta.locker.config.LockerKeys;

public class AES {

	private static int ITERATOR_COUNT = 65536;

	public static String encrypt( int keySize, String salt, String iv, String pepper, String password, String data) throws Exception {
		return Base64.getEncoder().encodeToString( _encrypt( keySize, salt, iv, pepper, password, data ) );
	}
	

	public static String decrypt( int keySize, String salt, String iv, String pepper, String password, String encryptedText) throws Exception {
		byte[] data = Base64.getDecoder().decode(encryptedText);
		return _decrypt( keySize, salt, iv, pepper, password, data );
	}


	public static byte[] _encrypt( int keySize, String salt, String iv, String pepper, String password, String plainText) throws Exception {
		/**
		 * prepend password pepper to strengthen password 
		 */
		password = pepper + password;

		//get salt
		byte[] saltBytes = salt.getBytes("UTF-8");
		
		// Derive the key
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(
				password.toCharArray(), 
				saltBytes, 
				ITERATOR_COUNT, 
				keySize
				);
		
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		
		//encrypt the message
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final IvParameterSpec ivSpec = new IvParameterSpec( iv.getBytes("UTF-8"));

		cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
		return encryptedBytes;
	}
	
	
	public static String _decrypt( int keySize, String salt, String iv, String pepper, String password, byte[] encryptedText) throws Exception {
	
		/**
		 * prepend password pepper to strengthen password 
		 */
		password = pepper + password;

		byte[] saltBytes = salt.getBytes("UTF-8");
		// Derive the key
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(
				password.toCharArray(), 
				saltBytes, 
				ITERATOR_COUNT, 
				keySize
				);
		
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		
		// Decrypt the message
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final IvParameterSpec ivSpec = new IvParameterSpec( iv.getBytes("UTF-8"));
		cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
		
		
		byte[] decryptedTextBytes = null;
		decryptedTextBytes = cipher.doFinal(encryptedText);
		return new String(decryptedTextBytes);
	}
	
		
}
package com.jeta.locker.io;

import static com.jeta.locker.common.LockerConstants.ID;
import static com.jeta.locker.common.LockerConstants.KEY_SIZE;
import static com.jeta.locker.common.LockerConstants.PAYLOAD;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;

import org.json.JSONObject;

import com.jeta.locker.common.FileUtils;
import com.jeta.locker.common.LockerException;
import com.jeta.locker.config.LockerKeys;
import com.jeta.locker.crypto.AES;

public class LockerIO {
	
	public static class ReadResult {
		public ReadResult( LockerKeys keys, JSONObject json ) {
			this.keys = keys;
			this.json = json;
		}
		private LockerKeys keys;
		private JSONObject json;
		
		public LockerKeys getKeys() {
			return keys;
		}
		public JSONObject getJSON() {
			return json;
		}
	}
	
	/**
	 * Reads the locker file and decrypts the data using multifactor authentication, the factors being:
	 *   1. password  2. associated key file
	 *   
	 * @param filePath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static ReadResult read( String filePath, String password ) throws Exception {
		FileInputStream fis = null;
		try {
			File dataFile = new File( filePath );
			if ( !dataFile.isFile() ) {
				throw new LockerException( filePath + " not found.");
			}
			
			fis = new FileInputStream(dataFile);
			byte[] data = new byte[(int)dataFile.length()];
			fis.read(data);
			
			/**
			 * 1. convert file to JSON. 
			 * 2. get the locker ID.
			 * 3. load the keys
			 * 4. using the keys and password, decrypt the locker payload 
			 */
			JSONObject locker = new JSONObject( new String(data, "UTF-8") );
			String id = locker.getString(ID);
			int keySize = locker.getInt(KEY_SIZE);
			
			System.out.println( "locker model loaded id: " + id + " keysize: " + keySize );
			
			LockerKeys keys = new LockerKeys( id, keySize );
			
			String payload = "{}";
			try {
				payload = AES.decrypt( keys.getKeySize(), 
						keys.getSalt(), 
						keys.getInitVector(), 
						keys.getPepper(), 
						password, 
						locker.getString(PAYLOAD) );
			} catch( InvalidKeyException e ) {
				/**
				 * this exception can be called if you encrypted with 256 bits but are using 128 bit defaults that
				 * come with JDK.
				 */
				throw new LockerException( e.getMessage() + "\nCheck that you have installed 256bit extensions.");
			} catch( Exception e ) {
				throw new LockerException( "Invalid password.");
			}

			return new ReadResult( keys, new JSONObject(payload) );
		} catch( Exception e ) {
			throw LockerException.create( e );
		} finally {
			FileUtils.close( fis ); 
		}
	}

	public static void write( String dataFile, JSONObject payload, String password, LockerKeys keys ) throws LockerException {
		try {
			String encryptedPayload = AES.encrypt( keys.getKeySize(), 
					keys.getSalt(),
					keys.getInitVector(),
					keys.getPepper(),  
					password, 
					payload.toString() );
			
			JSONObject locker = new JSONObject();
			locker.put( ID, keys.getId() );
			locker.put( KEY_SIZE, keys.getKeySize() ); 
			locker.put( PAYLOAD, encryptedPayload );
			
			FileOutputStream fis = new FileOutputStream(dataFile);
			fis.write( locker.toString().getBytes("UTF-8") );
			fis.close();
		} catch( Exception e ) {
			throw new LockerException("Unable to read file: " + dataFile );
		}
	}
	

}

package com.jeta.locker.main;



import static com.jeta.locker.common.LockerConstants.*;

import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jeta.locker.common.FileUtils;
import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.StringUtils;
import com.jeta.locker.config.LockerKeys;
import com.jeta.locker.crypto.AES;

public class LockerModel {

	private LockerKeys m_keys;
	private String m_password;
	/**
	 * locker file name and path
	 */
	private String m_filePath;
	private List<Worksheet>  m_worksheets = new ArrayList<Worksheet>();
	
	
	public LockerModel( String dataFile, String password ) throws LockerException {
		System.out.println( "LockerModel.loading data file: " + dataFile );
		m_filePath = dataFile;
		m_password = password;
	
		try {
			readData(dataFile);
		} catch( Exception e ) {
			throw LockerException.create( e );
		}
	}
	
	public String getFilePath() {
		return m_filePath;
	}
	public String getPassword() {
		return m_password;
	}

	
	public List<Worksheet> getWorksheets() {
		return m_worksheets;
	}

	public void addWorksheet(Worksheet worksheet) {
		m_worksheets.add(worksheet);
		worksheet.setModified(true);
	}
	
	public boolean isModified() {
		for( Worksheet ws : m_worksheets ) {
			if ( ws.isModified() ) {
				return true;
			}
		}
		return false;
	}
	
	private void readData( String filePath ) throws LockerException {
		FileInputStream fis = null;
		try {
			List<Worksheet> results = new ArrayList<Worksheet>();
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
			
			m_keys = new LockerKeys( id, keySize );
			
			String payload = "{}";
			try {
				payload = AES.decrypt( m_keys.getKeySize(), 
						m_keys.getSalt(), 
						m_keys.getInitVector(), 
						m_keys.getPepper(), 
						getPassword(), 
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
				
								
			JSONObject json = new JSONObject(payload);
			JSONArray worksheets = json.optJSONArray(WORKSHEETS);
			if ( worksheets != null ) {
				for( int index=0; index < worksheets.length(); index++ ) {
					results.add( Worksheet.fromJSON( worksheets.getJSONObject(index)));
				}
			}
			fis.close();
			m_worksheets = results;
			for( Worksheet ws : m_worksheets ) {
				ws.setModified(false);
			}	
		} catch( Exception e ) {
			throw LockerException.create( e );
		} finally {
			FileUtils.close( fis ); 
		}
	}
	

	public void save() throws LockerException {
		try {
			//String configFile = StringUtils.buildFilePath( LockerConfig.getDataDirectory(), "locker.data" );
	
			/**
			 * first make a backup
			 */
			/*
			SimpleDateFormat format = new SimpleDateFormat("MMM_d_yyyy_HH_mm_ss");
			String backupFile = "locker.backup." + format.format( new Date() );
			String backupPath = StringUtils.buildFilePath( LockerConfig.getDataDirectory(), backupFile );
			
			Files.copy( new File(configFile).toPath(), new File(backupPath).toPath(), StandardCopyOption.COPY_ATTRIBUTES);
			*/
			/**
			 * now write data file
			 */
			writeData( m_filePath );
			for( Worksheet ws : m_worksheets ) {
				ws.setModified(false);
			}
		} catch( Exception e ) {
			throw LockerException.create( e );
		}
	}
	
	public void writeData( String dataFile ) throws LockerException {
		try {

			JSONObject payload = new JSONObject();
			JSONArray passwords = new JSONArray();
			for( Worksheet sheet : m_worksheets ) {
				passwords.put( sheet.toJSON() );
			}
			payload.put( WORKSHEETS, passwords );
			payload.put( "JEFFS_LOCKER", "version 1.0" );
			String encryptedPayload = AES.encrypt( m_keys.getKeySize(), m_keys.getSalt(), m_keys.getInitVector(), m_keys.getPepper(), getPassword(), payload.toString() );
			
			
			JSONObject locker = new JSONObject();
			locker.put( ID, m_keys.getId() );
			locker.put( KEY_SIZE, m_keys.getKeySize() ); 
			locker.put( PAYLOAD, encryptedPayload );
			
			FileOutputStream fis = new FileOutputStream(dataFile);
			fis.write( locker.toString().getBytes("UTF-8") );
			fis.close();
		} catch( Exception e ) {
			throw new LockerException("Unable to read file: " + dataFile );
		}
	}
	
}

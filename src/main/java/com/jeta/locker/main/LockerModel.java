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

import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.StringUtils;
import com.jeta.locker.config.LockerConfig;
import com.jeta.locker.crypto.AES;

public class LockerModel {

	private String m_password = null;
	private List<Worksheet>  m_worksheets = new ArrayList<Worksheet>();
	
	public LockerModel( String password ) throws LockerException {
		System.out.println( "loading data file: " + LockerConfig.getDataFile() );
		m_password = password;
		try {
			String filePath = LockerConfig.getDataFile();
			readData(filePath);
		} catch( Exception e ) {
			throw LockerException.create( e );
		}
	}
	public String getFilePath() {
		return LockerConfig.getDataFile();
	}
	
	public String getLockerPassword() {
		return m_password;
	}
	public void setLockerPassword( String password ) {
		m_password = password;
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
		try {
			List<Worksheet> results = new ArrayList<Worksheet>();
			File dataFile = new File( filePath );
			if ( dataFile.isFile() ) {
				FileInputStream fis = new FileInputStream(dataFile);
				byte[] data = new byte[(int)dataFile.length()];
				fis.read(data);
				String jsonstr = "{}";
				try {
					jsonstr = AES.decrypt( getLockerPassword(), data );
				} catch( InvalidKeyException e ) {
					/**
					 * this exception can be called if you encrypted with 256 bits but are using 128 bit defaults that
					 * come with JDK.
					 */
					throw new LockerException( e.getMessage() + "\nCheck that you have installed 256bit extensions.");
				} catch( Exception e ) {
					throw new LockerException( "Invalid password.");
				}
				JSONObject json = new JSONObject(jsonstr);
			
				JSONArray passwords = json.optJSONArray(WORKSHEETS);
				if ( passwords != null ) {
					for( int index=0; index < passwords.length(); index++ ) {
						results.add( Worksheet.fromJSON( passwords.getJSONObject(index)));
					}
				}
				fis.close();
			} else {
				throw new LockerException( filePath + " not found.");
			}
			m_worksheets = results;
			for( Worksheet ws : m_worksheets ) {
				ws.setModified(false);
			}	
		} catch( Exception e ) {
			throw LockerException.create( e );
		}
	}
	

	public void save() throws LockerException {
		try {
			String configFile = StringUtils.buildFilePath( LockerConfig.getDataDirectory(), "locker.data" );
	
			/**
			 * first make a backup
			 */
			SimpleDateFormat format = new SimpleDateFormat("MMM_d_yyyy_HH_mm_ss");
			String backupFile = "locker.backup." + format.format( new Date() );
			String backupPath = StringUtils.buildFilePath( LockerConfig.getDataDirectory(), backupFile );
			
			Files.copy( new File(configFile).toPath(), new File(backupPath).toPath(), StandardCopyOption.COPY_ATTRIBUTES);
			/**
			 * now write data file
			 */
			writeData(configFile);
			for( Worksheet ws : m_worksheets ) {
				ws.setModified(false);
			}
		} catch( Exception e ) {
			throw LockerException.create( e );
		}
	}
	
	public void writeData( String dataFile ) throws LockerException {
		try {
			FileOutputStream fis = new FileOutputStream(dataFile);

			JSONObject json = new JSONObject();
			JSONArray passwords = new JSONArray();
			for( Worksheet sheet : m_worksheets ) {
				passwords.put( sheet.toJSON() );
			}
			json.put( WORKSHEETS, passwords );
			
			byte[] encrypted = AES.encrypt( getLockerPassword(), json.toString() );
			fis.write( encrypted );
			fis.close();
		} catch( Exception e ) {
			throw new LockerException("Unable to read file: " + LockerConfig.getDataDirectory() );
		}
	}
	
}

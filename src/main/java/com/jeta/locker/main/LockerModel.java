package com.jeta.locker.main;



import static com.jeta.locker.common.LockerConstants.*;

import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.StringUtils;
import com.jeta.locker.config.LockerProperties;
import com.jeta.locker.crypto.AES;

public class LockerModel {

	private String m_password = null;
	private List<Worksheet>  m_worksheets = new ArrayList<Worksheet>();
	
	public LockerModel( String password ) throws LockerException {
		m_password = password;
		try {
			String filePath = LockerProperties.getDataFile();
			readData(filePath);
		} catch( Exception e ) {
			throw LockerException.create( e );
		}
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
			String filePath = StringUtils.buildFilePath( LockerProperties.getDataDirectory(), "locker.data" );
			writeData(filePath);
			for( Worksheet ws : m_worksheets ) {
				ws.setModified(false);
			}	
			/**
			 * now save backup
			 */
			/*
			File folder = new File(LockerProperties.getConfigDirectory());
			File[] backups = folder.listFiles( new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if ( name.startsWith("locker.backup")){
						return true;
					}
					return false;
				}
			});
			
			SimpleDateFormat format = new SimpleDateFormat("MMM_d_yyyy_HH_mm_ss");
			String backupFile = "locker.backup." + format.format( new Date() );
			filePath = StringUtils.buildFilePath( LockerProperties.getConfigDirectory(), backupFile );
			writeData( filePath );
			*/
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
			throw new LockerException("Unable to read file: " + LockerProperties.getDataDirectory() );
		}
	}
	
}

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
import com.jeta.locker.config.AppProperties;
import com.jeta.locker.config.LockerKeys;
import com.jeta.locker.crypto.AES;
import com.jeta.locker.io.LockerIO;

public class LockerModel {

	private LockerKeys m_keys;
	private String m_password;
	/**
	 * locker file name and path
	 */
	private String m_filePath;
	private List<Worksheet>  m_worksheets = new ArrayList<Worksheet>();
	
	private LockerModel() {
		
	}
	
	public LockerModel( String dataFile, String password ) throws LockerException {
		System.out.println( "LockerModel.loading data file: " + dataFile );
		m_filePath = dataFile;
		m_password = password;
		try {
			readData(dataFile);
			AppProperties.addMostRecentFile( dataFile );
		} catch( Exception e ) {
			throw LockerException.create( e );
		}
	}
	
	public static LockerModel createFile( String filePath, String password, LockerKeys keys ) throws LockerException {
		LockerModel model = new LockerModel();
		model.m_keys = keys;
		model.m_filePath = filePath;
		model.m_password = password;
		model.save();
		return model;
	}
	
	public String getFilePath() {
		return m_filePath;
	}
	public String getPassword() {
		return m_password;
	}

	public LockerKeys getKeys() {
		return m_keys;
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
			LockerIO.ReadResult result = LockerIO.read( filePath, getPassword() );
			m_keys = result.getKeys();
			JSONArray worksheets = result.getJSON().optJSONArray(WORKSHEETS);
			List<Worksheet> results = new ArrayList<Worksheet>();
			if ( worksheets != null ) {
				for( int index=0; index < worksheets.length(); index++ ) {
					results.add( Worksheet.fromJSON( worksheets.getJSONObject(index)));
				}
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

			JSONObject json = new JSONObject();
			JSONArray passwords = new JSONArray();
			for( Worksheet sheet : m_worksheets ) {
				passwords.put( sheet.toJSON() );
			}
			json.put( WORKSHEETS, passwords );
			LockerIO.write( m_filePath, json, getPassword(), m_keys );

			for( Worksheet ws : m_worksheets ) {
				ws.setModified(false);
			}
		} catch( Exception e ) {
			throw LockerException.create( e );
		}
	}

	
}

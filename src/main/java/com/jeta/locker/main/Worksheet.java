package com.jeta.locker.main;

import static com.jeta.locker.common.LockerConstants.*;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeta.locker.common.LockerUtils;

public class Worksheet {

	private String m_id;// the worksheet id
	private String m_name; // the worksheet name
	private int  m_type; //  the worksheet type (password, credit card, ssh key)
	private List<JSONObject>  m_accounts = new ArrayList<JSONObject>();
	private boolean m_modified = false;
	
	/**
	 * A list of listeners that get notified when worksheet is modified 
	 */
	private List<ChangeListener>  m_listeners = new ArrayList<ChangeListener>();
	
	public Worksheet(String id, String name, int type) {
		m_id = id;
		m_name = name;
		m_type = type;
	}

	public String getId() {
		return m_id;
	}
	
	public int getType() {
		return m_type;
	}
	
	public String getName() {
		return m_name;
	}
	
	public void setName(String name) {
		m_name = name;
		setModified(true);
	}
	
	public void setModified(boolean modified) {
		m_modified = modified;
		notifyListeners(new ChangeEvent(this));
	}
	
	public boolean isModified() {
		return m_modified;
	}
	
	public void addEntry( JSONObject acct ) {
		m_accounts.add(acct);
		setModified(true);
	}
	
	public JSONObject getAccount(int index) {
		return m_accounts.get(index);
	}

	public void removeEntry(String id) {
		Iterator<JSONObject> iter = m_accounts.iterator();
		while( iter.hasNext() ) {
			JSONObject acct = iter.next();
			if ( id.equals( acct.getString( ID ) ) ) {
				iter.remove();
				break;
			}
		}
		setModified(true);
	}

	public List<JSONObject> getEntries() {
		return Collections.unmodifiableList(m_accounts);
	}
	
	public void addListener(ChangeListener listener) {
		m_listeners.add(listener);
	}
	
	public void notifyListeners(ChangeEvent evt) {
		for( ChangeListener listener : m_listeners ) {
			listener.stateChanged(evt);
		}
	}
	public void removeListener(ChangeListener listener) {
		m_listeners.remove(listener);
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put( ID, m_id );
		json.put( NAME, m_name );
		json.put( WORKSHEET_TYPE,  m_type );

		JSONArray accounts = new JSONArray();
		for( JSONObject acct : m_accounts ) {
			accounts.put( acct );
		}
		json.put( ACCOUNTS, accounts );
		return json;
	}
	
	public static Worksheet fromJSON( JSONObject json ) {
		String id =  StringUtils.trim(json.optString(ID));
		if ( id.length() == 0 ) {
			id = LockerUtils.generateId();
		}
		String name = json.optString( NAME );
		int worksheetType = json.optInt( WORKSHEET_TYPE ); 
		Worksheet sheet = new Worksheet( id, name, worksheetType );
		JSONArray accts = json.optJSONArray(ACCOUNTS);
		for( int index=0; index < accts.length(); index++ ) {
			sheet.addEntry( accts.optJSONObject(index) );
		}
		return sheet;
	}


}


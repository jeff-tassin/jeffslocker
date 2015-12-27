package com.jeta.locker.type.password;



import java.util.List;

import javax.swing.table.AbstractTableModel;
import static com.jeta.locker.common.LockerConstants.*;

import org.json.JSONObject;

import com.jeta.locker.common.LockerConstants;
import com.jeta.locker.main.Worksheet;


public class PasswordTableModel extends AbstractTableModel {
	
    private String[] columnNames = {"Service Name",
                                    "User Name",
                                    "Password",
                                    "Description",
                                    };

    
    private Worksheet m_worksheet = null;
    
    public PasswordTableModel( Worksheet worksheet ) {
    	m_worksheet = worksheet;
    }

    public void addRow( JSONObject acct ) {
    	
    	
/*
    		String id =  StringUtils.trim(json.optString(ID));
    		acct.setServiceName(json.optString( SERVICE ) );
    		acct.setUserName( json.optString( USERNAME ) );
    		acct.setPassword( json.optString( PASSWORD ) );
    		acct.setDescription( json.optString( DESCRIPTION ) );
  */  	
    	m_worksheet.addEntry( acct );
    	List<JSONObject> passwds = m_worksheet.getEntries();
    	fireTableRowsInserted( passwds.size()-1, passwds.size()-1);
    }
    
    public Worksheet getWorksheet() {
    	return m_worksheet;
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
    	List<JSONObject> passwds = m_worksheet.getEntries();
    	return passwds.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
    	List<JSONObject> passwds = m_worksheet.getEntries();
        JSONObject data = passwds.get(row);
        if ( data == null ) {
        	return "";
        }
        switch(col) {
        case 0:
        	return data.optString( LockerConstants.SERVICE );
        case 1:
        	return data.optString( LockerConstants.USER_NAME );
        case 2:
        	return data.optString( LockerConstants.PASSWORD );
        case 3:
        	return data.optString( LockerConstants.DESCRIPTION );
        }
        return "";
    }
    
    public void setValueAt(Object value, int row, int col) {
    	List<JSONObject> passwds = m_worksheet.getEntries();
        JSONObject data = passwds.get(row);
        if ( data == null ) {
        	return;
        }
        String sval = String.valueOf(value);
        switch(col) {
        case 0:
        	data.put( SERVICE, sval );
        	break;
        case 1:
        	data.put( USER_NAME, sval );
        	break;
        case 2:
        	data.put( PASSWORD, sval );
        	break;
        case 3:
        	data.put( DESCRIPTION, sval );
        	break;
        }
        m_worksheet.setModified(true);
        fireTableCellUpdated(row, col);
     }
    
    public void deleteRow(int row) {
    	List<JSONObject> passwds = m_worksheet.getEntries();
        JSONObject data = passwds.get(row);
        if ( data == null ) {
        	return;
        }
        m_worksheet.removeEntry(data.getString( ID ) );
        this.fireTableRowsDeleted(row, row);
	}

    public boolean isPasswordColumn(int col) {
    	return "Password".equals(getColumnName(col)); 
    }
    
    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
    	return String.class;
     }

    public boolean isCellEditable(int row, int col) {
    	return true;
    }
}

package com.jeta.locker.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.json.JSONObject;

import com.jeta.locker.common.LockerConstants;
import com.jeta.locker.common.StringUtils;
import com.jeta.locker.main.Worksheet;

public abstract class AbstractWorksheetModel extends AbstractTableModel {
    
    private Worksheet m_worksheet = null;
    private String[]  m_columnNames;
    private String[]  m_columnKeys;
    
    public AbstractWorksheetModel( Worksheet worksheet, String[] colNames, String[] colKeys ) {
    	m_worksheet = worksheet;
    	m_columnNames = colNames;
    	m_columnKeys = colKeys;
    }

    public Object getValueAt(int row, int col) {
    	List<JSONObject> passwds = getWorksheet().getEntries();
        JSONObject data = passwds.get(row);
        if ( data == null ) {
        	return "";
        }
        if ( col >= 0 && col < m_columnKeys.length ) {
        	return data.optString( m_columnKeys[col] );
        } else {
        	return "";
        }
    }
    
    public void setValueAt(Object value, int row, int col) {
    	List<JSONObject> passwds = getWorksheet().getEntries();
        JSONObject data = passwds.get(row);
        if ( data == null ) {
        	return;
        }
        String sval = StringUtils.safeTrim( value == null ? "" : value.toString() );
        if ( col >= 0 && col < m_columnKeys.length ) {
        	String currentVal = data.optString(m_columnKeys[col]);
        	if ( !sval.equals( currentVal ) ) {
        		data.put( m_columnKeys[col], sval );
                setModified(true);
                fireTableCellUpdated(row, col);
        	}
        }
     }
    

    public void addRow( JSONObject acct ) {
    	m_worksheet.addEntry( acct );
    	List<JSONObject> passwds = m_worksheet.getEntries();
    	fireTableRowsInserted( passwds.size()-1, passwds.size()-1);
    }
    

    public JSONObject getAccount(int row) {
		return getWorksheet().getAccount(row);
	}

    public Worksheet getWorksheet() {
    	return m_worksheet;
    }
    
    public int getColumnCount() {
        return m_columnNames.length;
    }

    public int getRowCount() {
    	List<JSONObject> passwds = m_worksheet.getEntries();
    	return passwds.size();
    }

    public String getColumnName(int col) {
        return m_columnNames[col];
    }

       
    public void deleteRow(int row) {
    	List<JSONObject> passwds = m_worksheet.getEntries();
        JSONObject data = passwds.get(row);
        if ( data == null ) {
        	return;
        }
        m_worksheet.removeEntry(data.getString(LockerConstants.ID ) );
        this.fireTableRowsDeleted(row, row);
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
    

	public void setModified(boolean modified) {
		m_worksheet.setModified(modified);
		fireTableDataChanged();
	}
    
   
}

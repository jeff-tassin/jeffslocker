package com.jeta.locker.worksheets;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.jeta.locker.main.Worksheet;

public class EditWorksheetsModel extends AbstractTableModel {
    
	private ArrayList<Worksheet>  m_data = new ArrayList<Worksheet>();

    public EditWorksheetsModel( List<Worksheet> worksheets ) {
    	m_data.addAll( worksheets );
    }

    public Object getValueAt(int row, int col) {
    	Worksheet w = m_data.get(row);
    	switch( col ) {
    	case 0:
    		return row+1;
    	case 1:
    		return w.getName();
    	case 2:
    		return w.getType();
    	}
    	return "";
    }
    
    public void setValueAt(Object value, int row, int col) {
    //	fireTableCellUpdated(row, col);
     }
    
    
    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
    	return m_data.size();
    }

    public String getColumnName(int col) {
        switch( col ) {
        case 0:
        	return "";
        case 1:
        	return "Name";
        case 2:
        	return "Type";
        }
        return "";
    }

    public Class getColumnClass(int c) {
    	return String.class;
     }

    public boolean isCellEditable(int row, int col) {
    	return col == 1;
    }
}

package com.jeta.locker.type.password;



import java.util.List;

import javax.swing.table.AbstractTableModel;
import static com.jeta.locker.common.LockerConstants.*;

import org.json.JSONObject;

import com.jeta.locker.common.LockerConstants;
import com.jeta.locker.main.Worksheet;
import com.jeta.locker.model.AbstractWorksheetModel;


public class PasswordTableModel extends AbstractWorksheetModel {
	
    private static String[] columnNames = {"Service Name",
                                    "User Name",
                                    "Password",
                                    "Description",
                                    };

    
    
    public PasswordTableModel( Worksheet worksheet ) {
    	super(worksheet, columnNames);
    }

  
    public Object getValueAt(int row, int col) {
    	List<JSONObject> passwds = getWorksheet().getEntries();
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
    	List<JSONObject> passwds = getWorksheet().getEntries();
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
        fireTableCellUpdated(row, col);
     }
    
   
    public boolean isPasswordColumn(int col) {
    	return col == 2;
    }
    
   
}

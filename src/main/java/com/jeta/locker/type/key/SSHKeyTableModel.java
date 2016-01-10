package com.jeta.locker.type.key;

import java.util.List;

import static com.jeta.locker.common.LockerConstants.*;

import org.json.JSONObject;

import com.jeta.locker.main.Worksheet;
import com.jeta.locker.model.AbstractWorksheetModel;


public class SSHKeyTableModel extends AbstractWorksheetModel {
	
    private static String[] columnNames = {"Service Name",
    								"Description",
                                    "Public Key",
                                    "Private Key"
                                    };
   
    public SSHKeyTableModel( Worksheet worksheet ) {
    	super( worksheet, columnNames );
    }
	
	
    public Object getValueAt(int row, int col) {
    	List<JSONObject> passwds = getWorksheet().getEntries();
        JSONObject data = passwds.get(row);
        if ( data == null ) {
        	return "";
        }
        switch(col) {
        case 0:
        	return data.optString( SERVICE );
        case 1:
        	return data.optString( DESCRIPTION );
        case 2:
        	return data.optString( PUBLIC_KEY );
        case 3:
        	return data.optString( PRIVATE_KEY );
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
        	data.put( DESCRIPTION, sval );
        	break;
        case 2:
        	data.put( PUBLIC_KEY, sval );
        	break;
        case 3:
        	data.put( PRIVATE_KEY, sval );
        	break;
        }
        fireTableCellUpdated(row, col);
     }


}
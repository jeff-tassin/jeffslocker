package com.jeta.locker.type.cc;

import java.util.List;

import static com.jeta.locker.common.LockerConstants.*;

import org.json.JSONObject;

import com.jeta.locker.common.StringUtils;
import com.jeta.locker.main.Worksheet;
import com.jeta.locker.model.AbstractWorksheetModel;


public class CreditCardTableModel extends AbstractWorksheetModel {
	
    private static String[] columnNames = {"Service Name",
                                    "Card Number",
                                    "Expiration MM/YY",
                                    "CVC",
                                    "Pin",
                                    };
    public CreditCardTableModel( Worksheet worksheet ) {
    	super(worksheet, columnNames);
    }
    
    public boolean isAccountNumberCol( int col ) {
    	return col == 1;
    }
    public boolean isExpirationCol( int col ) {
    	return col == 2;
    }
    public boolean isCVCCol( int col ) {
    	return col == 3;
    }
    public boolean isPinCol( int col ) {
    	return col == 4;
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
        	return data.optString( CARD_NUMBER );
        case 2:
        	return data.optString( EXPIRATION_DATE );
        case 3:
        	return data.optString( CVC );
        case 4:
        	return data.optString( PIN );
        }
        return "";
    }
    
    public void setValueAt(Object value, int row, int col) {
    	List<JSONObject> passwds = getWorksheet().getEntries();
        JSONObject data = passwds.get(row);
        if ( data == null ) {
        	return;
        }
        if ( value == null ) {
        	value = "";
        }
        String sval = StringUtils.safeTrim(String.valueOf(value));
        switch(col) {
        case 0:
        	data.put( SERVICE, sval );
        	break;
        case 1:
        	data.put( CARD_NUMBER, sval );
        	break;
        case 2:
        	data.put( EXPIRATION_DATE, sval );
        	break;
        case 3:
        	data.put( CVC, sval );
        	break;
        case 4:
        	data.put( PIN, sval );
        	break;
        }
        fireTableCellUpdated(row, col);
     }

}

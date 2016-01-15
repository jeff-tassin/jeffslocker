package com.jeta.locker.type.cc;

import static com.jeta.locker.common.LockerConstants.*;


import com.jeta.locker.main.Worksheet;
import com.jeta.locker.model.AbstractWorksheetModel;


public class CreditCardTableModel extends AbstractWorksheetModel {
	
    private static String[] columnNames = {"Service Name",
                                    "Card Number",
                                    "Expiration MM/YY",
                                    "CVC",
                                    "Pin",
                                    };
    
    private static String[] columnKeys = {
    		SERVICE,
    		CARD_NUMBER,
    		EXPIRATION_DATE,
    		CVC,
    		PIN 
    };
    
    public CreditCardTableModel( Worksheet worksheet ) {
    	super(worksheet, columnNames, columnKeys );
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

}

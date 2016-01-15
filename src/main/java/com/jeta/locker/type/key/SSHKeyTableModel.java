package com.jeta.locker.type.key;

import static com.jeta.locker.common.LockerConstants.*;

import com.jeta.locker.main.Worksheet;
import com.jeta.locker.model.AbstractWorksheetModel;


public class SSHKeyTableModel extends AbstractWorksheetModel {
	
    private static String[] columnNames = {"Service Name",
    								"Description",
                                    "Public Key",
                                    "Private Key"
                                    };
    
    private static String[] columnKeys = {
    		SERVICE,
    		DESCRIPTION,
           PUBLIC_KEY, 
           PRIVATE_KEY
    };
   
    public SSHKeyTableModel( Worksheet worksheet ) {
    	super( worksheet, columnNames, columnKeys );
    }

}

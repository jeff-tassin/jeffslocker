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
    
    private static String[] columnKeys = {  LockerConstants.SERVICE, 
    		LockerConstants.USER_NAME,
    		LockerConstants.PASSWORD,
    		LockerConstants.DESCRIPTION };

    
    
    public PasswordTableModel( Worksheet worksheet ) {
    	super(worksheet, columnNames, columnKeys );
    }
   
    public boolean isPasswordColumn(int col) {
    	return col == 2;
    }
}

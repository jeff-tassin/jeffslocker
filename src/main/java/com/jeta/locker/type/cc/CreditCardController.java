package com.jeta.locker.type.cc;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.json.JSONObject;

import com.jeta.locker.common.LockerConstants;
import com.jeta.locker.common.LockerUtils;
import com.jeta.locker.type.key.KeyConstants;
import com.jeta.open.gui.framework.JETAController;


public class CreditCardController extends JETAController {

	public CreditCardController(CreditCardView view) {
		super(view);
		assignAction( CreditCardConstants.ID_ADD_ACCOUNT, evt -> { addCCAccount(); } );
	}
	
	public CreditCardTableModel getAccountsModel() {
		CreditCardView view = (CreditCardView)getView();
		JTable table = view.getTable( KeyConstants.ID_KEY_TABLE );
		return (CreditCardTableModel)table.getModel();
	}
	
	public void addCCAccount() {
		JSONObject acct = new JSONObject();
		acct.put( LockerConstants.ID, LockerUtils.generateId() );
		getAccountsModel().addRow( acct );
	}

	
	public void deleteAccount() {
		int result = JOptionPane.showConfirmDialog(null,"Delete selected account?", "Confirm", JOptionPane.YES_NO_OPTION);
		if ( result == JOptionPane.YES_OPTION ) {
			
		}
	}
}

package com.jeta.locker.type.cc;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.json.JSONObject;

import com.jeta.locker.common.LockerConstants;
import com.jeta.locker.common.LockerUtils;
import com.jeta.locker.type.key.SSHKeyConstants;
import com.jeta.open.gui.framework.JETAController;


public class CreditCardController extends JETAController {

	public CreditCardController(CreditCardView view) {
		super(view);
		assignAction( CreditCardConstants.ID_ADD_ACCOUNT, evt -> addAccount() );
		assignAction( CreditCardConstants.ID_SHOW_CC, evt-> showNumber() );
		assignAction( CreditCardConstants.ID_SHOW_CVC, evt-> showCVC() );
		assignAction( CreditCardConstants.ID_SHOW_PIN, evt-> showPin() );
	}
	
	public CreditCardTableModel getAccountsModel() {
		CreditCardView view = (CreditCardView)getView();
		JTable table = view.getTable( SSHKeyConstants.ID_KEY_TABLE );
		return (CreditCardTableModel)table.getModel();
	}
	
	
	public void showNumber() {
		CreditCardView view = (CreditCardView)getView();
		view.showNumber( view.isSelected(CreditCardConstants.ID_SHOW_CC) );
	}
	public void showCVC() {
		CreditCardView view = (CreditCardView)getView();
		view.showCVC( view.isSelected(CreditCardConstants.ID_SHOW_CVC) );
	}
	public void showPin() {
		CreditCardView view = (CreditCardView)getView();
		view.showPin( view.isSelected(CreditCardConstants.ID_SHOW_PIN) );
	}
	
	public void addAccount() {
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

package com.jeta.locker.type.cc;

import javax.swing.JOptionPane;

import com.jeta.open.gui.framework.JETAController;


public class CreditCardController extends JETAController {

	public CreditCardController(CreditCardView view) {
		super(view);
		assignAction( CreditCardConstants.ID_ADD_ACCOUNT, evt -> { createAccount(); } );
	}
	
	
	public void createAccount() {

	}

	public void editAccount() {
		
	}

	public void deleteAccount() {
		int result = JOptionPane.showConfirmDialog(null,"Delete selected account?", "Confirm", JOptionPane.YES_NO_OPTION);
		if ( result == JOptionPane.YES_OPTION ) {
			
		}
	}
}

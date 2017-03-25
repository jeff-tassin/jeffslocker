package com.jeta.locker.type.password;

import static com.jeta.locker.common.LockerConstants.DESCRIPTION;
import static com.jeta.locker.common.LockerConstants.PRIVATE_KEY;
import static com.jeta.locker.common.LockerConstants.PUBLIC_KEY;
import static com.jeta.locker.common.LockerConstants.SERVICE;
import static com.jeta.locker.type.password.PasswordConstants.ID_DESCRIPTION;
import static com.jeta.locker.type.password.PasswordConstants.ID_PASSWORD;
import static com.jeta.locker.type.password.PasswordConstants.ID_SERVICE_NAME;
import static com.jeta.locker.type.password.PasswordConstants.ID_USER_NAME;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.JSONObject;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.common.LockerConstants;
import com.jeta.locker.common.LockerUtils;
import com.jeta.locker.common.LogUtils;
import com.jeta.locker.type.key.SSHKeyConstants;

import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.utils.JETAToolbox;

public class PasswordController  extends JETAController {

	public PasswordController(PasswordAccountsView view) {
		super(view);
		assignAction( PasswordConstants.ID_GENERATE_PASSWORD, evt -> generatePassword() );
		assignAction( PasswordConstants.ID_ADD_ACCOUNT, evt -> addPasswordAccount() );
		assignAction( PasswordConstants.ID_SHOW_PASSWORDS, evt -> showPasswords() );
		assignAction( PasswordConstants.ID_EDIT_ACCOUNT, evt-> editAccount() );
		assignAction( PasswordConstants.ID_DELETE_ACCOUNT, evt-> deleteAccount() );
		assignListener( PasswordConstants.ID_ACCOUNTS_TABLE, new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        	//	uiChanged();
        	}
        });
	}
	
	public PasswordTableModel getAccountsModel() {
		PasswordAccountsView view = (PasswordAccountsView)getView();
		JTable table = view.getTable();
		return (PasswordTableModel)table.getModel();
	}
	
	
	public void editAccount() {
		PasswordAccountsView view = (PasswordAccountsView)getView();
		JSONObject json = view.getSelectedAccount();
		if ( json != null ) {
			PasswordEditView editView = new PasswordEditView(json);
			editView.setPreferredSize( new java.awt.Dimension(500,350));
			JETADialog dlg = JETAToolbox.invokeDialog( editView, null, "Edit Password Account", null );
			if (dlg.isOk()) {
				json.put(LockerConstants.SERVICE, editView.getText( ID_SERVICE_NAME ) );
				json.put(LockerConstants.USER_NAME, editView.getText( ID_USER_NAME ) );
			  	json.put(LockerConstants.PASSWORD, editView.getText( ID_PASSWORD ) ); 
			  	json.put(LockerConstants.DESCRIPTION, editView.getText( ID_DESCRIPTION )); 
				getAccountsModel().setModified(true);
			}
		}
	}
	
	public void showPasswords() {
		PasswordAccountsView view = (PasswordAccountsView)getView();
		view.showPasswords( view.isSelected( PasswordConstants.ID_SHOW_PASSWORDS ));
	}
	
	public void addPasswordAccount() {
		JSONObject acct = new JSONObject();
		acct.put( LockerConstants.ID, LockerUtils.generateId() );
		getAccountsModel().addRow( acct );
	}
	
	public void generatePassword() {
		PasswordGeneratorView view = new PasswordGeneratorView();
		JETADialog dlg = JETAToolbox.invokeDialog( view, null, "Generate Password", null );
		if (dlg.isOk()) {
			StringSelection stringSelection = new StringSelection(view.getPassword());
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
		}			
	}
	
	public void deleteAccount() {
		int result = JOptionPane.showConfirmDialog(null,"Delete selected account?", "Confirm", JOptionPane.YES_NO_OPTION);
		if ( result == JOptionPane.YES_OPTION ) {
			PasswordAccountsView view = (PasswordAccountsView)getView();
			JTable table = view.getTable();
			int row = table.convertRowIndexToModel( table.getSelectedRow() );
			((PasswordTableModel)table.getModel()).deleteRow(row);
		}
	}
	
	public class AddAction implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt ) {

		}
	}

	public class EditKeyAction implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			FormPanel panel = new FormPanel( "key.jfrm" );		
			panel.setPreferredSize( new java.awt.Dimension(450,500));
			JETADialog dlg = JETAToolbox.invokeDialog( panel, null, "Edit Key", panel.getTextField(SSHKeyConstants.ID_DESCRIPTION) );
			if (dlg.isOk()) {
			}
		}
	}
	
	
}

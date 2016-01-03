package com.jeta.locker.type.password;

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
import com.jeta.locker.type.key.KeyConstants;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.utils.JETAToolbox;

public class PasswordController  extends JETAController {

	public PasswordController(PasswordView view) {
		super(view);
		assignAction( PasswordConstants.ID_GENERATE_PASSWORD, evt -> { generatePassword(); } );
		assignAction( PasswordConstants.ID_ADD_ACCOUNT, evt -> { addPasswordAccount(); } );
		
		assignListener( PasswordConstants.ID_ACCOUNTS_TABLE, new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        	//	uiChanged();
        	}
        });
	}
	
	public PasswordTableModel getAccountsModel() {
		PasswordView view = (PasswordView)getView();
		JTable table = view.getTable( KeyConstants.ID_KEY_TABLE );
		return (PasswordTableModel)table.getModel();
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
	
	
	public class AddAction implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt ) {

		}
	}

	public class EditKeyAction implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			FormPanel panel = new FormPanel( "key.jfrm" );		
			panel.setPreferredSize( new java.awt.Dimension(450,500));
			JETADialog dlg = JETAToolbox.invokeDialog( panel, null, "Edit Key", panel.getTextField(KeyConstants.ID_DESCRIPTION) );
			if (dlg.isOk()) {
			}
		}
	}
	
	public class DeleteAction implements ActionListener {
		public void actionPerformed( ActionEvent evt ) {
	 		int result = JOptionPane.showConfirmDialog(null,"Delete selected account?", "Confirm", JOptionPane.YES_NO_OPTION);
			if ( result == JOptionPane.YES_OPTION ) {
				//int row = m_table.convertRowIndexToModel( m_table.getSelectedRow() );
				//m_model.deleteRow( row );
			}
		}
	}
}

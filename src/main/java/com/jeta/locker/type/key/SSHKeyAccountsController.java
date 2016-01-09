package com.jeta.locker.type.key;

import static com.jeta.locker.common.LockerConstants.DESCRIPTION;
import static com.jeta.locker.common.LockerConstants.PRIVATE_KEY;
import static com.jeta.locker.common.LockerConstants.PUBLIC_KEY;
import static com.jeta.locker.common.LockerConstants.SERVICE;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.json.JSONObject;

import com.jeta.locker.common.LockerConstants;
import com.jeta.locker.common.LockerUtils;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.utils.JETAToolbox;


public class SSHKeyAccountsController  extends JETAController {

	public SSHKeyAccountsController(SSHKeyAccountsView view) {
		super(view);
		assignAction(SSHKeyConstants.ID_ADD_KEY, evt -> { addAction(); } );
		assignAction(SSHKeyConstants.ID_EDIT_KEY, evt -> { editAction(); } );
		assignAction(SSHKeyConstants.ID_DELETE_KEY, evt -> { deleteAction(); } );
	}
	
	public SSHKeyTableModel getAccountsModel() {
		SSHKeyAccountsView view = (SSHKeyAccountsView)getView();
		JTable table = view.getTable( SSHKeyConstants.ID_KEY_TABLE );
		return (SSHKeyTableModel)table.getModel();
	}
	
	public void addAction() {
		JSONObject acct = new JSONObject();
		acct.put( LockerConstants.ID, LockerUtils.generateId() );
		getAccountsModel().addRow( acct );
	}

	public void editAction() {
		SSHKeyAccountsView acctsView = (SSHKeyAccountsView)getView();
		JSONObject json = acctsView.getSelectedAccount();
		if ( json != null ) {
			SSHKeyEditView editView = new SSHKeyEditView(json);
			editView.setPreferredSize( new java.awt.Dimension(450,500));
			JETADialog dlg = JETAToolbox.invokeDialog( editView, null, "Edit Key", editView.getTextField(SSHKeyConstants.ID_DESCRIPTION) );
			if (dlg.isOk()) {
				json.put(SERVICE, editView.getText(SSHKeyConstants.ID_SERVICE) ); 
				json.put(DESCRIPTION, editView.getText(SSHKeyConstants.ID_DESCRIPTION) );
				json.put(PUBLIC_KEY, editView.getText(SSHKeyConstants.ID_PUBLIC_KEY ) );
				json.put(PRIVATE_KEY, editView.getText(SSHKeyConstants.ID_PRIVATE_KEY) );
				getAccountsModel().fireTableDataChanged();
			}
		}
	}
	
	public void deleteAction() {
		int result = JOptionPane.showConfirmDialog(null,"Delete selected account?", "Confirm", JOptionPane.YES_NO_OPTION);
		if ( result == JOptionPane.YES_OPTION ) {
			SSHKeyAccountsView view = (SSHKeyAccountsView)getView();
			JTable table = view.getTable( SSHKeyConstants.ID_KEY_TABLE );
			int row = table.convertRowIndexToModel( table.getSelectedRow() );
			((SSHKeyTableModel)table.getModel()).deleteRow( row );
		}
	}
}

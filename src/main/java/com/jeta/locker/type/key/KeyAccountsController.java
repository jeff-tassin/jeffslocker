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


public class KeyAccountsController  extends JETAController {

	public KeyAccountsController(KeyAccountsView view) {
		super(view);
		assignAction(KeyConstants.ID_ADD_KEY, evt -> { addAction(); } );
		assignAction(KeyConstants.ID_EDIT_KEY, evt -> { editAction(); } );
		assignAction(KeyConstants.ID_DELETE_KEY, evt -> { deleteAction(); } );
	}
	
	public KeyTableModel getAccountsModel() {
		KeyAccountsView view = (KeyAccountsView)getView();
		JTable table = view.getTable( KeyConstants.ID_KEY_TABLE );
		return (KeyTableModel)table.getModel();
	}
	
	public void addAction() {
		JSONObject acct = new JSONObject();
		acct.put( LockerConstants.ID, LockerUtils.generateId() );
		getAccountsModel().addRow( acct );
	}

	public void editAction() {
		KeyAccountsView acctsView = (KeyAccountsView)getView();
		JSONObject json = acctsView.getSelectedAccount();
		if ( json != null ) {
			KeyEditView editView = new KeyEditView(json);
			editView.setPreferredSize( new java.awt.Dimension(450,500));
			JETADialog dlg = JETAToolbox.invokeDialog( editView, null, "Edit Key", editView.getTextField(KeyConstants.ID_DESCRIPTION) );
			if (dlg.isOk()) {
				json.put(SERVICE, editView.getText(KeyConstants.ID_SERVICE) ); 
				json.put(DESCRIPTION, editView.getText(KeyConstants.ID_DESCRIPTION) );
				json.put(PUBLIC_KEY, editView.getText(KeyConstants.ID_PUBLIC_KEY ) );
				json.put(PRIVATE_KEY, editView.getText(KeyConstants.ID_PRIVATE_KEY) );
				getAccountsModel().fireTableDataChanged();
			}
		}
	}
	
	public void deleteAction() {
		int result = JOptionPane.showConfirmDialog(null,"Delete selected account?", "Confirm", JOptionPane.YES_NO_OPTION);
		if ( result == JOptionPane.YES_OPTION ) {
			KeyAccountsView view = (KeyAccountsView)getView();
			JTable table = view.getTable( KeyConstants.ID_KEY_TABLE );
			int row = table.convertRowIndexToModel( table.getSelectedRow() );
			((KeyTableModel)table.getModel()).deleteRow( row );
		}
	}
}

package com.jeta.locker.type.key;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.json.JSONObject;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.common.LockerConstants;
import com.jeta.locker.common.LockerUtils;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.utils.JETAToolbox;



public class KeyController  extends JETAController {

	public KeyController(KeyView view) {
		super(view);
		assignAction(KeyConstants.ID_ADD_KEY, new AddAction() );
		assignAction(KeyConstants.ID_EDIT_KEY, new EditKeyAction());
		assignAction(KeyConstants.ID_DELETE_KEY, new DeleteAction());
	}
	
	public class AddAction implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt ) {
			JSONObject acct = new JSONObject();
			acct.put( LockerConstants.ID, LockerUtils.generateId() );
			KeyView view = (KeyView)getView();
			JTable table = view.getTable( KeyConstants.ID_KEY_TABLE );
			((KeyTableModel)table.getModel()).addRow( acct );
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
				KeyView view = (KeyView)getView();
				JTable table = view.getTable( KeyConstants.ID_KEY_TABLE );
				int row = table.convertRowIndexToModel( table.getSelectedRow() );
				((KeyTableModel)table.getModel()).deleteRow( row );
			}
		}
	}
}

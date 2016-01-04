package com.jeta.locker.main;


import static com.jeta.locker.common.LockerConstants.CREDIT_CARD_TYPE;
import static com.jeta.locker.common.LockerConstants.KEY_TYPE;
import static com.jeta.locker.common.LockerConstants.PASSWORD_TYPE;

import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.common.LockerUtils;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.utils.JETAToolbox;

public class LockerController extends JETAController {
	
	public LockerController(LockerView view) {
		super(view);
		assignAction( LockerViewConstants.ID_ABOUT_LOCKER, evt -> aboutLocker() );
		assignAction( LockerViewConstants.ID_SAVE, evt -> saveLocker() );
		assignAction( LockerViewConstants.ID_ADD_WORKSHEET, evt -> addWorksheet() );
	}
	
	public void aboutLocker() {
		JETADialog dlg = (JETADialog) JETAToolbox.createDialog(JETADialog.class,null, true);
		AboutView view = new AboutView();
		dlg.setPrimaryPanel(view);
		dlg.setTitle( "About" );
		dlg.pack();
		dlg.showOkButton(false);
		dlg.setCloseText("Close");
		dlg.showCenter();
	}

	public void saveLocker() {
		try {
			((LockerView)getView()).getModel().save();
			((LockerView)getView()).enableComponent( LockerViewConstants.ID_SAVE, false );
		} catch( Exception e ) {
			JOptionPane.showMessageDialog(null,  "Error: " + e.getLocalizedMessage());
		}
	}

	public void addWorksheet() {
		int type = invokeWorksheetDialog();
		if ( type >= 0 ) {
			LockerView view = (LockerView)getView();
			JTabbedPane tabs = view.getTabbedPane( LockerViewConstants.ID_WORKSHEET_TABS );
			String name =  "Worksheet " + (tabs.getTabCount() + 1);
			Worksheet worksheet = new Worksheet( LockerUtils.generateId(), name, type);
			view.addTab(worksheet); 
			view.getModel().addWorksheet( worksheet );
			tabs.setSelectedIndex( tabs.getTabCount() - 1 );
		}
	}
		
	
	// Generation Start
	public static final String ID_PASSWORD = "password";  //javax.swing.JRadioButton
	public static final String ID_CC = "cc";  //javax.swing.JRadioButton
	public static final String ID_SSH_KEY = "ssh.key";  //javax.swing.JRadioButton
	private int invokeWorksheetDialog() {
		FormPanel form = new FormPanel( "newWorksheet.jfrm" );
		form.setPreferredSize(new Dimension(300,180));
		JETADialog dlg = JETAToolbox.invokeDialog( form,  null, "New Worksheet");
		if ( dlg.isOk() ) {
			if ( form.isSelected(ID_PASSWORD)) {
				return PASSWORD_TYPE;
			} else if ( form.isSelected( ID_CC ) ) {
				return CREDIT_CARD_TYPE;
			} else if ( form.isSelected( ID_SSH_KEY ) ) {
				return KEY_TYPE;
			}
		}
		return -1;
	}
	

	
	
}

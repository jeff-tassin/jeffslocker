package com.jeta.locker.preferences;


import java.awt.Dimension;
import java.awt.Frame;

import com.jeta.open.gui.framework.JETADialog;


public class PreferencesDialog extends JETADialog {
	
	public PreferencesDialog(Frame owner) {
		super(owner, true);
		BackupView view = new BackupView();
		view.setPreferredSize( new Dimension( 620, 360 ) );
		setPrimaryPanel(view);
		setTitle( "Preferences" );
		pack();
		this.addDialogListener( (BackupViewController)view.getController() );
	}
}

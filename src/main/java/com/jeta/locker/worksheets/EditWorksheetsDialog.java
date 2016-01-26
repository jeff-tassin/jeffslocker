package com.jeta.locker.worksheets;



import java.awt.Dimension;

import com.jeta.locker.main.LockerMain;
import com.jeta.open.gui.framework.JETADialog;

public class EditWorksheetsDialog extends JETADialog {

	public EditWorksheetsDialog() {
		super( LockerMain.getFrame(), true );
		setTitle( "Edit Worksheets" );
		EditWorksheetsView view = new EditWorksheetsView( LockerMain.getCurrentModel().getWorksheets() );
		this.setPrimaryPanel(view);
		view.setPreferredSize( new Dimension(500,300));
		pack();
	}

}

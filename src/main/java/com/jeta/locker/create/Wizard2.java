package com.jeta.locker.create;

import java.awt.BorderLayout;


import com.jeta.forms.components.panel.FormPanel;
import com.jeta.open.gui.framework.JETAPanel;
import static com.jeta.locker.create.CreateWizardConstants.*;


public class Wizard2 extends JETAPanel {

	public Wizard2() {
		setLayout( new BorderLayout() );
		add( new FormPanel( "wizard2.jfrm" ), BorderLayout.CENTER );
		setErrorMessage("");
	}
	
	public void setErrorMessage( String msg ) {
		setText( ID_ERROR, msg );
	}
}

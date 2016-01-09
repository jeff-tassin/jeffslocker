package com.jeta.locker.create;

import java.awt.BorderLayout; 

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.common.LockerUtils;
import com.jeta.open.gui.framework.JETAPanel;

import static com.jeta.locker.create.CreateWizardConstants.*;

public class Wizard1 extends JETAPanel {

	public Wizard1() {
		setLayout( new BorderLayout() );
		add( new FormPanel( "wizard1.jfrm" ), BorderLayout.CENTER );
		setText( ID_SALT, LockerUtils.generateRandomCharacters(32) );
		setText( ID_IV, LockerUtils.generateRandomCharacters(16) );
		setText( ID_PEPPER, LockerUtils.generateRandomCharacters(16) );
	}
}

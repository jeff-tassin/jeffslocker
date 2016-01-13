package com.jeta.locker.type.password;

import java.awt.BorderLayout; 

import com.jeta.locker.common.LockerConstants;
import static com.jeta.locker.type.password.PasswordConstants.*;

import org.json.JSONObject;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.open.gui.framework.JETAPanel;

public class PasswordEditView extends JETAPanel {

	public PasswordEditView() {
		FormPanel panel = new FormPanel( "passwordEdit.jfrm" );		
		setLayout( new BorderLayout() );
		add( panel, BorderLayout.CENTER );
	}

	public PasswordEditView(JSONObject json) {
		this();
	  	getTextField( ID_SERVICE_NAME ).setText( json.optString(LockerConstants.SERVICE) );
	  	getTextField( ID_USER_NAME ).setText( json.optString(LockerConstants.USER_NAME) );
	  	getTextField( ID_PASSWORD ).setText( json.optString(LockerConstants.PASSWORD) );
	  	getTextComponent( ID_DESCRIPTION ).setText( json.optString(LockerConstants.DESCRIPTION) );
		getTextComponent( ID_DESCRIPTION ).setCaretPosition(0);
	}

}

package com.jeta.locker.type.key;

import java.awt.BorderLayout;

import static com.jeta.locker.common.LockerConstants.*;

import org.json.JSONObject;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.open.gui.framework.JETAPanel;

public class SSHKeyEditView extends JETAPanel {

	public SSHKeyEditView() {
		FormPanel panel = new FormPanel( "editKey.jfrm" );		
		setLayout( new BorderLayout() );
		add( panel, BorderLayout.CENTER );
	}

	public SSHKeyEditView(JSONObject json) {
		this();
		getTextField(SSHKeyConstants.ID_SERVICE).setText( json.optString(SERVICE));
		getTextField(SSHKeyConstants.ID_DESCRIPTION).setText( json.optString(DESCRIPTION));
		getTextComponent(SSHKeyConstants.ID_PUBLIC_KEY).setText( json.optString(PUBLIC_KEY));
		getTextComponent(SSHKeyConstants.ID_PRIVATE_KEY).setText( json.optString(PRIVATE_KEY));
		getTextComponent(SSHKeyConstants.ID_PUBLIC_KEY).setCaretPosition(0);
		getTextComponent(SSHKeyConstants.ID_PRIVATE_KEY).setCaretPosition(0);
	}

}

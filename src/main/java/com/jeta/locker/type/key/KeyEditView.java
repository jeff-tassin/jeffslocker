package com.jeta.locker.type.key;

import java.awt.BorderLayout;

import static com.jeta.locker.common.LockerConstants.*;

import org.json.JSONObject;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.open.gui.framework.JETAPanel;

public class KeyEditView extends JETAPanel {

	public KeyEditView() {
		FormPanel panel = new FormPanel( "editKey.jfrm" );		
		setLayout( new BorderLayout() );
		add( panel, BorderLayout.CENTER );
	}

	public KeyEditView(JSONObject json) {
		this();
		getTextField(KeyConstants.ID_SERVICE).setText( json.optString(SERVICE));
		getTextField(KeyConstants.ID_DESCRIPTION).setText( json.optString(DESCRIPTION));
		getTextComponent(KeyConstants.ID_PUBLIC_KEY).setText( json.optString(PUBLIC_KEY));
		getTextComponent(KeyConstants.ID_PRIVATE_KEY).setText( json.optString(PRIVATE_KEY));
	}

}

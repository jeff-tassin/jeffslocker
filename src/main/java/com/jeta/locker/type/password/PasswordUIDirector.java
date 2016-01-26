package com.jeta.locker.type.password;


import org.json.JSONObject;

import com.jeta.open.gui.framework.UIDirector;

public class PasswordUIDirector implements UIDirector {
	/**
	 * The view we are updating.
	 */
	private PasswordAccountsView  m_view; 

	/**
	 * ctor
	 */
	public PasswordUIDirector(PasswordAccountsView view) {
		m_view = view;
	}

	/**
	 * Updates the components in the view
	 */
	public void updateComponents(java.util.EventObject evt) {
		JSONObject json = m_view.getSelectedAccount();
		m_view.enableComponent( PasswordConstants.ID_DELETE_ACCOUNT, json != null );
		m_view.enableComponent( PasswordConstants.ID_EDIT_ACCOUNT, json != null );
	}
}
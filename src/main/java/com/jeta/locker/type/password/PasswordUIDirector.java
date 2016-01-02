package com.jeta.locker.type.password;


import com.jeta.open.gui.framework.UIDirector;

public class PasswordUIDirector implements UIDirector {
	/**
	 * The view we are updating.
	 */
	private PasswordView  m_view; 

	/**
	 * ctor
	 */
	public PasswordUIDirector(PasswordView view) {
		m_view = view;
	}

	/**
	 * Updates the components in the view
	 */
	public void updateComponents(java.util.EventObject evt) {
		// m_deleteBtn.setEnabled( m_table.getSelectedRow() >= 0 );
	}
}
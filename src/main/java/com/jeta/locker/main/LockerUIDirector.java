package com.jeta.locker.main;

import com.jeta.open.gui.framework.UIDirector;

public class LockerUIDirector implements UIDirector {
	/**
	 * The view we are updating.
	 */
	private LockerView  m_view; 

	/**
	 * ctor
	 */
	public LockerUIDirector(LockerView view) {
		m_view = view;
	}

	/**
	 * Updates the components in the view
	 */
	public void updateComponents(java.util.EventObject evt) {
	
		
	}
}

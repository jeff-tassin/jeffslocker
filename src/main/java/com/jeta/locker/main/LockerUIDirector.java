package com.jeta.locker.main;

import com.jeta.open.gui.framework.UIDirector;
import static com.jeta.locker.main.LockerViewConstants.*; 


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
		LockerModel locker = m_view.getModel();
		if ( locker == null ) {
			m_view.enableComponent( ID_ADD_WORKSHEET, false );
			m_view.enableComponent( ID_EDIT_WORKSHEET, false );
			m_view.enableComponent( ID_DELETE_WORKSHEET, false );
			m_view.enableComponent( ID_SAVE_WORKSHEET, false );
		} else {
			m_view.enableComponent( LockerViewConstants.ID_SAVE_WORKSHEET, locker.isModified() );
		}
	}
}

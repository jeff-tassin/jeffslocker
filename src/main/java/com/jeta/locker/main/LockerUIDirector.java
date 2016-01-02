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
		/*
		boolean has_project = (m_frame.getProject() != null);

		m_frame.enableComponent(MainFrameNames.ID_CREATE_FORM, has_project);
		m_frame.enableComponent(MainFrameNames.ID_OPEN_FORM, has_project);
		m_frame.enableComponent(MainFrameNames.ID_PROJECT_SETTINGS, has_project);
		m_frame.enableComponent(MainFrameNames.ID_CREATE_PROJECT, !has_project);
		m_frame.enableComponent(MainFrameNames.ID_CLOSE_PROJECT, has_project);
		m_frame.enableComponent(MainFrameNames.ID_OPEN_PROJECT, !has_project);

		boolean has_editor = (m_frame.getCurrentEditor() != null);

		m_frame.enableComponent(MainFrameNames.ID_SHOW_FORM, has_editor);
		m_frame.enableComponent(MainFrameNames.ID_SAVE_FORM, has_editor);
		m_frame.enableComponent(MainFrameNames.ID_SAVE_FORM_AS, has_editor);
		m_frame.enableComponent(MainFrameNames.ID_CLOSE_FORM, has_editor);
		m_frame.enableComponent(TSComponentNames.ID_UNDO, has_editor);
		m_frame.enableComponent(TSComponentNames.ID_REDO, has_editor);

		m_frame.enableComponent(MainFrameNames.ID_FORWARD_ENGINEER, has_editor);

		boolean can_focus = has_editor;

		FormEditor editor = m_frame.getCurrentEditor();

		GridComponent sel_gc = null;
		boolean cell_selected = false;
		if (editor != null) {
			sel_gc = editor.getSelectedComponent();
			cell_selected = (sel_gc != null);
		}

		m_frame.enableComponent(FormEditorNames.ID_EXPORT_COMPONENT_NAMES, sel_gc instanceof FormComponent);

		m_frame.enableComponent(TSComponentNames.ID_CUT, cell_selected);
		m_frame.enableComponent(TSComponentNames.ID_COPY, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_COLUMN_PREFERRED_SIZE, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_ROW_PREFERRED_SIZE, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_INSERT_ROW_ABOVE, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_INSERT_ROW_BELOW, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_INSERT_COLUMN_LEFT, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_INSERT_COLUMN_RIGHT, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_DELETE_COLUMN, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_DELETE_ROW, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_SHOW_GRID, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_SET_AS_COLUMN_SEPARATOR, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_SET_AS_BIG_COLUMN_SEPARATOR, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_SET_AS_ROW_SEPARATOR, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_SET_AS_BIG_ROW_SEPARATOR, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_TRIM_ROWS, cell_selected);
		m_frame.enableComponent(FormEditorNames.ID_TRIM_COLUMNS, cell_selected);

		if (cell_selected) {
			GridView view = sel_gc.getParentView();
			if (view != null) {
				FormSpec spec = view.getColumnSpec(sel_gc.getColumn());
				m_frame.enableComponent(FormEditorNames.ID_COLUMN_RESIZE_GROW, spec.getResizeWeight() == FormSpec.NO_GROW);
				m_frame.enableComponent(FormEditorNames.ID_COLUMN_RESIZE_NONE, spec.getResizeWeight() != FormSpec.NO_GROW);

				spec = view.getRowSpec(sel_gc.getRow());
				m_frame.enableComponent(FormEditorNames.ID_ROW_RESIZE_GROW, spec.getResizeWeight() == FormSpec.NO_GROW);
				m_frame.enableComponent(FormEditorNames.ID_ROW_RESIZE_NONE, spec.getResizeWeight() != FormSpec.NO_GROW);
			}
		}
		else {
			m_frame.enableComponent(FormEditorNames.ID_COLUMN_RESIZE_GROW, false);
			m_frame.enableComponent(FormEditorNames.ID_COLUMN_RESIZE_NONE, false);
			m_frame.enableComponent(FormEditorNames.ID_ROW_RESIZE_GROW, false);
			m_frame.enableComponent(FormEditorNames.ID_ROW_RESIZE_NONE, false);
		}

		boolean can_span = false;
		if (sel_gc != null) {
			Component comp = sel_gc.getBeanDelegate();
			if (comp != null)
				can_span = true;
		}

		m_frame.enableComponent(FormEditorNames.ID_COLUMN_INCREASE_SPAN, can_span);
		m_frame.enableComponent(FormEditorNames.ID_COLUMN_DECREASE_SPAN, can_span);
		m_frame.enableComponent(FormEditorNames.ID_ROW_INCREASE_SPAN, can_span);
		m_frame.enableComponent(FormEditorNames.ID_ROW_DECREASE_SPAN, can_span);

		JETAPanel panel = (JETAPanel) m_frame.getCurrentControlsView();
		if (panel != null) {
			com.jeta.open.gui.framework.UIDirector uidirector = panel.getUIDirector();
			if (uidirector != null)
				uidirector.updateComponents(evt);
		}

		if (cell_selected) {
			try {
				Toolkit kit = Toolkit.getDefaultToolkit();
				Clipboard clipboard = kit.getSystemClipboard();
				Transferable transferable = clipboard.getContents(null);
				boolean enable_paste = FormObjectFlavor.isDesignerFlavorSupported(transferable);
				m_frame.enableComponent(TSComponentNames.ID_PASTE, enable_paste);
				m_frame.enableComponent(FormEditorNames.ID_PASTE_SPECIAL, enable_paste);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			m_frame.enableComponent(TSComponentNames.ID_PASTE, false);
			m_frame.enableComponent(FormEditorNames.ID_PASTE_SPECIAL, false);
		}
		*/
	}
}

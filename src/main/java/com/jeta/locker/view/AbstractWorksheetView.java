package com.jeta.locker.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.json.JSONObject;

import com.jeta.locker.main.Worksheet;
import com.jeta.locker.model.AbstractWorksheetModel;
import com.jeta.open.gui.framework.JETAPanel;

public abstract class AbstractWorksheetView extends JETAPanel {
	private AbstractWorksheetModel m_model;
	
	protected AbstractWorksheetView(AbstractWorksheetModel model){
		m_model = model;
	}
	public AbstractWorksheetModel getModel() {
		return m_model;
	}
	
	public Worksheet getWorksheet() {
		return m_model.getWorksheet();
	}
	
	protected void initializeTable(JTable table ) {
		table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setRowHeight(26);
        table.setGridColor( new Color(225,225,225) );
        Font font = table.getFont();
        if (font.getSize() < 14 ) {
        	table.setFont( new Font( font.getFamily(), Font.PLAIN, 14 ) );
        }

	}
	public JPopupMenu createContextMenu() {
		JMenuItem copy = new JMenuItem("Copy");
		JPopupMenu menu = new JPopupMenu();
		menu.add(copy);
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = getTable();
				int row = table.convertRowIndexToModel(table.getSelectedRow());
				int col = table.convertColumnIndexToModel(table.getSelectedColumn());
				String sval = String.valueOf(m_model.getValueAt(row, col));
				StringSelection stringSelection = new StringSelection(sval);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
		});
		return menu;
	}
	

	public JSONObject getSelectedAccount() {
		int row = getTable().getSelectedRow();
		if ( row >= 0 ) {
			row = getTable().convertRowIndexToModel(row);
		}
		return row >=0 ? m_model.getAccount(row) : null;
	}
	public abstract JTable getTable();
	    
}

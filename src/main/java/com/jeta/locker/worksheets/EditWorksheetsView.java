package com.jeta.locker.worksheets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.main.Worksheet;
import com.jeta.open.gui.framework.JETAPanel;

public class EditWorksheetsView extends JETAPanel {
	
	// Generation Start
	public static final String ID_WORKSHEETS_TABLE = "worksheets.table";  //javax.swing.JTable
	public static final String ID_MOVE_UP = "move.up";  //javax.swing.JButton
	public static final String ID_MOVE_DOWN = "move.down";  //javax.swing.JButton
	// Generation End

	
	public EditWorksheetsView( List<Worksheet> worksheets ) {
		setLayout( new BorderLayout() );
		add( new FormPanel( "editWorksheets.jfrm" ));

		JTable table = getTable( ID_WORKSHEETS_TABLE );

		table.setModel( new EditWorksheetsModel(worksheets) );
		table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(45);
        
        table.setGridColor( new Color(225,225,225) );
        Font font = table.getFont();
        if (font.getSize() < 14 ) {
        	table.setFont( new Font( font.getFamily(), Font.PLAIN, 14 ) );
        }

	}

}

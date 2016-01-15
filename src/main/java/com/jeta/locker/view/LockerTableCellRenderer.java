package com.jeta.locker.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LockerTableCellRenderer extends DefaultTableCellRenderer {
	private static Color EVEN_COLOR = new Color(245,245,245);
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
        
		int srow = table.getSelectedRow();
		int scol = table.getSelectedColumn();
		if ( table.isRowSelected(row)) {
			if ( srow == row && scol == column ) {
				setForeground(Color.white);
				setBackground( new Color(63,81,181));
			}  else {
				setForeground(table.getSelectionForeground());
				setBackground( table.getSelectionBackground());  // 202, 202, 202
			}
		} else {
			setForeground( Color.black );
	    	if ( row % 2 == 0 ) {
	        	setBackground(Color.white);
	        } else {
	        	setBackground( EVEN_COLOR );
	        }
		}
		setBorder(noFocusBorder);
        return this;
    }
};

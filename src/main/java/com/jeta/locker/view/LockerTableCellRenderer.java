package com.jeta.locker.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LockerTableCellRenderer extends DefaultTableCellRenderer {
	private static Color EVEN_COLOR = new Color(245,245,245);
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	if ( row % 2 == 0 ) {
        	setBackground(Color.white);
        } else {
        	setBackground( EVEN_COLOR );
        }
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
        return this;
    }
};

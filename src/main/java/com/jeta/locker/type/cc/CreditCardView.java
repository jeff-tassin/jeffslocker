package com.jeta.locker.type.cc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;


import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.main.Worksheet;
import com.jeta.locker.type.password.PasswordConstants;
import com.jeta.locker.view.AbstractWorksheetView;
import com.jeta.locker.view.LockerTableCellRenderer;
import com.jeta.open.gui.framework.JETAPanel;


public class CreditCardView extends AbstractWorksheetView {
 
	private CreditCardTableModel m_model;
	private JTable m_table;
	private boolean m_showCC=true;
 
    public CreditCardView( CreditCardTableModel model ) {
        super(model);
        setLayout(new BorderLayout());
         add( new FormPanel( "creditCardAccounts.jfrm"), BorderLayout.CENTER );
         m_model = model;
        m_table = getTable( PasswordConstants.ID_ACCOUNTS_TABLE );
        m_table.setModel(model);
        initializeTable(m_table);
     
        m_table.setComponentPopupMenu( createContextMenu() );
        m_table.setDefaultRenderer( String.class, new CCRenderer() );
        setController( new CreditCardController(this));
    }
    
    public JTable getTable() {
    	return m_table;
    }
        
    private class CCRenderer extends LockerTableCellRenderer {
        private String formatAccountNumber(Object value) {
        	if ( !m_showCC ) {
        		return "*******************";
        	}
        	String sval = String.valueOf(value);
        	if ( sval.length() == 16 ){
        		// visa
        		return sval.substring(0, 4) + "  " + sval.substring(4, 8) + "  " + sval.substring(8, 12) + "  " + sval.substring(12, 16);
        	} else {
        		return sval;
        	}
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int col) {
        	if ( value == null ) {
        		value = "";
        	}
        	if ( m_model.isAccountNumberCol(col) ) {
        		value = formatAccountNumber(value);
        	} 
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, col);
            return this;
        }
    };



}
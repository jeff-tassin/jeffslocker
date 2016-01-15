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
import com.jeta.locker.model.ImmutableTableEvent;
import com.jeta.locker.type.password.PasswordConstants;
import com.jeta.locker.view.AbstractWorksheetView;
import com.jeta.locker.view.LockerTableCellRenderer;
import com.jeta.open.gui.framework.JETAPanel;


public class CreditCardAccountsView extends AbstractWorksheetView {
 
	private CreditCardTableModel m_model;
	private JTable m_table;
	private boolean m_showNumber = false;
	private boolean m_showCVC = false;
	private boolean m_showPin = false;
 
    public CreditCardAccountsView( CreditCardTableModel model ) {
        super(model);
        setLayout(new BorderLayout());
         add( new FormPanel( "creditCardAccounts.jfrm"), BorderLayout.CENTER );
         m_model = model;
        m_table = getTable( PasswordConstants.ID_ACCOUNTS_TABLE );
        m_table.setModel(model);
        initializeTable(m_table);
     
        m_table.setComponentPopupMenu( createContextMenu() );
        m_table.setDefaultRenderer( String.class, new CreditCardRenderer() );
        setController( new CreditCardController(this));
    }
    
    public JTable getTable() {
    	return m_table;
    }
    
    public void showNumber( boolean show ) {
    	m_showNumber = show;
    	m_model.fireTableChanged( new ImmutableTableEvent(getModel()) );
    }
    public void showCVC( boolean show ) {
    	m_showCVC = show;
    	m_model.fireTableChanged( new ImmutableTableEvent(getModel()) );
    }
    public void showPin( boolean show ) {
    	m_showPin = show;
    	m_model.fireTableChanged( new ImmutableTableEvent(getModel()) );
    }
        
    private class CreditCardRenderer extends LockerTableCellRenderer {
        private String formatValue(Object value, boolean show) {
        	String sval = value == null ? "" : String.valueOf( value );
        	if ( !show && sval.length() > 0 ) {
        		return "*******************";
        	}
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
        		value = formatValue(value, m_showNumber);
        	} else if ( m_model.isPinCol(col) ) {
        		value = formatValue(value, m_showPin);
        	} else if ( m_model.isCVCCol(col) ) {
        		value = formatValue(value, m_showCVC);
        	} 
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, col);
            return this;
        }
    };



}
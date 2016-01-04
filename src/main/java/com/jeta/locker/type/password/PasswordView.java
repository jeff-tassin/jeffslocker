package com.jeta.locker.type.password;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;


import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import com.jeta.forms.components.panel.FormPanel;

import com.jeta.locker.main.Worksheet;
import com.jeta.locker.model.ImmutableTableEvent;
import com.jeta.locker.view.AbstractWorksheetView;
import com.jeta.locker.view.LockerTableCellRenderer;
import com.jeta.open.gui.framework.JETAPanel;


public class PasswordView extends AbstractWorksheetView {
 
	private PasswordTableModel m_model;
	private JTable m_table;
	private boolean m_showPasswords = false;
 
    public PasswordView( PasswordTableModel model ) {
        super(model);
		setLayout( new BorderLayout() );
        add( new FormPanel("passwordAccounts.jfrm"), BorderLayout.CENTER );
        m_table = getTable(PasswordConstants.ID_ACCOUNTS_TABLE);
        m_table.setModel(model);
        m_model = model;
        initializeTable(m_table);
      
        m_table.setComponentPopupMenu( createContextMenu() );
        
        DefaultTableCellRenderer cellRenderer = new LockerTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            	if ( m_model.isPasswordColumn(column) && value != null ) {
            		value = m_showPasswords ? value : "*******************";
            	}  
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
                return this;
            }
        };
    
        m_table.setDefaultRenderer( String.class, cellRenderer);
        setUIDirector( new PasswordUIDirector(this));
        setController( new PasswordController(this));
    }
    
    public void showPasswords( boolean show ) {
    	m_showPasswords = show;
    	m_model.fireTableChanged( new ImmutableTableEvent(getModel()) );
    }
        
    public JTable getTable() {
    	return m_table;
    }
   
}
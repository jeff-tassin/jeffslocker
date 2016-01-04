package com.jeta.locker.type.key;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.json.JSONObject;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.common.LockerConstants;
import com.jeta.locker.common.LockerUtils;
import com.jeta.locker.main.Worksheet;
import com.jeta.locker.view.AbstractWorksheetView;
import com.jeta.locker.view.LockerTableCellRenderer;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.framework.JETAPanel;


public class KeyAccountsView extends AbstractWorksheetView {
 
	private KeyTableModel m_model;
	private JTable m_table;
	
    public KeyAccountsView( KeyTableModel model ) {
        super(model);
		setLayout( new BorderLayout() );
        add( new FormPanel("keyAccounts.jfrm"), BorderLayout.CENTER );
        m_model = model;
        m_table = getTable( KeyConstants.ID_KEY_TABLE );
        
        m_model = model;
        m_table.setModel( model ); 
        initializeTable( m_table );
         
        ListSelectionModel selectionModel = m_table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        		//uiChanged();
        	}
        });
     
        m_table.setComponentPopupMenu( createContextMenu() );
        m_table.setDefaultRenderer( String.class, new LockerTableCellRenderer() );
        setController( new KeyAccountsController(this));
    }
   
    public JTable getTable() {
    	return m_table;
    }
 
   
}
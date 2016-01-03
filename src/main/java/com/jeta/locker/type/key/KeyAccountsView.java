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
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.framework.JETAPanel;


public class KeyAccountsView extends JETAPanel {
 
	private KeyTableModel m_model;
	private JTable m_table;
	
    public KeyAccountsView( KeyTableModel model ) {
        super(new BorderLayout());
 
        add( new FormPanel("keyAccounts.jfrm"), BorderLayout.CENTER );
        m_table = getTable( KeyConstants.ID_KEY_TABLE );
        
        m_model = model;
        m_table.setModel( model ); 
        m_table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        m_table.setFillsViewportHeight(true);
        m_table.setAutoCreateRowSorter(true);
        m_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_table.setCellSelectionEnabled(true);
        m_table.setRowHeight(26); 
        m_table.setGridColor( new Color(225,225,255) );
         
        ListSelectionModel selectionModel = m_table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        		//uiChanged();
        	}
        });
     
        m_table.setComponentPopupMenu( createContextMenu() );
        Font font = m_table.getFont();
        if (font.getSize() < 14 ) {
        	m_table.setFont( new Font( font.getFamily(), Font.PLAIN, 14 ) );
        }
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            Border padding = BorderFactory.createEmptyBorder(0, 5, 0, 5);
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
            	
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
                setBorder(BorderFactory.createCompoundBorder(getBorder(), padding));
                return this;
            }
        };
    
        m_table.setDefaultRenderer( String.class, cellRenderer);
        setController( new KeyAccountsController(this));
    }
    

    private JPopupMenu createContextMenu() {
        JMenuItem copy = new JMenuItem("Copy");
        JPopupMenu menu = new JPopupMenu();
        menu.add(copy);
        copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = m_table.convertRowIndexToModel(m_table.getSelectedRow());
				int col = m_table.convertColumnIndexToModel(m_table.getSelectedColumn());
				String sval = String.valueOf(m_model.getValueAt(row, col));
				StringSelection stringSelection = new StringSelection(sval);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
        });
        return menu;
    }
    
    public KeyTableModel getModel() {
    	return m_model;
    }
 
    public Worksheet getWorksheet() {
    	return m_model.getWorksheet();
    }
    
    private JETAPanel createToolbar() {
    	FormPanel panel = new FormPanel( "keyToolbar.jfrm" );	
    	return panel;
    }

    public JSONObject getSelectedAccount() {
    	int row = m_table.convertRowIndexToModel( m_table.getSelectedRow() );
    	return m_model.getAccount( row );
    }

   
    public void uiChanged() {
     //   m_deleteBtn.setEnabled( m_table.getSelectedRow() >= 0 );
     //   m_editBtn.setEnabled( m_table.getSelectedRow() >= 0 );
    }
 
   
}
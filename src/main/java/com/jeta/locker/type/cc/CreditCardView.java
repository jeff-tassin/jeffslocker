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
import com.jeta.open.gui.framework.JETAPanel;


public class CreditCardView extends JETAPanel {
 
	private CreditCardTableModel m_model;
	private JTable m_table;
	private boolean m_showCC=true;
 
    public CreditCardView( CreditCardTableModel model ) {
        super(new BorderLayout());
 
        add( new FormPanel( "creditCardAccounts.jfrm"), BorderLayout.CENTER );
        m_table = getTable( PasswordConstants.ID_ACCOUNTS_TABLE );
        m_model = model;
        m_table.setModel(model);
        m_table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        m_table.setFillsViewportHeight(true);
        m_table.setAutoCreateRowSorter(true);
        m_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_table.setCellSelectionEnabled(true);
        m_table.setRowHeight(26); 
        
        /*
        ListSelectionModel selectionModel = m_table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        	}
        });
        */
     
        m_table.setComponentPopupMenu( createContextMenu() );
        Font font = m_table.getFont();
        if (font.getSize() < 14 ) {
        	m_table.setFont( new Font( font.getFamily(), Font.PLAIN, 14 ) );
        }
   
    
        m_table.setDefaultRenderer( String.class, new CCRenderer() );
        setController( new CreditCardController(this));
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
    
    public CreditCardTableModel getModel() {
    	return m_model;
    }
 
    public Worksheet getWorksheet() {
    	return m_model.getWorksheet();
    }

    private class CCRenderer extends DefaultTableCellRenderer{
    	private Color evenColor = new Color(245,245,245);
        private Border padding = BorderFactory.createEmptyBorder(0, 5, 0, 5);
        
        
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
        	if ( row % 2 == 0 ) {
            	this.setBackground(Color.white);
            } else {
            	this.setBackground(evenColor);
            }
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, col);
            return this;
        }
    };



}
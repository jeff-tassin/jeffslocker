package com.jeta.locker.type.password;

import java.awt.BorderLayout; 
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import com.jeta.locker.common.LockerConstants;
import com.jeta.locker.common.LockerUtils;
import com.jeta.locker.main.Worksheet;


public class PasswordView extends JPanel {
 
	private PasswordTableModel m_model;
	private JTable m_table;
	private JButton m_addBtn;
	private JButton m_deleteBtn;
	private boolean m_showPasswords = false;
 
    public PasswordView( PasswordTableModel model ) {
        super(new BorderLayout());
 
        m_model = model;
        m_table = new JTable(model);
        m_table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        m_table.setFillsViewportHeight(true);
        m_table.setAutoCreateRowSorter(true);
        m_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_table.setCellSelectionEnabled(true);
        m_table.setRowHeight(26);
        
        
        ListSelectionModel selectionModel = m_table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        		uiChanged();
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
            	
            	if ( m_model.isPasswordColumn(column) && value != null ) {
            		value = m_showPasswords ? value : "*******************";
            	}
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
                setBorder(BorderFactory.createCompoundBorder(getBorder(), padding));
                return this;
            }
        };
    
        m_table.setDefaultRenderer( String.class, cellRenderer);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(m_table);
 
        //Set up renderer and editor for the Favorite Color column.
 
        //Add the scroll pane to this panel.
        add(createToolbar(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void showPasswords( boolean show ) {
    	m_showPasswords = show;
    	m_model.fireTableDataChanged();
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
    
    public PasswordTableModel getModel() {
    	return m_model;
    }
 
    public Worksheet getWorksheet() {
    	return m_model.getWorksheet();
    }
   
    private JPanel createToolbar() {
    	try {
    		JPanel panel = new JPanel();
    		panel.setLayout( new FlowLayout(FlowLayout.LEFT));
    		panel.add( new JLabel("Accounts:"));
    		m_addBtn = new JButton("Add"); // + (plus)
    		panel.add( m_addBtn );
    		m_addBtn.addActionListener(new ActionListener() {

    			@Override
    			public void actionPerformed(ActionEvent e) {
    				// TODO Auto-generated method stub
    				JSONObject acct = new JSONObject();
    				acct.put( LockerConstants.ID, LockerUtils.generateId() );
    				m_model.addRow( acct );
    			}
    		});

    		m_deleteBtn = new JButton("Delete"); // (trash)
    		panel.add( m_deleteBtn );
    		m_deleteBtn.addActionListener(new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {

    				// TODO Auto-generated method stub
    				int result = JOptionPane.showConfirmDialog(null,"Delete selected account?", "Confirm", JOptionPane.YES_NO_OPTION);
    				if ( result == JOptionPane.YES_OPTION ) {
    					int row = m_table.convertRowIndexToModel( m_table.getSelectedRow() );
    					m_model.deleteRow( row );
    				}
    			}
    		});
    		m_deleteBtn.setEnabled(false);

    		final JCheckBox cbox = new JCheckBox("Show Passwords");
    		cbox.addActionListener( new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				showPasswords( cbox.isSelected() );
    			}
    		});
    		panel.add( cbox );

    		return panel;
    	} catch (Exception e ) {
    		e.printStackTrace();
    	}
    	return new JPanel();

    }
    
    public void uiChanged() {
        m_deleteBtn.setEnabled( m_table.getSelectedRow() >= 0 );
    }
 
   
}
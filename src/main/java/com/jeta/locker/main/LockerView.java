package com.jeta.locker.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jeta.locker.common.LockerUtils;
import com.jeta.locker.type.cc.CreditCardTableModel;
import com.jeta.locker.type.cc.CreditCardView;
import com.jeta.locker.type.key.KeyTableModel;
import com.jeta.locker.type.key.KeyView;
import com.jeta.locker.type.password.PasswordView;
import com.jeta.locker.type.password.PasswordTableModel;

import static com.jeta.locker.common.LockerConstants.*; 


public class LockerView extends JPanel {
	private JTabbedPane m_tabs;
	private JButton m_addBtn;
	private JButton m_deleteBtn;
	private LockerModel m_model;
 
    public LockerView( LockerModel model ) {
        super(new BorderLayout());
        m_model = model;
        m_tabs = new JTabbedPane();
        TabTitleEditListener listener = new TabTitleEditListener(m_tabs);
        m_tabs.addChangeListener(listener);
        m_tabs.addMouseListener(listener);
        add(createToolbar(), BorderLayout.NORTH);
        add( m_tabs, BorderLayout.CENTER);
        for( Worksheet worksheet : model.getWorksheets() ) {
        	addTab( worksheet );
        }
    }
 
    private void addTab( Worksheet worksheet ) {
    	if ( worksheet.getType() == PASSWORD_TYPE ) {
    		PasswordTableModel model = new PasswordTableModel( worksheet );
    		m_tabs.add( worksheet.getName(), new PasswordView( model ) );
    	} else if ( worksheet.getType() == CREDIT_CARD_TYPE ) {
    		CreditCardTableModel model = new CreditCardTableModel( worksheet );
    		m_tabs.add( worksheet.getName(), new CreditCardView( model ) );
    	} else if( worksheet.getType() == KEY_TYPE ) {
    		KeyTableModel model = new KeyTableModel( worksheet );
    		m_tabs.add( worksheet.getName(), new KeyView( model ) );
    	}
    }
   
    private JPanel createToolbar() {
        try {
            JPanel panel = new JPanel();
            panel.add( new JLabel("Worksheets:"));
            
            JButton saveBtn = new JButton( "Save" );
            panel.add( saveBtn );
            saveBtn.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					try {
						m_model.save();
					} catch( Exception e ) {
						JOptionPane.showMessageDialog(null,  "Error: " + e.getLocalizedMessage());
					}
				}
            });
            
            
            m_addBtn = new JButton("Create"); 
            panel.add( m_addBtn );
            m_addBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int type = promptForWorksheetType();
					if ( type >= 0 ) {
						String name =  "Worksheet " + (m_tabs.getTabCount() + 1);
						Worksheet worksheet = new Worksheet( LockerUtils.generateId(), name, type);
						addTab(worksheet); 
						m_model.addWorksheet( worksheet );
					}
				}
            });
            
            
            m_deleteBtn = new JButton("Remove"); // (trash)
            panel.add( m_deleteBtn );
            m_deleteBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				}
            });
            m_deleteBtn.setEnabled(false);
            
        
            return panel;
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return new JPanel();
    }
    
    private int promptForWorksheetType() {
    	JPanel panel = new JPanel();
    	ButtonGroup group = new ButtonGroup();
    	BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS );
    	panel.setLayout( layout );
    	JRadioButton cbox1 = new JRadioButton("Password");
    	panel.add(cbox1);
    	group.add( cbox1 );
    	JRadioButton cbox2 = new JRadioButton( "Credit Card" );
    	panel.add(cbox2);
    	group.add( cbox2 );
    	JRadioButton cbox3 = new JRadioButton( "Key" );
    	panel.add(cbox3);
    	group.add( cbox3 );
    	
    	String[] options = new String[]{"OK", "Cancel"};
    	int option = JOptionPane.showOptionDialog(null, panel, "Enter worksheet type",
    			JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
    			null, options, options[0]);
    	if(option == 0) {
    		if ( cbox1.isSelected() ) {
    			return PASSWORD_TYPE;
    		} else if ( cbox2.isSelected() ) {
    			return CREDIT_CARD_TYPE;
    		} else if ( cbox3.isSelected() ) {
    			return KEY_TYPE;
    		}
    	} else {
    		return -1;
    	}
    	return -1;
    }
    
    private Worksheet getWorksheetAt( int tabIndex ) {
    	Object lview = m_tabs.getComponentAt( tabIndex );
    	if ( lview instanceof PasswordView ) {
    		PasswordView view = (PasswordView)lview;
    		PasswordTableModel model = view.getModel();
    		return model.getWorksheet();
    	} else if ( lview instanceof CreditCardView ) {
    		CreditCardView view = (CreditCardView)lview;
    		CreditCardTableModel model = view.getModel();
    		return model.getWorksheet();
    	} else if ( lview instanceof KeyView ) {
    		KeyView view = (KeyView)lview;
    		KeyTableModel model = view.getModel();
    		return model.getWorksheet();
    	}
    	return null;
    }
    
    private class TabTitleEditListener extends MouseAdapter implements ChangeListener {
        private final JTextField editor = new JTextField();
        private final JTabbedPane tabbedPane;
        private int editingIdx = -1;
        private int len = -1;
        private Dimension dim;
        private Component tabComponent;

        public TabTitleEditListener(final JTabbedPane tabbedPane) {
            super();
            this.tabbedPane = tabbedPane;
            editor.setBorder(BorderFactory.createEmptyBorder());
            editor.addFocusListener(new FocusAdapter() {
                @Override 
                public void focusLost(FocusEvent e) {
                    renameTabTitle();
                }
            });
            editor.addKeyListener(new KeyAdapter() {
                @Override 
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        renameTabTitle();
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        cancelEditing();
                    } else {
                        editor.setPreferredSize(editor.getText().length() > len ? null : dim);
                        tabbedPane.revalidate();
                    }
                }
            });
            tabbedPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "start-editing");
            tabbedPane.getActionMap().put("start-editing", new AbstractAction() {
                @Override 
                public void actionPerformed(ActionEvent e) {
                    startEditing();
                }
            });
        }
       
        @Override 
        public void mouseClicked(MouseEvent me) {
            Rectangle rect = tabbedPane.getUI().getTabBounds(tabbedPane, tabbedPane.getSelectedIndex());
            if (rect.contains(me.getPoint()) && me.getClickCount() == 2) {
                startEditing();
            } else {
                renameTabTitle();
            }
        }
        private void startEditing() {
            editingIdx = tabbedPane.getSelectedIndex();
            tabComponent = tabbedPane.getTabComponentAt(editingIdx);
            tabbedPane.setTabComponentAt(editingIdx, editor);
            editor.setVisible(true);
            editor.setText(tabbedPane.getTitleAt(editingIdx));
            editor.selectAll();
            editor.requestFocusInWindow();
            len = editor.getText().length();
            dim = editor.getPreferredSize();
            editor.setMinimumSize(dim);
        }
        private void cancelEditing() {
            if (editingIdx >= 0) {
                tabbedPane.setTabComponentAt(editingIdx, tabComponent);
                editor.setVisible(false);
                editingIdx = -1;
                len = -1;
                tabComponent = null;
                editor.setPreferredSize(null);
                tabbedPane.requestFocusInWindow();
            }
        }
        private void renameTabTitle() {
            String title = editor.getText().trim();
            if (editingIdx >= 0 && !title.isEmpty()) {
                tabbedPane.setTitleAt(editingIdx, title);
                Worksheet worksheet = getWorksheetAt(editingIdx);
                worksheet.setName(title);
            }
            cancelEditing();
        }

		@Override
		public void stateChanged(ChangeEvent e) {
			renameTabTitle();
		}

	
    }

 
}

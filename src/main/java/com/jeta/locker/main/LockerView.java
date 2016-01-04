package com.jeta.locker.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.model.AbstractWorksheetModel;
import com.jeta.locker.type.cc.CreditCardTableModel;
import com.jeta.locker.type.cc.CreditCardView;
import com.jeta.locker.type.key.KeyTableModel;
import com.jeta.locker.type.key.KeyAccountsView;
import com.jeta.locker.type.password.PasswordView;
import com.jeta.locker.view.AbstractWorksheetView;
import com.jeta.open.gui.framework.JETAPanel;
import com.jeta.locker.type.password.PasswordTableModel;

import static com.jeta.locker.common.LockerConstants.*; 


public class LockerView extends JETAPanel {
	private JTabbedPane m_tabs;
	private LockerModel m_model;
 
    public LockerView( LockerModel model ) {
        super(new BorderLayout());
        m_model = model;
        add( new FormPanel( "lockerMain.jfrm" ));
        m_tabs = getTabbedPane( LockerViewConstants.ID_WORKSHEET_TABS );
        TabTitleEditListener listener = new TabTitleEditListener(m_tabs);
        m_tabs.addChangeListener(listener);
        m_tabs.addMouseListener(listener);
        for( Worksheet worksheet : model.getWorksheets() ) {
        	addTab( worksheet );
        }
        setController( new LockerController(this) );
        setText( LockerViewConstants.ID_MODIFIED, "");
    }
    
    public LockerModel getModel() {
    	return m_model;
    }
 
    private void addTab( Worksheet worksheet ) {
    	if ( worksheet.getType() == PASSWORD_TYPE ) {
    		PasswordTableModel model = new PasswordTableModel( worksheet );
    		m_tabs.add( worksheet.getName(), new PasswordView( model ) );
    		model.addTableModelListener( evt -> { tableChanged(evt); } );
    	} else if ( worksheet.getType() == CREDIT_CARD_TYPE ) {
    		CreditCardTableModel model = new CreditCardTableModel( worksheet );
    		m_tabs.add( worksheet.getName(), new CreditCardView( model ) );
    		model.addTableModelListener( evt -> { tableChanged(evt); } );
    	} else if( worksheet.getType() == KEY_TYPE ) {
    		KeyTableModel model = new KeyTableModel( worksheet );
    		m_tabs.add( worksheet.getName(), new KeyAccountsView( model ) );
    		model.addTableModelListener( evt -> { tableChanged(evt); } );
    	}
    }
    
    private void tableChanged(TableModelEvent evt) {
    	AbstractWorksheetModel model = (AbstractWorksheetModel)evt.getSource();
    	model.getWorksheet().setModified(true);
        setText( LockerViewConstants.ID_MODIFIED, "Modified");
    }
  
   
    private Worksheet getWorksheetAt( int tabIndex ) {
    	Object lview = m_tabs.getComponentAt( tabIndex );
    	if ( lview instanceof AbstractWorksheetView ) {
    		AbstractWorksheetView view = (AbstractWorksheetView)lview;
    		return view.getModel().getWorksheet();
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

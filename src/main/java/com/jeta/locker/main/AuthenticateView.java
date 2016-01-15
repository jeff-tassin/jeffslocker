package com.jeta.locker.main;


import static com.jeta.locker.create.CreateWizardConstants.ID_ERROR;
import static com.jeta.locker.create.CreateWizardConstants.ID_FILENAME;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.EventObject;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.StringUtils;
import com.jeta.locker.config.AppProperties;
import com.jeta.locker.create.CreateWizardDialog;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETAPanel;
import com.jeta.open.gui.framework.UIDirector;


public class AuthenticateView extends JETAPanel {
	
	// Generation Start
	public static final String ID_CREATE_NEW = "create.new";  //javax.swing.JButton
	public static final String ID_PASSWORD = "password";  //javax.swing.JPasswordField
	public static final String ID_SHOW_PASSWORD = "show.password";  //javax.swing.JCheckBox
	public static final String ID_RECENT_FILES = "recent.files";  //javax.swing.JComboBox
	public static final String ID_ABOUT = "about";  //javax.swing.JButton
	public static final String ID_OPEN = "open.file";  //javax.swing.JButton
	public static final String ID_CHOOSE_FILE = "choose.file";  //javax.swing.JButton
	public static final String ID_PASSWORD_MESSAGE = "password.message";
	// Generation End

	private LockerMain m_main;
	private FormPanel m_form;
	
	public AuthenticateView( LockerMain main ) {
		m_main = main;
		m_form = new FormPanel( "loginBarrier.jfrm" );
		setLayout( new BorderLayout() );
		add( m_form, BorderLayout.CENTER );
		m_form.setText(ID_PASSWORD_MESSAGE, "" );
		
		JComboBox<File> cbox = (JComboBox<File>)m_form.getComboBox(ID_RECENT_FILES);
		cbox.removeAllItems();
		for( String filePath : AppProperties.getMostRecentFiles() ) {
			File file = new File(filePath);
			if ( file.isFile() ) {
				cbox.addItem(file);
			}
		}
		cbox.setRenderer( new FilenameRenderer() );
		setController( new Controller() );
		setUIDirector( new AuthenticateViewUIDirector() );
		updateComponents(null);
	}
	
		
	public void setFocus() {
		m_form.getTextComponent(ID_PASSWORD).requestFocusInWindow();
	}
	
	public String getPassword() {
		return m_form.getText(ID_PASSWORD); 
	}

	
	private class AuthenticateViewUIDirector implements UIDirector {
		@Override
		public void updateComponents(EventObject evt) {
			int passwordLen =  StringUtils.safeTrim(getText(ID_PASSWORD)).length();
			enableComponent( ID_OPEN, passwordLen > 0 );
		}
	}

	private class Controller extends JETAController {
		Controller() {
			super(AuthenticateView.this);
			assignAction(ID_SHOW_PASSWORD, evt->showPassword());
			assignAction(ID_OPEN, evt->openLocker() );
			assignAction(ID_CREATE_NEW, evt->createNew() );
			assignAction( ID_CHOOSE_FILE, evt->chooseFile() );
			m_form.getPasswordField( ID_PASSWORD ).addKeyListener( new KeyAdapter() {             
				@Override             
				public void keyPressed(KeyEvent evt) {
					m_form.setText(ID_PASSWORD_MESSAGE, "" );
					if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) {
						openLocker();
					}
					/**
					 * call invokeLater to allow key processing to complete
					 */
					SwingUtilities.invokeLater( () -> {
							AuthenticateView.this.updateComponents(null);
						});
				}
				
			});
		}
		
		private void showPassword() {
			char echoChar = m_form.isSelected(ID_SHOW_PASSWORD) ? 0 : '*'; 
			m_form.getPasswordField(ID_PASSWORD).setEchoChar(echoChar);
		}
		
		private void openLocker() {
			try {
				File selectedFile = (File)m_form.getComboBox(ID_RECENT_FILES).getSelectedItem();
				m_main.openLocker( new LockerModel( selectedFile.getAbsolutePath(), getPassword() ) );
			} catch( LockerException e ) {
				m_form.setText(ID_PASSWORD_MESSAGE, e.getMessage() );
			}
		}
		
		private void chooseFile() {
			 JFileChooser chooser = new JFileChooser();
			 chooser.setFileFilter( new FileNameExtensionFilter("Locker files", "jlocker" ) );
			 int returnVal = chooser.showOpenDialog( (Component)getView() );
			 if(returnVal == JFileChooser.APPROVE_OPTION) {
				 File lockerFile = chooser.getSelectedFile();
				 
				 JComboBox<File> cbox = (JComboBox<File>)m_form.getComboBox(ID_RECENT_FILES);
				 for( int index=0; index < cbox.getItemCount(); index++ ) {
					 if ( cbox.getItemAt(index).equals(lockerFile) ) {
						 cbox.removeItemAt(index);
					 }
				 }
				 if ( cbox.getItemCount() == 0 ) {
					 cbox.addItem(lockerFile);
				 } else {
					 cbox.insertItemAt(lockerFile, 0);
				 }
				 cbox.setSelectedIndex(0);
			 }
		}
		private void createNew() {
			CreateWizardDialog dlg = new CreateWizardDialog(null);
			dlg.showCenter();
		}
	}
	
	public static class FilenameRenderer extends BasicComboBoxRenderer {
			private static ImageIcon m_lockIcon = null;
		static {
			try {
				m_lockIcon = new ImageIcon(ImageIO.read( ClassLoader.getSystemResource( "images/16x16/locked59.png" ) ));
			} catch( Exception e ) {
				// ignore
			}
		}
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        	super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        	if ( value == null ) {
        		return this;
        	} else {
        		if ( m_lockIcon != null ) {
        			this.setIcon(m_lockIcon);
        		}
        		File file = (File)value;
        		if ( file != null ) {
        			String fname = file.getName();
        			int pos = fname.lastIndexOf('.');
        			if ( pos > 0 ) {
        				fname = fname.substring(0, pos);
        			}
        			setText( fname );
        		}
                return this;
        	}
        }
    }

	
}

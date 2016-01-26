package com.jeta.locker.create;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.EventObject;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.common.LockerUtils;
import com.jeta.locker.common.StringUtils;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETAPanel;
import com.jeta.open.gui.framework.UIDirector;

import static com.jeta.locker.create.CreateWizardConstants.*;

public class Wizard1 extends JETAPanel {

	public Wizard1() {
		setLayout( new BorderLayout() );
		add( new FormPanel( "wizard1.jfrm" ), BorderLayout.CENTER );
		setText( ID_SALT, LockerUtils.generateRandomCharacters(32) );
		setText( ID_IV, LockerUtils.generateRandomCharacters(16) );
		setText( ID_PEPPER, LockerUtils.generateRandomCharacters(32) );
		
		if ( LockerUtils.isDebug() ) {
			setText( ID_FILENAME, "/home/tiger/test.jlocker");
			setText( ID_PASSWORD, "1234" );
			setText( ID_CONFIRM_PASSWORD, "1234" );
		}
		setController( new Wizard1Controller(this) );
		setUIDirector( new Wizard1UIDirector(this) );
		setErrorMessage("");
	}
	
	public void setErrorMessage( String msg ) {
		setText( ID_ERROR, msg );
	}
	public int getKeySize() {
		return isSelected( ID_256_BITS ) ? 256 : 128;
	}
	
	static class Wizard1UIDirector implements UIDirector {
		private Wizard1 m_view1;
		
		public Wizard1UIDirector( Wizard1 view ) {
			m_view1 = view;
		}
		
		@Override
		public void updateComponents(EventObject evt) {
			String error = ((Wizard1Controller)m_view1.getController()).validatePasswords();
			if ( error == null ) {
				m_view1.setVisible( ID_PASSWORD_ERROR_ICON, false );
			} else {
				m_view1.setToolTipText( ID_PASSWORD_ERROR_ICON, error );
				m_view1.setVisible( ID_PASSWORD_ERROR_ICON, true );
			}
		}
	}
	
	static class Wizard1Controller extends JETAController {
		private Wizard1 m_view1;
		
		public Wizard1Controller( Wizard1 view ) {
			super( view );
			m_view1 = view;
			assignAction( ID_CHOOSE_FILE, evt->chooseFile() );
			assignAction( ID_GENERATE_SALT, evt->generateSalt() );
			assignAction( ID_GENERATE_IV, evt->generateIV() );
			assignAction( ID_GENERATE_PEPPER, evt->generatePepper() );
			
			m_view1.getTextComponent( ID_FILENAME ).addKeyListener( new KeyAdapter() {
				@Override             
				public void keyPressed(KeyEvent evt) {
					m_view1.setText(ID_ERROR,"");
				}
			});
			
			m_view1.getPasswordField( ID_PASSWORD ).addKeyListener( new KeyAdapter() {             
				@Override             
				public void keyPressed(KeyEvent evt) {
					m_view1.setText(ID_ERROR,"");
					/**
					 * call invokeLater to allow key processing to complete
					 */
					  SwingUtilities.invokeLater(() -> {
						  Wizard1Controller.this.updateComponents(null);
					  });
				}
			});
			m_view1.getPasswordField( ID_CONFIRM_PASSWORD ).addKeyListener( new KeyAdapter() {             
				@Override             
				public void keyPressed(KeyEvent evt) {
					m_view1.setText(ID_ERROR,"");
					/**
					 * call invokeLater to allow key processing to complete
					 */
					  SwingUtilities.invokeLater(() -> {
						  Wizard1Controller.this.updateComponents(null);
					  });
				}
			});
		}
		
		public String validateInput() {
			String error = validateFile();
			return error == null ? validatePasswords() : error;
		}
		
		public String validatePasswords() {
			String p1 = StringUtils.safeTrim(m_view1.getText( ID_PASSWORD ));
			String p2 = StringUtils.safeTrim(m_view1.getText( ID_CONFIRM_PASSWORD ) );
			if ( p1.length() >= 4 && p1.equals(p2) ) {
				return null;
			} else {
				if ( !p1.equals(p2)) {
					return "Passwords don't match";
				} else {
					return "Password too short";
				}
			}
		}
		
		public String validateFile() {
			try {
				String path = m_view1.getText(ID_FILENAME);
				File file = new File(path);
				if ( file.isFile()) {
					return "File already exists. Please choose a new name.";
				}
				if ( !file.getParentFile().isDirectory() ) {
					return "Invalid path: " + file.getParentFile().getAbsolutePath();
				}
				return null;
			} catch( Exception e ) {
				return "Invalid file.";
			}
		}

		public void chooseFile() {
			 JFileChooser chooser = new JFileChooser();
			 int returnVal = chooser.showSaveDialog( m_view1 );
			 if(returnVal == JFileChooser.APPROVE_OPTION) {
				 File lockerFile = chooser.getSelectedFile();
				 String filePath = lockerFile.getAbsolutePath();
				 if ( !lockerFile.getName().endsWith(".jlocker") ) {
					 filePath += ".jlocker";
				 }
				 m_view1.setText( ID_FILENAME, filePath );
				 m_view1.setText( ID_ERROR, "" );
			 }
		}
		
		public void generateSalt() {
			m_view1.setText( ID_SALT, LockerUtils.generateRandomCharacters(32) );
		}
		public void generateIV() {
			m_view1.setText( ID_IV, LockerUtils.generateRandomCharacters(16) );
		}
		public void generatePepper() {
			m_view1.setText( ID_PEPPER, LockerUtils.generateRandomCharacters(32) );
		} 
	}

}
 
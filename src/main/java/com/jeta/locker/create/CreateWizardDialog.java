package com.jeta.locker.create;

import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.EventObject;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.jeta.locker.common.LockerUtils;
import com.jeta.locker.common.StringUtils;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.framework.JETADialogListener;
import com.jeta.open.gui.framework.UIDirector;

import static com.jeta.locker.create.CreateWizardConstants.*;


public class CreateWizardDialog extends JETADialog {
	private Wizard1 m_view1;
	private Wizard2 m_view2;

	public CreateWizardDialog(Frame owner) {
		super(owner, true);
		m_view1 = new Wizard1();
		setPrimaryPanel( m_view1 );
		setTitle( "Create Locker" );
		setOkText("Next");
		pack();
		m_view1.setController( new CreateLockerController() );
		m_view1.setUIDirector( new CreateLockerUIDirector() );
		
		addDialogListener( new JETADialogListener() {
			@Override
			public boolean cmdOk() {
				String error = ((CreateLockerController)m_view1.getController()).validatePasswords();
				if ( error == null ) {
					error = ((CreateLockerController)m_view1.getController()).validateFile();
				}
				if ( error == null ) {
					return true;
				} else {
					JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		});
	}
	
	
	private class CreateLockerUIDirector implements UIDirector {
		@Override
		public void updateComponents(EventObject evt) {
			String error = ((CreateLockerController)m_view1.getController()).validatePasswords();
			if ( error == null ) {
				m_view1.setVisible( ID_PASSWORD_ERROR_ICON, false );
			} else {
				m_view1.setToolTipText( ID_PASSWORD_ERROR_ICON, error );
				m_view1.setVisible( ID_PASSWORD_ERROR_ICON, true );
			}
		}
	}
	
	private class CreateLockerController extends JETAController {
		public CreateLockerController() {
			super( m_view1 );
			assignAction( ID_CHOOSE_FILE, evt->chooseFile() );
			assignAction( ID_GENERATE_SALT, evt->generateSalt() );
			assignAction( ID_GENERATE_IV, evt->generateIV() );
			assignAction( ID_GENERATE_PEPPER, evt->generatePepper() );
			
			m_view1.getPasswordField( ID_PASSWORD ).addKeyListener( new KeyAdapter() {             
				@Override             
				public void keyPressed(KeyEvent evt) {
					CreateLockerController.this.updateComponents(null);
				}
			});
			m_view1.getPasswordField( ID_CONFIRM_PASSWORD ).addKeyListener( new KeyAdapter() {             
				@Override             
				public void keyPressed(KeyEvent evt) {
					CreateLockerController.this.updateComponents(null);
				}
			});
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
			 int returnVal = chooser.showOpenDialog( m_view1 );
			 if(returnVal == JFileChooser.APPROVE_OPTION) {
				 File lockerFile = chooser.getSelectedFile();
				 String filePath = lockerFile.getAbsolutePath();
				 if ( !lockerFile.getName().endsWith(".jlocker") ) {
					 filePath += ".jlocker";
				 }
				 m_view1.setText( ID_FILENAME, filePath );
			 }
		}
		
		public void generateSalt() {
			m_view1.setText( ID_SALT, LockerUtils.generateRandomCharacters(32) );
		}
		public void generateIV() {
			m_view1.setText( ID_IV, LockerUtils.generateRandomCharacters(16) );
		}
		public void generatePepper() {
			m_view1.setText( ID_PEPPER, LockerUtils.generateRandomCharacters(16) );
		}
	}

}

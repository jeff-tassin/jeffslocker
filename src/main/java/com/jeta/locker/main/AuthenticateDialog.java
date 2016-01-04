package com.jeta.locker.main;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.common.LockerException;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.framework.JETADialogListener;
import com.jeta.open.gui.utils.JETAToolbox;


public class AuthenticateDialog extends JETADialog {
	
	// Generation Start
	public static final String ID_SHOW_PASSWORD = "show.password";  //javax.swing.JCheckBox
	public static final String ID_PASSWORD = "password";  //javax.swing.JPasswordField
	public static final String ID_CONFIGURATION = "configuration";  //javax.swing.JButton
	public static final String ID_PASSWORD_MESSAGE = "password.message";  //javax.swing.JLabel
	
	// Generation End
	private FormPanel m_form;
	private LockerModel m_model;


	public AuthenticateDialog() {
		super( (Frame)null, true );
		m_form = new FormPanel( "authenticate.jfrm" );
		setPrimaryPanel(m_form);
		setTitle("Authenticate");
		setSize(new Dimension(350,210));
		setInitialFocusComponent(m_form.getTextComponent(ID_PASSWORD));
		m_form.setText(ID_PASSWORD_MESSAGE, "" );
		
		addDialogListener( new JETADialogListener() {
			@Override
			public boolean cmdOk() {
				return validatePassword();
			}
		});
		m_form.setController(new Controller());
	}
	
	public String getPassword() {
		return m_form.getText(ID_PASSWORD); 
	}

	public LockerModel getModel() {
		return m_model;
	}
	private boolean validatePassword() {
		try {
			m_model = new LockerModel( getPassword() );
			return true;
		} catch( LockerException e ) {
			m_form.setText(ID_PASSWORD_MESSAGE, e.getMessage() );
		}
		return false;
	}
	

	private class Controller extends JETAController {
		Controller() {
			super(m_form);
			assignAction(ID_SHOW_PASSWORD, evt->showPassword());
			assignAction(ID_CONFIGURATION, evt->showAbout());
			m_form.getPasswordField( ID_PASSWORD ).addKeyListener( new KeyAdapter() {             
				@Override             
				public void keyPressed(KeyEvent evt) {
					m_form.setText(ID_PASSWORD_MESSAGE, "" );
					if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) {
						AuthenticateDialog.this.cmdOk();
					}
				}
			});
		}
		
		private void showPassword() {
			char echoChar = m_form.isSelected(ID_SHOW_PASSWORD) ? 0 : '*'; 
			m_form.getPasswordField(ID_PASSWORD).setEchoChar(echoChar);
		}
		
		private void showAbout() {
			JETADialog dlg = (JETADialog) JETAToolbox.createDialog(JETADialog.class,null, true);
			AboutView view = new AboutView();
			dlg.setPrimaryPanel(view);
			dlg.setTitle( "About" );
			dlg.pack();
			dlg.showOkButton(false);
			dlg.setCloseText("Close");
			dlg.showCenter();
		}
	}
	
}

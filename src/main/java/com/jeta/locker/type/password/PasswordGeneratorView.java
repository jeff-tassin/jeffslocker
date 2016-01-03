package com.jeta.locker.type.password;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.common.LockerUtils;
import com.jeta.open.gui.framework.JETAContainer;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETAPanel;

public class PasswordGeneratorView extends JETAPanel {
	// Generation Start
	public static final String ID_PASSWORD = "password";  //javax.swing.JTextField
	public static final String ID_8_CHARS = "8_chars";  //javax.swing.JRadioButton
	public static final String ID_24_CHARS = "24_chars";  //javax.swing.JRadioButton
	public static final String ID_16_CHARS = "16_chars";  //javax.swing.JRadioButton
	public static final String ID_32_CHARS = "32_chars";  //javax.swing.JRadioButton
	// Generation End


	public PasswordGeneratorView() {
		setLayout( new BorderLayout() );
		FormPanel panel = new FormPanel( "passwordGenerator.jfrm" );		
		panel.setPreferredSize( new java.awt.Dimension(300,200));
		add(panel, BorderLayout.CENTER);
		setController( new Controller(this));
		getTextField(ID_PASSWORD).setText(LockerUtils.generatePassword(8));
	}
	
	public String getPassword() {
		return getTextField(ID_PASSWORD).getText();
	}
	
	public class Controller extends JETAController {

		public Controller(JETAContainer view) {
			super(view);
			assignAction( ID_8_CHARS, new PasswordSizeAction() );
			assignAction( ID_16_CHARS, new PasswordSizeAction() );
			assignAction( ID_24_CHARS, new PasswordSizeAction() );
			assignAction( ID_32_CHARS, new PasswordSizeAction() );
		}
		
	}
	
	public class PasswordSizeAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JComponent comp = (JComponent)e.getSource();
			int len = 24;
			switch( comp.getName() ) {
			case ID_8_CHARS:
				len = 8;
				break;
			case ID_16_CHARS:
				len = 16;
				break;
			case ID_24_CHARS:
				len = 24;
				break;
			case ID_32_CHARS:
				len = 32;
				break;
			default:
				len = 16;
				break;
			}
			PasswordGeneratorView.this.getTextField(ID_PASSWORD).setText(LockerUtils.generatePassword(len));
		}
		
	}
}

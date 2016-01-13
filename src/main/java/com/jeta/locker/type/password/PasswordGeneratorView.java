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
	public static final String ID_INCLUDE_SYMBOLS = "include.symbols";  //javax.swing.JCheckBox
	// Generation End


	public PasswordGeneratorView() {
		setLayout( new BorderLayout() );
		FormPanel panel = new FormPanel( "passwordGenerator.jfrm" );		
		panel.setPreferredSize( new java.awt.Dimension(300,250));
		add(panel, BorderLayout.CENTER);
		setController( new Controller(this));
		getTextField(ID_PASSWORD).setText(LockerUtils.generateRandomCharacters(8));
	}
	
	public String getPassword() {
		return getTextField(ID_PASSWORD).getText();
	}

	private int getLength() {
		if ( isSelected(ID_8_CHARS) ) {
			return 8;
		} else if ( isSelected(ID_16_CHARS ) ) {
			return 16;
		} else if ( isSelected(ID_24_CHARS ) ) {
			return 24;
		} else {
			return 32;
		}
	}
	
	public class Controller extends JETAController {
		public Controller(JETAContainer view) {
			super(view);
			assignAction( ID_8_CHARS, e -> passwordSize(8) );
			assignAction( ID_16_CHARS, e -> passwordSize(16) );
			assignAction( ID_24_CHARS, e -> passwordSize(24) );
			assignAction( ID_32_CHARS, e -> passwordSize(32) );
			assignAction( ID_INCLUDE_SYMBOLS, e->passwordSize( getLength()));
		}
		
	}
	
	public void passwordSize( int N ) {
		boolean includeSymbols = isSelected( ID_INCLUDE_SYMBOLS );
		PasswordGeneratorView.this.getTextField(ID_PASSWORD).setText(LockerUtils.generateRandomCharacters(N, includeSymbols));
	}
		
}

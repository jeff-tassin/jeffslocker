package com.jeta.locker.main;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.StringUtils;
import com.jeta.locker.config.LockerProperties;

public class LockerMain {
	
	private LockerModel m_locker;
	
	public LockerMain() throws LockerException {
		/**
		 * Fix Mac slow rendering
		 */
		System.setProperty("swing.volatileImageBufferEnabled", "false" );
		try {
			LockerProperties.initialize();
		} catch( Exception e ) {
			JOptionPane.showMessageDialog(null,  "Unable to load properties file:\n" + LockerProperties.getConfigFile() );
			System.exit(0);
		}
		
		try {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "WikiTeX");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch( Exception e) {
			// ignore
		}

		m_locker = new LockerModel( getPassword() );
		
		JFrame frame = new JFrame("My Locker");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener( new WindowController() );

		frame.getContentPane().add( createContent(), BorderLayout.CENTER);

		frame.setLocation(100, 100);
		frame.setSize( 1000,  600 );
		frame.setVisible(true);
	}
	
	private String getPassword() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Enter password:");
		JPasswordField pass = new JPasswordField(25);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[]{"OK", "Cancel"};
		int option = JOptionPane.showOptionDialog(null, panel, "Authentication Required",
		                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
		                         null, options, options[0]);
		if(option == 0) {
		    return new String(pass.getPassword());
		} else {
			System.exit(0);
		}
		return null;
	}

	private JPanel createContent() throws LockerException {
		return new LockerView( m_locker );
	}


	public static void main(String[] args ) {
		try {
			new LockerMain();
		} catch( LockerException e ) {
			JOptionPane.showMessageDialog(null,  "Error: " + e.getLocalizedMessage());
		}
	}
	
	private class WindowController extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent evt) {
			if ( m_locker.isModified() ) {
				int result = JOptionPane.showConfirmDialog(null,"Locker data has changed. Save to database?", "Confirm", JOptionPane.YES_NO_OPTION);
				if ( result == JOptionPane.YES_OPTION ) {
					try {
						m_locker.save();
					} catch( Exception e ) {
						JOptionPane.showMessageDialog(null,  "Error: " + e.getLocalizedMessage());
					}
				}
			}
		}
	}
}

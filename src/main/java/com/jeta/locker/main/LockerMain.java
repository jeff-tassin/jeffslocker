package com.jeta.locker.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import com.jeta.locker.config.LockerConfig;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.utils.JETAToolbox;

public class LockerMain {
	
	private LockerModel m_locker;
	
	public LockerMain() throws LockerException {
		try {
			LockerConfig.initialize();
		} catch( Exception e ) {
			JOptionPane.showMessageDialog(null,  "Unable to load properties file:\n" + LockerConfig.getConfigFile() );
			System.exit(0);
		}
		
		try {
			String os = StringUtils.safeToLowerCase(System.getProperty("os.name"));
			if ( os.contains("linux")) {
				// use standard look and feel for Linux
			} else {
				/**
				 * Fix Mac slow rendering
				 */
				System.setProperty("swing.volatileImageBufferEnabled", "false" );
				
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				System.setProperty("com.apple.mrj.application.apple.menu.about.name", "WikiTeX");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		} catch( Exception e) {
			// ignore
		}
		

		m_locker = authenticate();
		
		JFrame frame = new JFrame("Jeff's Locker");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener( new WindowController() );

		frame.getContentPane().add( createContent(), BorderLayout.CENTER);

		frame.setSize( 1000,  600 );
		JETAToolbox.centerWindow(frame);
		frame.setVisible(true);
	}
	
	private LockerModel authenticate() {
		AuthenticateDialog dlg = new AuthenticateDialog();
		dlg.showCenter();
		if ( dlg.isOk() ) {
			return dlg.getModel();
		} else {
			System.exit(0);
			return null;
		}
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

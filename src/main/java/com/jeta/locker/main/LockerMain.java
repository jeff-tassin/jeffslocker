package com.jeta.locker.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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
import com.jeta.locker.config.LockerKeys;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.utils.JETAToolbox;

public class LockerMain {
	private AuthenticateView m_loginBarrier;
	private JFrame m_frame;
	
	
	public LockerMain() throws LockerException {
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
		
		m_frame = new JFrame("Jeff's Locker");
		
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.addWindowListener( new WindowController() );

		m_loginBarrier = new AuthenticateView( this );
		m_frame.getContentPane().add( m_loginBarrier, BorderLayout.CENTER);
		
		m_frame.setSize( 1000,  600 );
		JETAToolbox.centerWindow( m_frame );
		m_frame.setVisible(true);
		m_loginBarrier.setFocus();
	}
	
	public void openLocker( LockerModel model ) {
		if ( m_loginBarrier != null ) {
			//m_frame.remove( m_loginBarrier );
			m_frame.getContentPane().removeAll();

			m_frame.getContentPane().add( new LockerView( this, model ), BorderLayout.CENTER);
			m_frame.getContentPane().revalidate();
			m_loginBarrier = null;
		}
	}
	
	public void showLoginBarrier() {
		m_frame.getContentPane().removeAll();
		m_loginBarrier = new AuthenticateView(this);
		m_frame.getContentPane().add( m_loginBarrier, BorderLayout.CENTER);
		m_frame.revalidate();
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
			/*
			if ( m_locker != null && m_locker.isModified() ) {
				int result = JOptionPane.showConfirmDialog(null,"Locker data has changed. Save to database?", "Confirm", JOptionPane.YES_NO_OPTION);
				if ( result == JOptionPane.YES_OPTION ) {
					try {
						m_locker.save();
					} catch( Exception e ) {
						JOptionPane.showMessageDialog(null,  "Error: " + e.getLocalizedMessage());
					}
				}
			}
			*/
		}
	}
}

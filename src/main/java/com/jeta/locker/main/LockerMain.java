package com.jeta.locker.main;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.StringUtils;
import com.jeta.locker.view.AbstractWorksheetView;
import com.jeta.open.gui.utils.JETAToolbox;

public class LockerMain {
	private AuthenticateView m_loginBarrier;
	private static LockerView       m_view;
	private static JFrame m_frame;
	
	
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
	
	public static JFrame getFrame() {
		return m_frame;
	}
	
	public static LockerModel getCurrentModel() {
		return m_view.getModel();
	}
	
	public void openLocker( LockerModel model ) {
		if ( m_loginBarrier != null ) {
			m_frame.getContentPane().removeAll();
			m_view =  new LockerView( this, model );
			m_frame.getContentPane().add(m_view, BorderLayout.CENTER);
			m_frame.getContentPane().revalidate();
			m_loginBarrier = null;
		}
	}
	
	public void showLoginBarrier() {
		m_frame.getContentPane().removeAll();
		m_loginBarrier = new AuthenticateView(this);
		m_frame.getContentPane().add( m_loginBarrier, BorderLayout.CENTER);
		m_frame.revalidate();
		m_view = null;
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
			if ( m_view != null ) {
				AbstractWorksheetView view = m_view.getCurrentView();
				if ( view != null ) {
					view.stopEditing();
				}
				if ( m_view.getModel().isModified() ) {
					int result = JOptionPane.showConfirmDialog( LockerMain.getFrame(),"Locker data has changed. Save to database?", "Confirm", JOptionPane.YES_NO_OPTION);
					if ( result == JOptionPane.YES_OPTION ) {
						try {
							m_view.getModel().save();
						} catch( Exception e ) {
							JOptionPane.showMessageDialog( LockerMain.getFrame(),  "Error: " + e.getLocalizedMessage());
						}
					}
				}
			}
		}
		
		@Override
		public void windowDeactivated(WindowEvent e) {
			if ( m_view != null ) {
				AbstractWorksheetView view = m_view.getCurrentView();
				if ( view != null ) {
					view.stopEditing();
				}
			}
		}
	}
}

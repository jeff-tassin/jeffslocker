package com.jeta.locker.create;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.EventObject;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;

import com.jeta.locker.common.FileUtils;
import com.jeta.locker.common.LockerUtils;
import com.jeta.locker.common.StringUtils;
import com.jeta.locker.config.AppProperties;
import com.jeta.locker.config.LockerKeys;
import com.jeta.locker.main.AuthenticateView;
import com.jeta.locker.main.LockerModel;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.framework.JETADialogListener;
import com.jeta.open.gui.framework.UIDirector;

import static com.jeta.locker.create.CreateWizardConstants.*;


public class CreateWizardDialog extends JETADialog {
	private Wizard1 m_view1;
	private Wizard2 m_view2;
	private JPanel  m_container = new JPanel(new BorderLayout());
	private static final int VIEW_1 = 1;
	private static final int VIEW_2 = 2;
	private int m_state = 0;
	private String m_id;
	private JButton m_finishBtn;
	

	public CreateWizardDialog(Frame owner) {
		super(owner, true);
		m_id = LockerUtils.generateId();
		m_view1 = new Wizard1();
		m_view2 = new Wizard2();
		
		m_view2.getCheckBox( ID_CONFIRM ).addActionListener( evt-> confirmFinish() );

		setSize( new Dimension(750,570));
		setTitle( "Create Locker" );

		JPanel btnPanel = getButtonPanel();
		btnPanel.removeAll();
		
		setPrimaryPanel( m_container );
		navigateNext();
	}

	private void confirmFinish() {
		m_finishBtn.setEnabled( m_view2.isSelected( ID_CONFIRM ));
	}
	
	private void onFinish() {
		FileOutputStream kis = null;
		
		try {
			LockerKeys keys = new LockerKeys( m_id, m_view1.getKeySize(), m_view1.getText( ID_IV ), m_view1.getText( ID_SALT ), m_view1.getText( ID_PEPPER ) ); 
			/**
			 * create key file
			 */
			System.out.println( "creating key file  " + keys.getFilePath() + "  salt: " + keys.getSalt());
			kis = new FileOutputStream( keys.getFilePath() );
			Properties keyprops= new Properties();
			keyprops.put("init.vector", keys.getInitVector() );
			keyprops.put("salt", keys.getSalt() );
			keyprops.put("pepper", keys.getPepper() );
			keyprops.store( kis, "" );
			kis.flush();
			/**
			 * create model
			 */
			LockerModel.createFile( m_view2.getText(ID_FILENAME), m_view1.getText(ID_PASSWORD), keys);
			
			cmdOk();
		} catch( Exception e ) {
			e.printStackTrace();
			m_view2.setErrorMessage( "Error: " + e.getMessage() );
		} finally { 
			FileUtils.close( kis );
		}
	}
	
	public String getFilePath() {
		return m_view2.getText(ID_FILENAME);
	}
	
	private void doPrevious() {
		m_state = VIEW_2;
		navigateNext();
	}
	
	private void doNext() {
		if ( m_state == VIEW_1 ) {
			String error = ((Wizard1.Wizard1Controller)m_view1.getController()).validateInput();
			if ( error == null ) {
				navigateNext();
			} else {
				m_view1.setErrorMessage( error );
			}
		} 
	}

	private int navigateNext() {
		if ( m_state == VIEW_1 ) {
			m_state = VIEW_2;
			m_container.removeAll();
			m_container.add(m_view2, BorderLayout.CENTER );
			m_container.revalidate();
			/**
			 * hack to fix Swing dialog issue not repainting when child views change
			 */
			setSize( getWidth(), getHeight() + 1 );
			setSize( getWidth(), getHeight() - 1 );
			m_container.repaint();
			
			m_finishBtn.setEnabled(false);
			m_view2.setSelected( ID_CONFIRM,  false );

			String filename = m_view1.getText( ID_FILENAME );
			if ( !filename.endsWith(".jlocker") ) {
				filename += ".jlocker";
			}
			m_view2.setText( ID_FILENAME, filename );
			m_view2.setText( ID_KEY_FILE, StringUtils.buildFilePath(AppProperties.getConfigDirectory(), m_id + ".jkeys" ) );
			m_view2.setErrorMessage("");
			configureButtonPanel();
		} else {
			m_view1.setErrorMessage( "" );
			m_state = VIEW_1;
			m_container.removeAll();
			m_container.add(m_view1, BorderLayout.CENTER );
			m_container.revalidate();
			/**
			 * hack to fix Swing dialog issue not repainting when child views change
			 */
			setSize( getWidth(), getHeight() + 1 );
			setSize( getWidth(), getHeight() - 1 );
			m_container.repaint();

			
			configureButtonPanel();
		}
		return m_state;
	}
	
	private void configureButtonPanel() {
		JPanel btnPanel = getButtonPanel();
		btnPanel.removeAll();
		
		JButton prevBtn = new JButton( "Prev" );
		prevBtn.addActionListener( evt-> doPrevious() );
		JButton nextBtn = new JButton( "Next" );
		nextBtn.addActionListener( evt-> doNext() );
		
		m_finishBtn = new JButton( "Finish" );
		m_finishBtn.setEnabled(false);
		m_finishBtn.addActionListener( evt -> onFinish() );
		JButton cancelBtn = new JButton( "Cancel" );
		cancelBtn.addActionListener( evt-> cmdCancel() );
		

		if ( m_state == VIEW_1 ) {
			btnPanel.add( nextBtn );
		} else if ( m_state == VIEW_2 ) {
			btnPanel.add( prevBtn );
			btnPanel.add( m_finishBtn );
		}
		btnPanel.add( cancelBtn );
	}

}

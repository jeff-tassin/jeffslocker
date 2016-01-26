package com.jeta.locker.preferences;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import com.jeta.locker.common.FileUtils;
import com.jeta.locker.common.LockerException;
import com.jeta.locker.common.StringUtils;
import com.jeta.locker.config.AppProperties;
import com.jeta.locker.main.LockerMain;
import com.jeta.locker.main.LockerModel;
import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETADialogListener;

import static com.jeta.locker.preferences.BackupView.*;

public class BackupViewController extends JETAController implements JETADialogListener {
	public BackupViewController( BackupView view ) {
		super(view);
		
		view.getTextComponent( ID_BACKUP_LOCATION ).addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				SwingUtilities.invokeLater(() -> {
					BackupViewController.this.updateComponents(null);
				});
			}
		});
		assignAction( ID_OPEN_CHOOSER, evt->showFileChooser() );
		assignAction( ID_BACKUP_NOW, evt->backupNow() );
	}

	BackupView getView2() {
		return (BackupView)getView();
	}
	
	private void backupNow() {
		try {
			LockerModel model = LockerMain.getCurrentModel();
			String target = FileUtils.backup( model.getFilePath(), getView2().getText( ID_BACKUP_LOCATION ) );
			getView2().setVisible(ID_BACKUP_MESSAGE, true );
			getView2().setText( ID_BACKUP_MESSAGE, "Successful Backup: " + target );
		} catch( LockerException e) {
			JOptionPane.showMessageDialog( LockerMain.getFrame(),  e.getMessage() );
		}
	}
	
	private void showFileChooser() {
		 JFileChooser chooser = new JFileChooser();
		 chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		 int returnVal = chooser.showOpenDialog( getView2() );
		 if(returnVal == JFileChooser.APPROVE_OPTION) {
			 File path = chooser.getSelectedFile();
			 getView2().setText( ID_BACKUP_LOCATION, path.getAbsolutePath() );
			 updateComponents(null);
		 }
	}

	@Override
	public boolean cmdOk() {
		BackupView view = getView2();
		String path = StringUtils.safeTrim( view.getText( ID_BACKUP_LOCATION ) );
		if ( new File( path ).isDirectory() ) {
			AppProperties.setProperty(ID_BACKUP_LOCATION, view.getText( ID_BACKUP_LOCATION) );
			AppProperties.setProperty(ID_AUTOMATIC_BACKUPS, view.isSelected(ID_AUTOMATIC_BACKUPS) ? "true" : "false" );
			AppProperties.setProperty( ID_BACKUP_PERIOD, String.valueOf(view.getSpinner( ID_BACKUP_PERIOD ).getValue()) );

			AppProperties.save();
			return true;
		} else {
			String errorMsg = path.length() == 0 ? "Invalid backup path" : "Invalid backup path: " + path;
			JOptionPane.showMessageDialog( LockerMain.getFrame(),  errorMsg );
		}
		return false;
	}
}

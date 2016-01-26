package com.jeta.locker.preferences;

import java.awt.BorderLayout;
import java.util.EventObject;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.locker.common.FileUtils;
import com.jeta.locker.common.StringUtils;
import com.jeta.locker.config.AppProperties;
import com.jeta.open.gui.framework.JETAPanel;
import com.jeta.open.gui.framework.UIDirector;

public class BackupView extends JETAPanel {
	// Generation Start
	public static final String ID_AUTOMATIC_BACKUPS = "automatic.backups";  //javax.swing.JCheckBox
	public static final String ID_BACKUP_NOW = "backup.now";  //javax.swing.JButton
	public static final String ID_BACKUP_PERIOD = "backup.period.days";  //javax.swing.JSpinner
	public static final String ID_BACKUP_LOCATION = "backup.location";  //javax.swing.JTextField
	public static final String ID_OPEN_CHOOSER = "open.chooser";  //javax.swing.JButton
	public static final String ID_BACKUP_MESSAGE = "backup.message";
	// Generation End
	
	public BackupView() {
		setLayout( new BorderLayout() );
		add( new FormPanel( "backups.jfrm" ), BorderLayout.CENTER );
		
		SpinnerModel model = new SpinnerNumberModel(30, 1, 120, 1 );
		getSpinner(ID_BACKUP_PERIOD).setModel(model);
		

		setController( new BackupViewController(this) );
		setUIDirector( new BackupViewUIDirector() );
		
		model.setValue( AppProperties.getIntProperty( ID_BACKUP_PERIOD, 30 ) );
		setText(ID_BACKUP_LOCATION, AppProperties.getProperty( ID_BACKUP_LOCATION));
		setSelected( ID_AUTOMATIC_BACKUPS, "true".equals(AppProperties.getProperty( ID_AUTOMATIC_BACKUPS)));
		setVisible(ID_BACKUP_MESSAGE, false );
		updateComponents(null);
	}
	
	public class BackupViewUIDirector implements UIDirector {
		@Override
		public void updateComponents(EventObject evt) {
			String path = StringUtils.safeTrim( getText( ID_BACKUP_LOCATION ) );
			enableComponent( ID_BACKUP_NOW, FileUtils.isDirectory( path ) );
		}
	}

}

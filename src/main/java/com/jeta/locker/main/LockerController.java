package com.jeta.locker.main;


import static com.jeta.locker.common.LockerConstants.CREDIT_CARD_TYPE;
import static com.jeta.locker.common.LockerConstants.KEY_TYPE;
import static com.jeta.locker.common.LockerConstants.PASSWORD_TYPE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


import com.jeta.open.gui.framework.JETAController;
import com.jeta.open.gui.framework.JETADialog;
import com.jeta.open.gui.utils.JETAToolbox;

public class LockerController extends JETAController {
	
	public LockerController(LockerView view) {
		super(view);
		assignAction( LockerViewConstants.ID_ABOUT_LOCKER, evt -> { aboutLocker(); } );
		assignAction( LockerViewConstants.ID_SAVE, evt -> { saveLocker();} );
	}
	
	public void aboutLocker() {
		JETADialog dlg = (JETADialog) JETAToolbox.createDialog(JETADialog.class,null, true);
		AboutView view = new AboutView();
		dlg.setPrimaryPanel(view);
		dlg.setTitle( "About" );
		dlg.pack();
		dlg.showOkButton(false);
		dlg.setCloseText("Close");
		dlg.showCenter();
	}

	public void saveLocker() {
		try {
			((LockerView)getView()).getModel().save();
			((LockerView)getView()).setText( LockerViewConstants.ID_MODIFIED, "");
		} catch( Exception e ) {
			JOptionPane.showMessageDialog(null,  "Error: " + e.getLocalizedMessage());
		}
	}

	public void newWorksheet() {
		int type = promptForWorksheetType();
		if ( type >= 0 ) {
			/*
				String name =  "Worksheet " + (m_tabs.getTabCount() + 1);
				Worksheet worksheet = new Worksheet( LockerUtils.generateId(), name, type);
				addTab(worksheet); 
				m_model.addWorksheet( worksheet );
			 */
		}
	}
		
		 
	private int promptForWorksheetType() {
		JPanel panel = new JPanel();
		ButtonGroup group = new ButtonGroup();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS );
		panel.setLayout( layout );
		JRadioButton cbox1 = new JRadioButton("Password");
		panel.add(cbox1);
		group.add( cbox1 );
		JRadioButton cbox2 = new JRadioButton( "Credit Card" );
		panel.add(cbox2);
		group.add( cbox2 );
		JRadioButton cbox3 = new JRadioButton( "Key" );
		panel.add(cbox3);
		group.add( cbox3 );
		
		String[] options = new String[]{"OK", "Cancel"};
		int option = JOptionPane.showOptionDialog(null, panel, "Enter worksheet type",
				JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[0]);
		if(option == 0) {
			if ( cbox1.isSelected() ) {
				return PASSWORD_TYPE;
			} else if ( cbox2.isSelected() ) {
				return CREDIT_CARD_TYPE;
			} else if ( cbox3.isSelected() ) {
				return KEY_TYPE;
			}
		} else {
			return -1;
		}
		return -1;
	}
	

	public class DeleteAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}

	
}

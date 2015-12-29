package com.jeta.locker.type.key;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class KeyDialog extends JDialog {

	public KeyDialog(JFrame parent ) {
		super(parent, "Edit Key" );
		setLocation(100,100);

		// Create a message
		JPanel content = createContentPane();

		// get content pane, which is usually the
		// Container of all the dialog's components.
		getContentPane().add( content );

		// Create a button
		JPanel buttonPane = new JPanel();
		JButton saveBtn = new JButton("Save");
		buttonPane.add( saveBtn );
		JButton cancelBtn = new JButton( "Cancel" );
		buttonPane.add( cancelBtn );

		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private JPanel createContentPane() {
		JPanel content = new JPanel( new BorderLayout() );
		JTextArea ta = new JTextArea();
		content.add( ta, BorderLayout.CENTER );
		return content;
	}
}

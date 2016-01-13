package com.jeta.locker.main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JEditorPane;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.open.gui.framework.JETAPanel;


import static com.jeta.locker.main.AboutConstants.*;

public class AboutView extends JETAPanel {

	private FormPanel m_view;

	public AboutView( LockerModel model ) {
		setLayout(new BorderLayout());
		m_view = new FormPanel("about.jfrm");
		add(m_view, BorderLayout.CENTER);
		createCreditsPanel();
		setPreferredSize( new Dimension(550,450) );
		
		setText( ID_KEY_FILE, model.getKeys().getFilePath() );
		setText( ID_SALT, model.getKeys().getSalt() );
		setText( ID_INITIALIZATION_VECTOR, model.getKeys().getInitVector() );
		setText( ID_PASSWORD_PEPPER, model.getKeys().getPepper() );
		setText( ID_DATA_FILE, model.getFilePath() );
		setText( ID_KEY_SIZE, model.getKeys().getKeySize() + " bits");
	}

	public void createCreditsPanel() {
		JEditorPane editor = (JEditorPane) m_view.getComponentByName(AboutConstants.ID_CREDITS);
		editor.setEditorKit(new javax.swing.text.html.HTMLEditorKit());
		try {
			java.net.URL url = AboutView.class.getClassLoader().getResource("credits.htm");
			editor.setPage(url);
			editor.setEditable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
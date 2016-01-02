package com.jeta.locker.main;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.open.gui.framework.JETAPanel;


public class AboutView extends JETAPanel {

	private FormPanel m_view;

	public AboutView() {
		setLayout(new BorderLayout());
		m_view = new FormPanel("about.jfrm");
		add(m_view, BorderLayout.CENTER);
		createCreditsPanel();
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
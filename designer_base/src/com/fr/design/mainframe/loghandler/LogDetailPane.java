package com.fr.design.mainframe.loghandler;

import com.fr.base.BaseUtils;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class LogDetailPane extends JPanel{
	public LogDetailPane() {
		setLayout(new BorderLayout());
		add(DesignerLogHandler.getInstance().getLogHandlerArea(), BorderLayout.CENTER);
		add(DesignerLogHandler.getInstance().getCaption(), BorderLayout.SOUTH);
	}

	public JFrame showDialog() {
		JFrame fr = new JFrame();
		fr.setSize(600, 400);
		GUICoreUtils.centerWindow(fr);
		fr.setResizable(false);
		fr.setTitle(Inter.getLocText("FR-Designer_Log"));
		fr.setIconImage(BaseUtils.readImageWithCache("com/fr/design/images/buttonicon/history.png"));
		fr.getContentPane().setLayout(new BorderLayout());
		fr.getContentPane().add(this, BorderLayout.CENTER);
		return fr;
	}

}
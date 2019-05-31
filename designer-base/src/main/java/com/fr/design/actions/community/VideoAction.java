package com.fr.design.actions.community;

import com.fr.design.actions.UpdateAction;
import com.fr.design.i18n.ActionType;
import com.fr.design.i18n.LocaleCenter;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.utils.BrowseUtils;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class VideoAction extends UpdateAction
{

	public VideoAction()
	{
		 this.setMenuKeySet(VIDEO);
	     this.setName(getMenuKeySet().getMenuName());
	     this.setMnemonic(getMenuKeySet().getMnemonic());
	     this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/bbs/video.png"));
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		BrowseUtils.browser(LocaleCenter.getInstance().getLocaleAction(GeneralContext.getLocale()).getUrls().get(ActionType.VIDEO));

	}
	  public static final MenuKeySet VIDEO = new MenuKeySet() {
	        @Override
	        public char getMnemonic() {
	            return 'V';
	        }

	        @Override
	        public String getMenuName() {
	            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Community_Video");
	        }

	        @Override
	        public KeyStroke getKeyStroke() {
	            return null;
	        }
	    };

}

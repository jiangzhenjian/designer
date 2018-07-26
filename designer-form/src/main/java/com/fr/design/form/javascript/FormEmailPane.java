package com.fr.design.form.javascript;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.javascript.EmailPane;
import com.fr.design.layout.TableLayoutHelper;


/**
 * 表单的邮件pane
 * 
 * @author jim
 *
 */
public class FormEmailPane extends EmailPane{
	
	@Override
	protected void initCenterPane(UILabel mainTextLabel, JScrollPane scrollPane, double fill, double preferred) {
		double[] rowSize = { preferred, preferred, preferred, preferred, preferred, fill, preferred};
        double[] columnSize = { preferred, fill};
        centerPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                {new UILabel(), tipsPane1},
                createLinePane(com.fr.design.i18n.Toolkit.i18nText("HJS-Mail_to"), maitoEditor = new UITextField()),
                createLinePane(com.fr.design.i18n.Toolkit.i18nText("HJS-CC_to"), ccEditor = new UITextField()),
                createLinePane(com.fr.design.i18n.Toolkit.i18nText("EmailPane-BCC"), bccEditor = new UITextField()),
                createLinePane(com.fr.design.i18n.Toolkit.i18nText("EmailPane-mailSubject"), titleEditor = new UITextField()),
                {mainTextLabel, scrollPane},
                {new UILabel(), tipsPane2}},rowSize, columnSize, 7);
	}

}
package com.fr.design.javascript;

import com.fr.base.Parameter;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.scrollruler.ModLineBorder;
import com.fr.general.ComparatorUtils;
import com.fr.js.FormSubmitJavaScript;
import com.fr.stable.ParameterProvider;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FormSubmitJavaScriptPane extends FurtherBasicBeanPane<FormSubmitJavaScript> {
	public static final String ASYNCH = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_JavaScript_Asynch");
	public static final String SYNCH = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_JavaScript_Synch");
	private UITextField urlTextField;
	private UIComboBox asynComboBox;
	private ReportletParameterViewPane pViewPane;

	private JavaScriptActionPane javaScriptActionPane;
	private UIButton addCallbackButton;
	
	public FormSubmitJavaScriptPane(JavaScriptActionPane javaScriptActionPane){
		this.javaScriptActionPane = javaScriptActionPane;
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		this.add(northPane, BorderLayout.NORTH);
		JPanel firstLine = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Submit_Url") + ":");
		firstLine.add(label);
		urlTextField = new UITextField(25);
		firstLine.add(urlTextField);
		urlTextField.setSize(new Dimension(100, 16));
		northPane.add(firstLine,BorderLayout.NORTH);
		JPanel submitPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		northPane.add(submitPane,BorderLayout.CENTER);
		submitPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Submit_Style") + ":"));
		asynComboBox = new UIComboBox(new String[]{ASYNCH, SYNCH});
		submitPane.add(asynComboBox);
		asynComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				checkVisible();
			}			
		});
		pViewPane = new ReportletParameterViewPane();
		pViewPane.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter")));
		northPane.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), com.fr.design.i18n.Toolkit.i18nText("FIne-Design_Basic_Submit")));
		
		addCallbackButton = javaScriptActionPane.createCallButton();
		
		JPanel buttonPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		buttonPane.add(addCallbackButton);
		this.add(buttonPane, BorderLayout.SOUTH);
		
		this.add(pViewPane, BorderLayout.CENTER);
	}
	
	@Override
	/**
	 *
	 */
	public String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_JavaScript_Form_Submit");
	}

	/**
	 *
	 */
	public void reset() {
		populateBean(null);
	}
	
	public void populateBean(FormSubmitJavaScript js){
		if (js == null) {
			js = new FormSubmitJavaScript();
		}
		
		urlTextField.setText(js.getAction());
		if (js.isAsynchronous()){
			asynComboBox.setSelectedItem(ASYNCH);
			this.javaScriptActionPane.setCall(js.getCallBack());
		} else {
			asynComboBox.setSelectedItem(SYNCH);
		}
		pViewPane.populate(js.getParameters());
		
		this.checkVisible();
	}
	
	public FormSubmitJavaScript updateBean(){
		FormSubmitJavaScript js = new FormSubmitJavaScript();
		js.setAction(urlTextField.getText());
		if (ComparatorUtils.equals(ASYNCH,asynComboBox.getSelectedItem())){
			js.setAsynchronous(true);
			js.setCallBack(this.javaScriptActionPane.getCall());
		} else {
			js.setAsynchronous(false);
		}
		List<ParameterProvider> list = pViewPane.update();
		js.setParameters(list.toArray(new Parameter[list.size()]));

		return js;
	}
	
	private void checkVisible(){
		// ͬ��
		if (ComparatorUtils.equals(SYNCH,asynComboBox.getSelectedItem())){
			addCallbackButton.setEnabled(false);
		} else {
			addCallbackButton.setEnabled(true);
		}
	}

	@Override
	/**
	 *
	 */
	public boolean accept(Object ob) {
		return ob instanceof FormSubmitJavaScript;
	}
}

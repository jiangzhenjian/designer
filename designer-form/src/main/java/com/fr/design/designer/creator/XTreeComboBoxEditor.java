/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.JComponent;

import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.TreeComboBoxEditor;

import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class XTreeComboBoxEditor extends XTreeEditor {
	LimpidButton btn;

    public XTreeComboBoxEditor(TreeComboBoxEditor widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            UITextField textField = new UITextField(5);
            textField.setOpaque(false);
            editor.add(textField, BorderLayout.CENTER);
			btn = new LimpidButton("", this.getIconPath(), toData().isVisible() ? FULL_OPACITY : HALF_OPACITY);
            btn.setPreferredSize(new Dimension(21, 21));
            btn.setOpaque(true);
            editor.add(btn, BorderLayout.EAST);
            editor.setBackground(Color.WHITE);
        }
        return editor;
    }

	protected CRPropertyDescriptor[] addWaterMark(CRPropertyDescriptor[] crp) throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.add(crp, new CRPropertyDescriptor("waterMark", this.data.getClass()).setI18NName(
				com.fr.design.i18n.Toolkit.i18nText("FR-Designer_WaterMark")).putKeyValue(
				XCreatorConstants.PROPERTY_CATEGORY, "Advanced"));
	}

	protected CRPropertyDescriptor[] addAllowEdit(CRPropertyDescriptor[] crp) throws IntrospectionException{
		return (CRPropertyDescriptor[])ArrayUtils.add(crp, new CRPropertyDescriptor("directEdit", this.data.getClass())
          .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Allow_Edit")).putKeyValue(
                  XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate"));
	}

	protected CRPropertyDescriptor[] addCustomData(CRPropertyDescriptor[] crp) throws IntrospectionException{
		return (CRPropertyDescriptor[])ArrayUtils.add(crp, new CRPropertyDescriptor("customData", this.data.getClass())
          .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Allow_Custom_Data")).putKeyValue(
                  XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate"));
	}

    @Override
    protected String getIconName() {
        return "comboboxtree.png";
    }

	protected void makeVisible(boolean visible) {
		btn.makeVisible(visible);
	}
	
	/**
	 * 获取当前XCreator的一个封装父容器
	 * 
	 * @param widgetName 当前组件名
	 * 
	 * @return 封装的父容器
	 * 
	 *
	 * @date 2014-11-25-下午4:47:23
	 * 
	 */
	protected XLayoutContainer getCreatorWrapper(String widgetName){
		return new XWScaleLayout();
	}
	
	/**
	 * 将当前对象添加到父容器中
	 * 
	 * @param parentPanel 父容器组件
	 * 
	 *
	 * @date 2014-11-25-下午4:57:55
	 * 
	 */
	protected void addToWrapper(XLayoutContainer parentPanel, int width, int minHeight){			
		this.setSize(width, minHeight);
		parentPanel.add(this);
	}
	
	/**
   	 * 此控件在自适应布局要保持原样高度
   	 * 
   	 * @return 是则返回true
   	 */
   	@Override
   	public boolean shouldScaleCreator() {
   		return true;
   	}
   	
}
/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.beans.IntrospectionException;

import com.fr.design.form.util.XCreatorConstants;
import com.fr.form.ui.DirectWriteEditor;

import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class XDirectWriteEditor extends XFieldEditor {

    public XDirectWriteEditor(DirectWriteEditor widget, Dimension initSize) {
         super(widget, initSize);
    }

	@Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(),
				new CRPropertyDescriptor[] { new CRPropertyDescriptor("directEdit", this.data.getClass())
								.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Allow_Edit")).putKeyValue(
								 XCreatorConstants.PROPERTY_VALIDATE, "Fine-Design_Basic_Validate") });
	}
}
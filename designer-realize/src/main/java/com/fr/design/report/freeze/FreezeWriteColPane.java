package com.fr.design.report.freeze;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.SwingConstants;

import com.fr.stable.FT;
import com.fr.stable.StableUtils;

public class FreezeWriteColPane extends FreezeAndRepeatPane {

	public FreezeWriteColPane() {

		start = new UILabel(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Frozen", "N.O."}) + " A", SwingConstants.CENTER);
		end = new ColSpinner(1,Integer.MAX_VALUE,1,1);
	    super.initComponent();
		this.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Column")));
	}



	@Override
	protected String title4PopupWindow() {
		return "FreezeColumn";
	}

	@Override
	public void populateBean(FT ob) {
		((UILabel)start).setText(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Frozen", "N.O."}) + StableUtils.convertIntToABC(ob.getFrom()));
		((ColSpinner)end).setValue((ob.getTo() + 1));
	}

	@Override
	public FT updateBean() {
		return new FT(StableUtils.convertABCToInt(((UILabel) start).getText()) - 1,(int)((ColSpinner)end).getValue() - 1);
	}

	@Override
	public String getLabeshow() {
		return com.fr.design.i18n.Toolkit.i18nText("ColumnTo");
	}
}
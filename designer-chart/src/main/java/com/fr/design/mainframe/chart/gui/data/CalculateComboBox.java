package com.fr.design.mainframe.chart.gui.data;

import com.fr.data.util.function.AbstractDataFunction;
import com.fr.data.util.function.AverageFunction;
import com.fr.data.util.function.CountFunction;
import com.fr.data.util.function.MaxFunction;
import com.fr.data.util.function.MinFunction;
import com.fr.data.util.function.NoneFunction;
import com.fr.data.util.function.SumFunction;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;


/**
 * 公式选择.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-8 上午09:52:15
 */
public class CalculateComboBox extends UIComboBox{

	public static final String[] CALCULATE_ARRAY = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Function_None"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Function_Sum"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Function_Average"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Function_Max"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Function_Min"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Function_Count")};
	public static final Class[] CLASS_ARRAY = {NoneFunction.class, SumFunction.class, AverageFunction.class, 
			MaxFunction.class, MinFunction.class, CountFunction.class};
	
	public CalculateComboBox() {
		super(CALCULATE_ARRAY);
		setSelectedIndex(0);
	}
	
	public void reset() {
		this.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Function_None"));
	}
	
	/**
	 * 更新公式选择.
	 */
	public void populateBean(AbstractDataFunction function) {
		for(int i = 0; i < CLASS_ARRAY.length; i++) {
			if(function != null && ComparatorUtils.equals(function.getClass(), CLASS_ARRAY[i])) {
				setSelectedIndex(i);
				break;
			}
		}
	}
	
	/**
	 * 返回当前选择的公式
	 */
	public AbstractDataFunction updateBean() {
		try {
			int selectIndex = getSelectedIndex();
			if(selectIndex >= 0 && selectIndex < CLASS_ARRAY.length) {
				return (AbstractDataFunction)CLASS_ARRAY[selectIndex].newInstance();
			}
		} catch (InstantiationException e) {
			FineLoggerFactory.getLogger().error("Function Error");
			return null;
		} catch (IllegalAccessException e) {
			FineLoggerFactory.getLogger().error("Function Error");
			return null;
		}
		
		return null;
	}
}
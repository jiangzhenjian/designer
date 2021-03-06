package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.ChartCategoryPane;


/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-6
 * Time   : 上午9:50
 */
public class GanntChartStyleAxisPane extends BinaryChartStyleAxisPane {
    public GanntChartStyleAxisPane(Plot plot) {
        super(plot);
    }

    protected AxisStyleObject getXAxisPane(Plot plot) {
    	ChartCategoryPane categoryPane = new ChartCategoryPane();
    	categoryPane.getAxisValueTypePane().removeTextAxisPane();
        return new AxisStyleObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Date_Time_Axis"), categoryPane);
    }

    protected AxisStyleObject getYAxisPane(Plot plot) {
        return new AxisStyleObject(CATE_AXIS, new ChartCategoryPane());
    }
}
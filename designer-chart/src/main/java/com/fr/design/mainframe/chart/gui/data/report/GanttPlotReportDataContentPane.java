package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.GanttReportDefinition;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.mainframe.chart.gui.ChartDataPane;


import javax.swing.event.ChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 甘特图 属性表 单元格数据界面
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-21 下午03:01:48
 */
public class GanttPlotReportDataContentPane extends AbstractReportDataContentPane {

	private static final String STEP = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Step");
	private static final String START = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Plan_Start");
	private static final String END = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Plan_End");
	
	private static final String RESTART = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Actual_Start");
	private static final String REEND = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Actual_End");
	private static final String PERCENT = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_StyleFormat_Percent");
	private static final String PRO = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Project");
	
	public GanttPlotReportDataContentPane(ChartDataPane parent) {
		initEveryPane();
		
		List list = new ArrayList();
		list.add(new Object[]{STEP, ""});
		list.add(new Object[]{START, ""});
		list.add(new Object[]{END, ""});
		list.add(new Object[]{RESTART, ""});
		list.add(new Object[]{REEND, ""});
		list.add(new Object[]{PERCENT, ""});
		list.add(new Object[]{PRO, ""});
		seriesPane.populateBean(list);
		seriesPane.noAddUse();
	}

    protected void initSeriesPane() {
        seriesPane = new UICorrelationPane(columnNames()) {
            public UITableEditor createUITableEditor() {
                return new InnerTableEditor();
            }

            protected UITable initUITable() {
                return new UITable(columnCount) {

                    public UITableEditor createTableEditor() {
                        return createUITableEditor();
                    }

                    public void tableCellEditingStopped(ChangeEvent e) {
                        stopPaneEditing(e);
                    }

                    public boolean isCellEditable(int row, int column) {
                        return column != 0;
                    }
                };
            }
        };
    }
	
	public void populateBean(ChartCollection collection) {
		TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
		if(definition instanceof GanttReportDefinition) {
			GanttReportDefinition ganttDefinition = (GanttReportDefinition)definition;
			
			List list = new ArrayList();
			list.add(new Object[]{STEP, ganttDefinition.getStep()});
			list.add(new Object[]{START, ganttDefinition.getPlanStart()});
			list.add(new Object[]{END, ganttDefinition.getPlanEnd()});
			list.add(new Object[]{RESTART, ganttDefinition.getRealStart()});
			list.add(new Object[]{REEND, ganttDefinition.getRealEnd()});
			list.add(new Object[]{PERCENT, ganttDefinition.getProgress()});
			list.add(new Object[]{PRO, ganttDefinition.getItem()});
			seriesPane.populateBean(list);
		}
	}
	
	public void updateBean(ChartCollection collection) {
		GanttReportDefinition ganttDefinition = new GanttReportDefinition();
		
		List list = seriesPane.updateBean();
		HashMap map = createNameValue(list);
		
		ganttDefinition.setStep(canBeFormula(map.get(STEP)));
		ganttDefinition.setPlanStart(canBeFormula(map.get(START)));
		ganttDefinition.setPlanEnd(canBeFormula(map.get(END)));
		ganttDefinition.setRealStart(canBeFormula(map.get(RESTART)));
		ganttDefinition.setRealEnd(canBeFormula(map.get(REEND)));
		ganttDefinition.setProgress(canBeFormula(map.get(PERCENT)));
		ganttDefinition.setItem(canBeFormula(map.get(PRO)));
		
		collection.getSelectedChart().setFilterDefinition(ganttDefinition);
	}
	
	@Override
	protected String[] columnNames() {
		return new String[]{"", ""};
	}

}

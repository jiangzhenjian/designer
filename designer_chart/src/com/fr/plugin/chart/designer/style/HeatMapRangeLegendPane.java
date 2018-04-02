package com.fr.plugin.chart.designer.style;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.mainframe.chart.gui.style.series.MapColorPickerPaneWithFormula;
import com.fr.general.Inter;
import com.fr.plugin.chart.range.component.GradualIntervalConfigPane;
import com.fr.plugin.chart.range.component.GradualLegendPane;
import com.fr.plugin.chart.range.component.SectionIntervalConfigPaneWithOutNum;
import com.fr.plugin.chart.range.component.SectionLegendPane;
import com.fr.plugin.chart.type.LegendType;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by Mitisky on 16/10/20.
 * 只有渐变色图例和区域段图例.
 * 没有主题配色
 */
public class HeatMapRangeLegendPane extends VanChartRangeLegendPane {

    public HeatMapRangeLegendPane() {
    }

    public HeatMapRangeLegendPane(VanChartStylePane parent) {
        super(parent);
    }

    protected JPanel createCommonLegendPane(){
        return this.createLegendPaneWithoutHighlight();
    }

    @Override
    protected UIButtonGroup<LegendType> createLegendTypeButton(){
        return new UIButtonGroup<LegendType>(new String[]{
                Inter.getLocText("Plugin-ChartF_Legend_Gradual"),
                Inter.getLocText("Plugin-ChartF_Legend_Section")
        }, new LegendType[]{LegendType.GRADUAL, LegendType.SECTION});
    }

    @Override
    protected GradualLegendPane createGradualLegendPane() {
        return new GradualLegendPane(){
            @Override
            protected GradualIntervalConfigPane createGradualIntervalConfigPane() {
                return new GradualIntervalConfigPane(){
                    @Override
                    protected Component[][] getPaneComponents() {
                        return super.getPaneComponentsWithOutTheme();
                    }
                };
            }
        };
    }

    @Override
    protected SectionLegendPane createSectionLegendPane() {
        return new SectionLegendPane(this.parent) {
            @Override
            protected MapColorPickerPaneWithFormula createSectionIntervalConfigPane(AbstractAttrNoScrollPane parent) {
                return new SectionIntervalConfigPaneWithOutNum(parent);
            }
        };
    }
}

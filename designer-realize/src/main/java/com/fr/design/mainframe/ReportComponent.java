/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import com.fr.base.vcs.DesignerMode;
import com.fr.design.actions.AllowAuthorityEditAction;
import com.fr.design.actions.ExitAuthorityEditAction;
import com.fr.design.actions.report.ReportBackgroundAction;
import com.fr.design.actions.report.ReportFooterAction;
import com.fr.design.actions.report.ReportHeaderAction;
import com.fr.design.actions.report.ReportPageSetupAction;
import com.fr.design.designer.TargetComponent;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.selection.SelectableElement;
import com.fr.design.selection.Selectedable;
import com.fr.report.report.TemplateReport;

import javax.swing.JScrollBar;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-19 报表编辑面板
 */
public abstract class ReportComponent<T extends TemplateReport, E extends ElementCasePane, S extends SelectableElement> extends TargetComponent<T> implements Selectedable<S> {
    protected E elementCasePane;

    public E getEditingElementCasePane() {
        return elementCasePane;
    }

    public ReportComponent(T t) {
        super(t);
    }

    // TODO ALEX_SEP 这个方法有没有可能删掉
    public T getTemplateReport() {
        return this.getTarget();
    }

    public abstract JScrollBar getHorizontalScrollBar();

    public abstract JScrollBar getVerticalScrollBar();

    public abstract S getDefaultSelectElement();

    /**
     * 更新JSliderPane
     */
    public abstract void updateJSliderValue();


    @Override
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[]{
                new ReportPageSetupAction(this),
                new ReportHeaderAction(this),
                new ReportFooterAction(this),
                new ReportBackgroundAction(this),
        };
    }

    public void cancelFormat() {
    }


    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[]{
                new NameSeparator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit_DashBoard_Potence")),
                DesignerMode.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this),
        };

    }
}
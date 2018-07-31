package com.fr.design.actions;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.constants.UIConstants;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.designer.TargetComponent;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.*;

/**
 * Author : daisy
 * Date: 13-8-30
 * Time: 上午10:12
 */
public class AllowAuthorityEditAction extends TemplateComponentAction {

    public AllowAuthorityEditAction(TargetComponent t) {
        super(t);
        this.setMenuKeySet(KeySetUtils.ALLOW_AUTHORITY_EDIT);
        this.setName(getMenuKeySet().getMenuName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/allow_authority_edit.png"));
    }

    /**
     * 撤销
     */
    public void prepare4Undo() {
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().iniAuthorityUndoState();
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        TargetComponent tc = getEditingComponent();
        if (tc == null) {
            return false;
        }

        cleanAuthorityCondition();

        //进入时是格式刷则取消格式刷
        if (DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL) {
            tc.cancelFormat();
        }
        DesignerMode.setMode(DesignerMode.AUTHORITY);
        ReportAndFSManagePane.getInstance().refreshDockingView();
        RolesAlreadyEditedPane.getInstance().refreshDockingView();
        WestRegionContainerPane.getInstance().replaceDownPane(ReportAndFSManagePane.getInstance());
        DesignerContext.getDesignerFrame().setCloseMode(UIConstants.CLOSE_OF_AUTHORITY);
        DesignerContext.getDesignerFrame().resetToolkitByPlus(tc.getToolBarMenuDockPlus());
        DesignerContext.getDesignerFrame().needToAddAuhtorityPaint();
        EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION);
        EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(tc.getEastUpPane());
        DesignerContext.getDesignerFrame().refreshDottedLine();
        EastRegionContainerPane.getInstance().replaceConfiguredRolesPane(RolesAlreadyEditedPane.getInstance());
        EastRegionContainerPane.getInstance().removeParameterPane();

        //画虚线
        return true;
    }


    /**
     * 进入权限编辑之前将权限编辑界面重置一下工具栏
     */
    private void cleanAuthorityCondition() {

        java.util.List<JTemplate<?, ?>> opendedTemplate = HistoryTemplateListPane.getInstance().getHistoryList();
        for (int i = 0; i < opendedTemplate.size(); i++) {
            opendedTemplate.get(i).cleanAuthorityUndo();
        }
    }

}
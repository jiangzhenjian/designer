package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.actions.behavior.MovableUpEnable;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * 同级上移一层（控件树内）
 * Created by plough on 2017/12/4.
 */

public class MoveUpAction extends FormWidgetEditAction {

    public MoveUpAction(FormDesigner t) {
        super(t);
        this.setName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Move_Up"));
        this.setMnemonic('F');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/up.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, DEFAULT_MODIFIER));
        this.setUpdateBehavior(new MovableUpEnable());
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        FormDesigner designer = getEditingComponent();
        if (designer == null) {
            return false;
        }
        FormSelection selection = designer.getSelectionModel().getSelection();
        XCreator creator = selection.getSelectedCreator();
        XLayoutContainer container = (XLayoutContainer) creator.getParent();
        int targetIndex = container.getComponentZOrder(creator) - 1;
        if (targetIndex < 0) {
            return false;
        }
        container.setComponentZOrder(creator, targetIndex);

        designer.getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_SELECTED);
        return true;
    }
}
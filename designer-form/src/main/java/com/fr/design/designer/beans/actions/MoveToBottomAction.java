package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.actions.behavior.MovableDownEnable;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;


import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * 置于底层（控件树内）
 * Created by plough on 2017/12/4.
 */

public class MoveToBottomAction extends FormWidgetEditAction {

    public MoveToBottomAction(FormDesigner t) {
        super(t);
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Move_To_Bottom"));
        this.setMnemonic('K');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/to_bottom.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, DEFAULT_MODIFIER + InputEvent.SHIFT_MASK));
        this.setUpdateBehavior(new MovableDownEnable());
    }

    @Override
    protected String getToolTipText() {
        String originText = super.getToolTipText();
        return originText.replace(KeyEvent.getKeyText(KeyEvent.VK_OPEN_BRACKET), "[");
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
        int targetIndex = container.getComponentCount() - 1;
        if (container.getComponentZOrder(creator) >= targetIndex) {
            return false;
        }
        container.setComponentZOrder(creator, targetIndex);
        designer.getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_SELECTED);
        return true;
    }
}

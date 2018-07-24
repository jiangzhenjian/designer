package com.fr.design.remote.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.border.UITitledBorder;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.remote.ui.tree.FileAuthorityTree;
import com.fr.file.filetree.FileNode;
import com.fr.file.filetree.IOFileNodeFilter;
import com.fr.general.Inter;
import com.fr.report.DesignAuthority;
import com.fr.stable.CoreConstants;

import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class AuthorityEditorPane extends BasicBeanPane<DesignAuthority> {


    private FileAuthorityTree tree = new FileAuthorityTree();


    public AuthorityEditorPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(6, 0, 6, 6),
                        UITitledBorder.createBorderWithTitle(
                                Inter.getLocText("Fine-Designer_Remote_Design_Template_Authority_Config")
                        )
                )
        );
        IOFileNodeFilter filter = new IOFileNodeFilter(new String[]{".cpt", ".class", ".frm", ".form"});
        tree.setDigIn(true);
        tree.setFileNodeFilter(filter);
        this.add(new UIScrollPane(tree), BorderLayout.CENTER);
        tree.refreshEnv();

    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Fine-Designer_Remote_Design_Configure_Authority");
    }

    @Override
    public void populateBean(DesignAuthority ob) {
        if (ob == null) {
            return;
        }

        DesignAuthority.Item[] items = ob.getItems();
        if (items == null) {
            return;
        }
        String[] paths = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            paths[i] = items[i].getPath();
        }
        tree.selectCheckBoxPaths(paths);
    }

    @Override
    public DesignAuthority updateBean() {
        DesignAuthority da = new DesignAuthority();
        TreePath[] treePaths = tree.getCheckBoxTreeSelectionModel().getSelectionPaths();

        List<DesignAuthority.Item> items = new ArrayList<>();
        for (TreePath treePath : treePaths) {
            StringBuilder tempSpot = new StringBuilder();
            boolean type = true;
            for (int counter = 1, maxCounter = treePath.getPathCount(); counter < maxCounter;
                 counter++) {
                if (counter > 1) {
                    tempSpot.append(CoreConstants.SEPARATOR);
                }
                FileNode fileNode = (FileNode) ((ExpandMutableTreeNode) treePath.getPathComponent(counter)).getUserObject();
                type = type && fileNode.isDirectory();
                tempSpot.append(fileNode.getName());
            }
            items.add(new DesignAuthority.Item(tempSpot.toString(), type));
        }
        da.setItems(items.toArray(new DesignAuthority.Item[0]));
        return da;
    }
}

package com.fr.design.actions.insert.cell;

import com.fr.design.actions.core.WorkBookSupportable;
import com.fr.design.dscolumn.DSColumnPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.general.IOUtils;
import com.fr.report.cell.cellattr.core.group.DSColumn;

public class DSColumnCellAction extends AbstractCellAction implements WorkBookSupportable {
	public DSColumnCellAction(ElementCasePane t) {
    	super(t);
        this.setMenuKeySet(KeySetUtils.INSERT_DATA_COLUMN);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_insert/bindColumn.png"));
        this.setSearchText(new DSColumnPane());
    }

    @Override
    public Class getCellValueClass() {
        return DSColumn.class;
    }
}
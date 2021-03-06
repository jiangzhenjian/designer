package com.fr.design.data.tabledata.tabledatapane;

import com.fr.data.TableDataSource;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.file.ProcedureConfig;
import com.fr.general.ComparatorUtils;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProcedureListPane extends JListControlPane {
	public ProcedureListPane() {
		super();
		this.addEditingListener(new PropertyChangeAdapter() {
			@Override
			public void propertyChange() {
				TableDataSource source = DesignTableDataManager.getEditingTableDataSource();
				if (source == null) {
					return;
				}
				String[] allDSNames = DesignTableDataManager.getAllDSNames(source);
				String tempName = getEditingName();
				if(StringUtils.isEmpty(tempName)) {
					nameableList.stopEditing();
					//JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(ProcedureListPane.this), com.fr.design.i18n.Toolkit.i18nText("NOT_NULL_Des") + "," + com.fr.design.i18n.Toolkit.i18nText("Please_Rename") + "!");
					setIllegalIndex(nameableList.getSelectedIndex());
					return;
				}
				for(int i = 0; i < allDSNames.length; i++) {
					String dsname = allDSNames[i];
					if (ComparatorUtils.equals(dsname, tempName)) {
//						JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(ProcedureListPane.this), com.fr.design.i18n.Toolkit.i18nText(new String[]{"already_exists", "TableData"})
//								+ tempName + "," + com.fr.design.i18n.Toolkit.i18nText("Please_Rename") + "!");
						setIllegalIndex(nameableList.getSelectedIndex());
						break;
					}
				}
			}
		});
	}

    /**
     * 创建不重复的名称
     *
     * @param prefix 前缀
     *
     * @return 不重复的名称
     */
	public String createUnrepeatedName(String prefix) {
		TableDataSource source = DesignTableDataManager.getEditingTableDataSource();
		if (source == null) {
			return super.createUnrepeatedName(prefix);
		}
		String[] allDsNames = DesignTableDataManager.getAllDSNames(source);
		DefaultListModel model = this.getModel();
		Nameable[] all = new Nameable[model.getSize()];
		for (int i = 0; i < model.size(); i++) {
			all[i] = ((ListModelElement) model.get(i)).wrapper;
		}
		// richer:生成的名字从1开始. kunsnat: 添加属性从0开始.
		int count = all.length + 1;
		while (true) {
			String name_test = prefix + count;
			boolean repeated = false;
			for (int i = 0, len = model.size(); i < len; i++) {
				Nameable nameable = all[i];
				if (ComparatorUtils.equals(nameable.getName(), name_test)) {
					repeated = true;
					break;
				}
			}
			for (String dsname : allDsNames) {
				if (ComparatorUtils.equals(dsname, name_test)) {
					repeated = true;
					break;
				}
			}

			if (!repeated) {
				return name_test;
			}

			count++;
		}
	}

	/**
	 * 创建对象组件
	 *
	 * @return 面板组件
	 */
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[] { new NameObjectCreator("Proc", "/com/fr/design/images/data/store_procedure.png", StoreProcedure.class,
				ProcedureDataPane.class) };
	}

	@Override
	protected String title4PopupWindow() {
		return "procedure";
	}

	/**
	 * Populate.
	 * 
	 * @param procedureConfig
	 *            the new datasourceManager.
	 */
	public void populate(ProcedureConfig procedureConfig) {
		Iterator<String> nameIt = procedureConfig.getProcedures().keySet().iterator();

		List<NameObject> nameObjectList = new ArrayList<NameObject>();
		while (nameIt.hasNext()) {
			String name = nameIt.next();
			nameObjectList.add(new NameObject(name, procedureConfig.getProcedure(name)));
		}
		this.populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));

	}

	/**
	 * Update.
	 */
	public void update(ProcedureConfig procedureConfig) {
		// Nameable[]居然不能强转成NameObject[],一定要这么写...
		Nameable[] res = this.update();
		NameObject[] res_array = new NameObject[res.length];
		java.util.Arrays.asList(res).toArray(res_array);

		procedureConfig.removeAllProcedure();

		for (int i = 0; i < res_array.length; i++) {
			NameObject nameObject = res_array[i];
			procedureConfig.addProcedure(nameObject.getName(), (StoreProcedure) nameObject.getObject());
		}
	}

}
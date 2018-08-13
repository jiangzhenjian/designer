/* * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved. */package com.fr.design.form.parameter;import java.awt.Component;import com.fr.design.designer.beans.ConstraintsGroupModel;import com.fr.design.designer.creator.XWParameterLayout;import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;import com.fr.design.mainframe.widget.editors.PropertyCellEditor;import javax.swing.table.DefaultTableCellRenderer;import javax.swing.table.TableCellEditor;import javax.swing.table.TableCellRenderer;/** * Created by IntelliJ IDEA. * User   : Richer * Version: 6.5.5 * Date   : 11-7-5 * Time   : 下午2:56 */public class RootDesignGroupModel implements ConstraintsGroupModel {    private DefaultTableCellRenderer renderer;    private PropertyCellEditor editor;    private XWParameterLayout root;    public RootDesignGroupModel(XWParameterLayout root) {        this.root = root;        renderer = new DefaultTableCellRenderer();        editor = new PropertyCellEditor(new IntegerPropertyEditor());        //初始值为参数面板的初始宽度        if (root.toData().getDesignWidth() == 0){            root.toData().setDesignWidth(root.getWidth());        }    }    @Override    public String getGroupName() {        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_ Design_Size");    }    @Override    public int getRowCount() {        return 1;    }    @Override    public TableCellRenderer getRenderer(int row) {        return renderer;    }    @Override    public TableCellEditor getEditor(int row) {        return editor;    }    @Override    public Object getValue(int row, int column) {        if (column == 0) {            switch (row) {                case 0:                    return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Desin_Width");            }        } else {            switch (row) {                case 0:                    return root.toData().getDesignWidth();            }        }		return null;    }    @Override    public boolean setValue(Object value, int row, int column) {		if (column == 1) {			int designerWidth = value == null ? 0 : ((Number) value).intValue();			switch (row) {				case 0:					if(isCompsOutOfDesignerWidth(designerWidth)){						return false;					}                    root.toData().setDesignWidth(designerWidth);					return true;				default:					return true;			}		} else {			return false;		}    }            private boolean isCompsOutOfDesignerWidth(int designerWidth){    	for(int i=0; i<root.getComponentCount(); i++){    		Component comp = root.getComponent(i);    		if(comp.getX() + comp.getWidth() > designerWidth){    			return true;    		}    	}    	return false;    }    /**     * 改行是否可编辑     * @param row    行号     * @return 第row行可编辑返回true，否则返回false     */    public boolean isEditable(int row) {        return true;    }}
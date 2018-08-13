package com.fr.design.designer.properties.items;

import javax.swing.SwingConstants;



public class LabelHorizontalAlignmentItems implements ItemProvider {

    private static Item[] VALUE_ITEMS = {
        new Item(com.fr.design.i18n.Toolkit.i18nText("Left"), SwingConstants.LEFT),
        new Item(com.fr.design.i18n.Toolkit.i18nText("Right"), SwingConstants.RIGHT),
        new Item(com.fr.design.i18n.Toolkit.i18nText("Center"), SwingConstants.CENTER)
        };
        
    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
}
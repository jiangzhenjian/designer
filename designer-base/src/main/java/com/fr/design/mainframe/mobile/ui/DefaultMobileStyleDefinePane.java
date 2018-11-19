package com.fr.design.mainframe.mobile.ui;

import com.fr.base.GraphHelper;
import com.fr.design.mainframe.widget.preview.MobileTemplatePreviewPane;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.FRFont;
import com.fr.general.cardtag.mobile.DefaultMobileTemplateStyle;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DefaultMobileStyleDefinePane extends MobileTemplateStyleDefinePane {

    public DefaultMobileStyleDefinePane(WCardTagLayout tagLayout) {
        super(tagLayout);
    }

    @Override
    public void populateBean(MobileTemplateStyle ob) {
        updatePreviewPane();
    }

    protected void createConfigPane() {

    }

    @Override
    protected void initDefaultConfig() {

    }

    @Override
    public MobileTemplateStyle updateSubStyle() {
        return null;
    }

    @Override
    public MobileTemplateStyle updateBean() {
        return new DefaultMobileTemplateStyle();
    }

    @Override
    protected MobileTemplateStyle getDefaultTemplateStyle() {
        return new DefaultMobileTemplateStyle();
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    protected MobileTemplatePreviewPane createPreviewPane() {
        return new DefaultStylePreviewPane();
    }

    public class DefaultStylePreviewPane extends MobileTemplatePreviewPane {

        public DefaultStylePreviewPane() {
            this.setBackground(DefaultMobileTemplateStyle.DEFAULT_INITIAL_COLOR);
        }

        public void repaint() {
            super.repaint();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Dimension dimension = this.getSize();
            int panelWidth = dimension.width;
            int panelHeight = dimension.height;
            Graphics2D g2d = (Graphics2D) g.create();
            FRFont frFont = DefaultMobileTemplateStyle.DEFAULT_TAB_FONT.getFont();
            FontMetrics fm = GraphHelper.getFontMetrics(frFont);
            WCardTagLayout cardTagLayout = DefaultMobileStyleDefinePane.this.getTagLayout();
            int eachWidth = panelWidth / cardTagLayout.getWidgetCount();
            g2d.setFont(frFont);
            for (int i = 0; i < cardTagLayout.getWidgetCount(); i++) {
                CardSwitchButton cardSwitchButton = cardTagLayout.getSwitchButton(i);
                String widgetName = cardSwitchButton.getText();
                int width = fm.stringWidth(widgetName);
                g2d.drawString(widgetName, (eachWidth - width) / 2, (panelHeight) / 2);
                if (i == 0) {
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.drawLine(0, panelHeight - 1, eachWidth, panelHeight - 1);
                }
                g2d.translate(eachWidth, 0);
            }
        }
    }

}

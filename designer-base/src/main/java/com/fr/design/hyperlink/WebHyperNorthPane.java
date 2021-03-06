package com.fr.design.hyperlink;

import com.fr.config.ServerPreferenceConfig;
import com.fr.design.constants.LayoutConstants;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.js.WebHyperlink;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;

import javax.swing.JPanel;
import java.awt.BorderLayout;


/**
 * chart 网页链接 定义属性 target url 特征的 界面
 *
 * @author kunsnat
 */
public class WebHyperNorthPane extends AbstractHyperNorthPane<WebHyperlink> {
    private UITextField itemNameTextField;
    private boolean needRenamePane = false;
    private UITextField urlTextField;

    public WebHyperNorthPane(boolean needRenamePane) {
        this.needRenamePane = needRenamePane;
        this.inits();
    }

    public WebHyperNorthPane() {
        this.inits();
    }

    /**
     * 初始化
     *
     * @date 2014-4-11
     */
    public void inits() {
        super.initComponents();
    }

    @Override
    protected JPanel setHeaderPanel() {
        JPanel headerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();

        urlTextField = new UITextField(headerPane.getWidth());
        urlTextField.setText(ProductConstants.WEBSITE_URL);

        JPanel urlWithHelp = GUICoreUtils.createNamedPane(urlTextField, "URL:");

        if (this.needRenamePane) {
            headerPane.setLayout(new BorderLayout(LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_SMALL));
            itemNameTextField = new UITextField();
            headerPane.add(GUICoreUtils.createNamedPane(itemNameTextField, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Name") + ":"), BorderLayout.NORTH);
            headerPane.add(urlWithHelp, BorderLayout.CENTER);
        } else {
            headerPane.add(urlWithHelp, BorderLayout.NORTH);
        }

        return headerPane;
    }

    public String getURL() {
        return this.urlTextField.getText();
    }

    @Override
    protected String title4PopupWindow() {
        return "web";
    }

    @Override
    protected void populateSubHyperlinkBean(WebHyperlink link) {
        String url = link.getURL();
        if (StringUtils.isBlank(url)) {
            url = ServerPreferenceConfig.getInstance().getHyperlinkAddress();
        }
        this.urlTextField.setText(url);
        if (itemNameTextField != null) {
            this.itemNameTextField.setText(link.getItemName());
        }
    }

    @Override
    protected WebHyperlink updateSubHyperlinkBean() {
        WebHyperlink webHyperlink = new WebHyperlink();
        updateSubHyperlinkBean(webHyperlink);

        return webHyperlink;
    }

    protected void updateSubHyperlinkBean(WebHyperlink webHyperlink) {
        webHyperlink.setURL(this.urlTextField.getText());
        if (itemNameTextField != null) {
            webHyperlink.setItemName(this.itemNameTextField.getText());
        }
    }

    @Override
    protected JPanel setFootPanel() {
        return new JPanel();
    }

}

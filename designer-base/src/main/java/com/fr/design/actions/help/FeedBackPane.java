/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.actions.help;

import com.fr.base.FRContext;
import com.fr.base.FeedBackInfo;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.DesignUtils;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.concurrent.CancellationException;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-18
 * Time: 上午9:50
 */
public class FeedBackPane extends BasicPane {

    private static final Dimension DIALOG_SIZE = new Dimension(660, 600);
    private static final Dimension TEXT_AREA_SIZE = new Dimension(585, 180);
    private static final int TEXT_FIELD_WIDTH = 160;
    private static final int TEXT_FIELD_HEIGHT = 21;
    private static final int GAP = 5;
    private static final ColorUIResource BORDER_COLOR = new ColorUIResource(168, 172, 180);
    private static final Border TIP_BORDER = BorderFactory.createEmptyBorder(10, 4, 4, 4);
    private static final Border DIALOG_BORDER = BorderFactory.createEmptyBorder(0, 6, 4, 6);
    private static final Border INNER_LEFT_BORDER = BorderFactory.createEmptyBorder(15, 18, 0, 0);
    private static final Border TEXT_AREA_BORDER = BorderFactory.createEmptyBorder(0, 13, 0, 15);
    private static final Border SEND_BORDER = BorderFactory.createEmptyBorder(10, 0, 10, 0);
    private static final int DETAIL_TEXT_MAX_LENGTH = 400;
    private static final int QQ_MAX_LENGTH = 15;
    private static final int EMAIL_MAX_LENGTH = 40;
    private static final int TEL_MAX_LENGTH = 11;
    private static final String ALLOWED_INTEGER_TYPE = "0123456789";
    private SwingWorker worker;
    private JDialog dlg = new JDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Send"), true);
    private UIButton ok = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_OK"));
    private UIButton cancle = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cancel"));
    private int ifHasBeenWriten = 0;
    private UITextArea detailField;
    private boolean isSendSuccessful = false;
    private UITextField qq = new UITextField() {
        public Dimension getPreferredSize() {
            return new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        }
    };
    protected UITextField email = new UITextField() {
        public Dimension getPreferredSize() {
            return new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        }
    };
    protected UITextField phone = new UITextField() {
        public Dimension getPreferredSize() {
            return new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        }
    };
    private UIButton sendButton = null;

    private BasicDialog feedbackDialog = null;
    private JOptionPane send = null;

    public FeedBackPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        initDetailArea();
        UILabel tip = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Feedback_Info"));
        tip.setBorder(TIP_BORDER);
        this.add(tip, BorderLayout.NORTH);
        UIScrollPane scrollPane = new UIScrollPane(initDetailPane());
        scrollPane.setBorder(new MatteBorder(1, 1, 1, 1, BORDER_COLOR));
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(getSendButton(), BorderLayout.SOUTH);
        sendButton.setEnabled(false);
        this.setBorder(DIALOG_BORDER);
        initDocument();
    }

    public void setFeedbackDialog(BasicDialog dialog) {
        feedbackDialog = dialog;
    }

    public Dimension getPreferredSize() {
        return DIALOG_SIZE;
    }


    private void initDocument() {
        LimitedDocument qqLimited = new LimitedDocument(QQ_MAX_LENGTH);
        qqLimited.setAllowChar(ALLOWED_INTEGER_TYPE);
        qq.setDocument(qqLimited);
        email.setDocument(new LimitedDocument(EMAIL_MAX_LENGTH));
        LimitedDocument phoneLimited = new LimitedDocument(TEL_MAX_LENGTH);
        phoneLimited.setAllowChar(ALLOWED_INTEGER_TYPE);
        phone.setDocument(phoneLimited);
    }


    private void initDetailArea() {
        detailField = new UITextArea() {
            public Dimension getPreferredSize() {
                return TEXT_AREA_SIZE;
            }
        };
        this.detailField.setForeground(Color.gray);
        this.detailField.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Feedback_Tip"));
        detailField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (ifHasBeenWriten == 0) {
                    detailField.setText(StringUtils.EMPTY);
                    detailField.setDocument(new LimitedDocument(DETAIL_TEXT_MAX_LENGTH));
                    ifHasBeenWriten = 1;
                }
            }

            public void keyReleased(KeyEvent e) {
                if (ifHasBeenWriten == 0) {
                    detailField.setText(StringUtils.EMPTY);
                    detailField.setDocument(new LimitedDocument(DETAIL_TEXT_MAX_LENGTH));
                    ifHasBeenWriten = 1;
                }
                detailField.setForeground(Color.black);
                String text = detailField.getText();
                // 判断在中文输入状态是否还包含提示符 要删掉
                String tip = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Feedback_Tip");
                if (text.contains(tip)) {
                    text = text.substring(0, text.indexOf(tip));
                    detailField.setText(text);
                }
                sendButton.setEnabled(!(ifHasBeenWriten == 0 || ComparatorUtils.equals(detailField.getText().trim(), StringUtils.EMPTY)));
            }
        });

        detailField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (ifHasBeenWriten == 0) {
                    detailField.setText(StringUtils.EMPTY);
                    detailField.setDocument(new LimitedDocument(DETAIL_TEXT_MAX_LENGTH));
                    ifHasBeenWriten = 1;
                }
            }
        });
    }

    private JPanel initDetailPane() {
        double p = TableLayout.PREFERRED;
        UILabel info = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Detail_Description") + ":");
        info.setBorder(INNER_LEFT_BORDER);
        JPanel contactPane = getContactPane();
        contactPane.setBorder(INNER_LEFT_BORDER);
        UILabel contact = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Contact_Info") + ":");
        contact.setBorder(INNER_LEFT_BORDER);
        JPanel jPanel = new JPanel();
        jPanel.add(detailField);
        jPanel.setBorder(TEXT_AREA_BORDER);
        Component[][] components = new Component[][]{
                new Component[]{info},
                new Component[]{jPanel},
                new Component[]{contact},
                new Component[]{contactPane},
        };
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p};
        int[][] rowCount = {{1}, {1}, {1}, {1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }


    protected JPanel getContactPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel("QQ:", SwingConstants.RIGHT), qq},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Email") + ":", SwingConstants.RIGHT), email},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Mobile_Number") + ":", SwingConstants.RIGHT), phone}
        };
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, p};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }

    private JPanel getSendButton() {
        JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.LEFT, GAP, 0));
        controlPane.add(buttonsPane, BorderLayout.EAST);
        sendButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Send"));
        buttonsPane.add(sendButton);
        buttonsPane.setBorder(SEND_BORDER);
        sendButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                doWithSendPane();
            }
        });
        controlPane.setBorder(new EmptyBorder(0, 0, GAP, 0));
        return controlPane;
    }


    private void doWithSendPane() {
        Object[] options = new Object[]{ok, cancle};
        send = new JOptionPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Sending"),
                JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);
        ok.setEnabled(false);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSendSuccessful) {
                    dlg.dispose();
                    feedbackDialog.dispose();
                } else {
                    ok.setEnabled(false);
                    send.setMessage(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Sending"));
                    setWorker(send);
                    worker.execute();
                }
            }
        });
        cancle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worker.cancel(true);
                dlg.dispose();
            }
        });

        dlg.setContentPane(send);
        dlg.pack();
        dlg.setLocationRelativeTo(DesignerContext.getDesignerFrame());
        setWorker(send);
        worker.execute();
        dlg.setVisible(true);


    }


    private void setWorker(final JOptionPane send) {
        worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                FeedBackInfo feedBackInfo = new FeedBackInfo(new Date(), qq.getText(), email.getText(), phone.getText(), detailField.getText());
                try {
                    return DesignUtils.sendFeedBack(feedBackInfo);
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                    return false;
                }
            }

            public void done() {
                try {
                    boolean model = get();
                    ok.setEnabled(true);
                    cancle.setEnabled(!model);
                    if (model) {
                        //发送成功
                        isSendSuccessful = true;
                        send.setMessage(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_HJS_Send_Successfully") + "!");
                    } else {
                        isSendSuccessful = false;
                    }
                } catch (Exception e) {
                    isSendSuccessful = false;
                    if (!(e instanceof CancellationException)) {
                        FRContext.getLogger().error(e.getMessage(), e);
                    }
                }
            }
        };
    }


    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Product_Feedback");
    }


    private class LimitedDocument extends PlainDocument {
        private static final long serialVersionUID = 1L;
        private int maxLength = -1;// 允许的最大长度
        private String allowCharAsString = null;// 允许的字符串格式（0123456789）

        public LimitedDocument(int maxLength) {
            super();
            this.maxLength = maxLength;
        }

        public void insertString(int offset, String str, AttributeSet attrSet) throws BadLocationException {
            if (str == null) {
                return;
            }
            if (allowCharAsString != null && str.length() == 1) {
                if (allowCharAsString.indexOf(str) == -1) {
                    Toolkit.getDefaultToolkit().beep();// 发出一个警告声
                    return;// 不是所要求的字符格式，就直接返回，不进行下面的添加
                }
            }
            char[] charVal = str.toCharArray();
            String strOldValue = getText(0, getLength());
            char[] tmp = strOldValue.toCharArray();
            if (maxLength != -1 && (tmp.length + charVal.length > maxLength)) {
                Toolkit.getDefaultToolkit().beep();// 发出一个警告声
                return;// 长度大于指定的长度maxLength，也直接返回，不进行下面的添加
            }
            super.insertString(offset, str, attrSet);
        }

        public void setAllowChar(String str) {
            allowCharAsString = str;
        }
    }

}

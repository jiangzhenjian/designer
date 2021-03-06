package com.fr.design.utils;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.FeedBackInfo;
import com.fr.base.ServerConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.DesignerEnvProcessor;
import com.fr.design.gui.UILookAndFeel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.FileFILE;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.GeneralContext;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.CodeUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.start.ServerStarter;
import com.fr.workspace.WorkContext;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Locale;


/**
 * Some util method of Designer
 */
public class DesignUtils {
    private static final int MESSAGEPORT = 51462;
    private static final int TIME_OUT = 20 * 1000;

    public synchronized static void setPort(int port) {
        DesignUtils.port = port;
    }

    private static int port = MESSAGEPORT;

    private DesignUtils() {

    }


    /**
     * 通过端口是否被占用判断设计器有没有启动
     * s
     *
     * @return 启动了返回true
     */
    public static boolean isStarted() {
        try {
            new Socket("localhost", port);
            return true;
        } catch (Exception exp) {

        }
        return false;
    }

    /**
     * 向服务器发送命令行，给服务器端处理
     *
     * @param lines 命令行
     */
    public static void clientSend(String[] lines) {
        if (lines != null && lines.length <= 0) {
            return;
        }
        Socket socket = null;
        PrintWriter writer = null;
        try {
            socket = new Socket("localhost", port);

            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), EncodeConstants.ENCODING_UTF_8)));
            for (int i = 0; i < lines.length; i++) {
                writer.println(lines[i]);
            }

            writer.flush();
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        } finally {
            try {
                writer.close();
                socket.close();
            } catch (IOException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    /**
     * 建立监听端口
     *
     * @param startPort 端口
     * @param suffixs   文件后缀
     */
    public static void creatListeningServer(final int startPort, final String[] suffixs) {
        Thread serverSocketThread = new Thread() {
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(startPort);
                } catch (IOException e1) {
                    FineLoggerFactory.getLogger().error("Cannot create server socket on" + port);
                }
                while (true) {
                    try {
                        Socket socket = serverSocket.accept(); // 接收客户连接
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), EncodeConstants.ENCODING_UTF_8));
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("demo")) {
                                DesignerEnvManager.getEnvManager().setCurrentEnv2Default();
                                ServerStarter.browserDemoURL();
                            } else if (StringUtils.isNotEmpty(line)) {
                                File f = new File(line);
                                String path = f.getAbsolutePath();

                                boolean isMatch = false;
                                for (int i = 0; i < suffixs.length; i++) {
                                    isMatch = isMatch || path.endsWith(suffixs[i]);
                                }
                                if (isMatch) {
                                    DesignerContext.getDesignerFrame().openTemplate(new FileFILE(f));
                                }
                            }
                        }

                        reader.close();
                        socket.close();
                    } catch (IOException e) {

                    }
                }
            }
        };
        serverSocketThread.start();
    }

    /**
     * 弹出对话框,显示报错
     *
     * @param message 报错信息
     */
    public static void errorMessage(String message) {
        final String final_msg = message;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), final_msg);
            }
        });
    }

    public static void refreshDesignerFrame() {

        // 刷新DesignerFrame里面的面板
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (DesignerContext.getDesignerFrame() == null) {
                    return;
                }
                DesignerContext.getDesignerFrame().refreshEnv();
                DesignerContext.getDesignerFrame().repaint();// kunsnat: 切换环境后 刷新下 报表. 比如图表某些风格改变.
            }
        });
    }

    /**
     * p:初始化look and feel, 把一切放到这个里面.可以让多个地方调用.
     */
    public static void initLookAndFeel() {
        // p:隐藏对话框的系统标题风格，用look and feel定义的标题风格.
        try {
            UIManager.setLookAndFeel(UILookAndFeel.class.getName());
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error("Substance Raven Graphite failed to initialize");
        }
        //获取当前系统语言下设计器用的默认字体
        FRFont guiFRFont = getCurrentLocaleFont();
        //指定UIManager中字体
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();

            if (key.endsWith(".font")) {
                UIManager.put(key, isTextField(key) ? getNamedFont("Dialog") : guiFRFont);
            }
        }
    }

    private static boolean isTextField(String key) {
        return key.startsWith("TextField.") || key.startsWith("PasswordField.");
    }

    private static FRFont getCurrentLocaleFont() {
        FRFont guiFRFont;
        Locale defaultLocale = Locale.getDefault();

        if (isDisplaySimSun(defaultLocale)) {
            guiFRFont = getNamedFont("SimSun");
        } else if (isDisplayDialog(defaultLocale)) {
            guiFRFont = getNamedFont("Dialog");
        } else {
            guiFRFont = getNamedFont("Tahoma");
        }

        //先初始化的设计器locale, 后初始化lookandfeel.如果顺序改了, 这边也要调整.
        Locale designerLocale = GeneralContext.getLocale();
        String file = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_File");
        char displayChar = file.charAt(0);
        if (!guiFRFont.canDisplay(displayChar)) {
            //如果不能用默认的语言显示字体, 比如想在英文系统里用中文设计器
            //默认语言(中文:宋体, 英文:Tahoma, 其他:Dialog)
            guiFRFont = getNamedFont("SimSun");
            if (!guiFRFont.canDisplay(displayChar)) {
                //比如想在中文或英文系统里用韩文设计器
                guiFRFont = getNamedFont("Dialog");
                if (!guiFRFont.canDisplay(displayChar)) {
                    FRContext.getLogger().error(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Base_SimSun_Not_Found"));
                }
            }
        }

        return guiFRFont;
    }

    private static FRFont getNamedFont(String name) {
        return FRFont.getInstance(name, Font.PLAIN, 12);
    }

    private static boolean isDisplaySimSun(Locale defaultLocale) {
        return ComparatorUtils.equals(defaultLocale, Locale.SIMPLIFIED_CHINESE);
    }

    private static boolean isDisplayDialog(Locale defaultLocale) {
        return ComparatorUtils.equals(defaultLocale, Locale.TRADITIONAL_CHINESE)
                || ComparatorUtils.equals(defaultLocale, Locale.JAPANESE)
                || ComparatorUtils.equals(defaultLocale, Locale.JAPAN)
                || ComparatorUtils.equals(defaultLocale, Locale.KOREAN)
                || ComparatorUtils.equals(defaultLocale, Locale.KOREA);
    }

    /**
     * 访问服务器环境-空参数
     */
    public static void visitEnvServer() {
        visitEnvServerByParameters(StringUtils.EMPTY, new String[]{}, new String[]{});
    }

    /**
     * 访问服务器环境
     *
     * @param names  参数名字
     * @param values 参数值
     */
    public static void visitEnvServerByParameters(String baseRoute, String[] names, String[] values) {
        int len = Math.min(ArrayUtils.getLength(names), ArrayUtils.getLength(values));
        String[] segs = new String[len];
        for (int i = 0; i < len; i++) {
            try {
                //设计器里面据说为了改什么界面统一, 把分隔符统一用File.separator, 意味着在windows里面报表路径变成了\
                //以前的超链, 以及预览url什么的都是/, 产品组的意思就是用到的地方替换下, 真恶心.
                String value = values[i].replaceAll("\\\\", "/");
                segs[i] = URLEncoder.encode(CodeUtils.cjkEncode(names[i]), EncodeConstants.ENCODING_UTF_8) + "=" + URLEncoder.encode(CodeUtils.cjkEncode(value), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        String postfixOfUri = (segs.length > 0 ? "?" + StableUtils.join(segs, "&") : StringUtils.EMPTY);
    
        if (!WorkContext.getCurrent().isLocal()) {
            try {
                String urlPath = getWebBrowserPath();
                Desktop.getDesktop().browse(new URI(urlPath + baseRoute + postfixOfUri));
            } catch (Exception e) {
                FRContext.getLogger().error("cannot open the url Successful", e);
            }
        } else {
            try {
                String web = GeneralContext.getCurrentAppNameOfEnv();
                String url = "http://localhost:" + DesignerEnvManager.getEnvManager().getEmbedServerPort()
                        + "/" + web + "/" + ServerConfig.getInstance().getServletName() + baseRoute
                        + postfixOfUri;
                ServerStarter.browserURLWithLocalEnv(url);
            } catch (Throwable e) {
                //
            }
        }
    }

    private static String getWebBrowserPath() {
        String urlPath = WorkContext.getCurrent().getPath();
        DesignerEnvProcessor processor = ExtraDesignClassManager.getInstance().getSingle(DesignerEnvProcessor.XML_TAG);
        if (processor != null) {
            //cas访问的时候, url要处理下.
            urlPath = processor.getWebBrowserURL(urlPath);
        }
        return urlPath;
    }

    //TODO:august:下个版本，要把下面的图片都放在一个preload文件夹下，表示可以预先加载。然后遍历一下就可以了，不用这么一个一个的写了

    /**
     * 预加载
     */
    public static void preLoadingImages() {
        BaseUtils.readIcon("com/fr/design/images/custombtn/baobiaozhuti.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/baobiaozhuti_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/baobiaozhuti_click.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/canshujiemian.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/canshujiemian_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/canshujiemian_click.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/setting.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/setting_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/setting_click.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/page.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/page_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/page_click.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/form.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/form_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/form_click.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/edit.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/edit_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/edit_click.png");
        BaseUtils.readIcon("com/fr/base/images/oem/addworksheet.png");
        BaseUtils.readIcon("com/fr/design/images/sheet/addpolysheet.png");
        BaseUtils.readIcon("com/fr/base/images/oem/worksheet.png");
        BaseUtils.readIcon("com/fr/design/images/sheet/polysheet.png");
        BaseUtils.readIcon("com/fr/design/images/sheet/left_right_btn.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/cellPop.png");
        BaseUtils.readIcon("/com/fr/design/images/docking/right.gif");
        BaseUtils.readIcon("/com/fr/design/images/docking/left.gif");
        BaseUtils.readIcon("/com/fr/design/images/m_file/save.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/excel.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/pdf.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/word.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/svg.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/csv.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/text.png");
        BaseUtils.readIcon("/com/fr/design/images/m_web/datasource.png");
        BaseUtils.readIcon("/com/fr/design/images/m_report/webreportattribute.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/pageSetup.png");
        BaseUtils.readIcon("/com/fr/design/images/m_report/header.png");
        BaseUtils.readIcon("/com/fr/design/images/m_report/footer.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/saveAs.png");
        BaseUtils.readIcon("/com/fr/design/images/m_report/background.png");
        loadOtherImages();
    }


    private static void loadOtherImages() {
        BaseUtils.readIcon("/com/fr/design/images/m_report/reportWriteAttr.png");
        BaseUtils.readIcon("/com/fr/design/images/m_report/linearAttr.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/bindColumn.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/text.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/chart.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/image.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/bias.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/subReport.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/insertRow.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/insertColumn.png");
        BaseUtils.readIcon("/com/fr/design/images/m_format/highlight.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/hyperLink.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/merge.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/unmerge.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/export.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/cell.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/float.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/undo.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/redo.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/cut.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/paste.png");
        BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/leftAlignment.png");
        BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/centerAlignment.png");
        BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/rightAlignment.png");
        BaseUtils.readIcon("/com/fr/design/images/m_format/noboder.png");
        BaseUtils.readIcon("/com/fr/design/images/gui/color/background.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/floatPop.png");
    }


    /**
     * 将用户反馈发送至服务器
     *
     * @param feedBackInfo 用户反馈
     * @return 发送成功返回true
     * @throws Exception 异常
     */
    public static boolean sendFeedBack(FeedBackInfo feedBackInfo) throws Exception {
        return true;
    }
}

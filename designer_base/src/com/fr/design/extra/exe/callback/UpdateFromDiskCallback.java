package com.fr.design.extra.exe.callback;

import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.error.PluginErrorCode;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskResult;

import javax.swing.*;
import java.io.File;

/**
 * Created by ibm on 2017/5/27.
 */
public class UpdateFromDiskCallback extends AbstractPluginTaskCallback {
    private File zipFile;
    private JSCallback jsCallback;
    private static int HUNDRED_PERCENT = 100;

    public UpdateFromDiskCallback(File zipFile, JSCallback jsCallback) {
        this.zipFile = zipFile;
        this.jsCallback = jsCallback;
    }

    @Override
    public void updateProgress(String description, double aProgress) {
        jsCallback.execute(String.valueOf(aProgress * HUNDRED_PERCENT + "%"));
    }


    @Override
    public void done(PluginTaskResult result) {
        jsCallback.execute("success");
        if (result.isSuccess()) {
            FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Update_Success"));
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Plugin_Update_Success"));
        } else if (result.errorCode() == PluginErrorCode.NeedInstallInterPluginDependency) {
            int rv = JOptionPane.showOptionDialog(
                    null,
                    Inter.getLocText(Inter.getLocText("FR-Designer-Plugin_Update_Dependence")),
                    Inter.getLocText("FR-Designer-Plugin_Warning"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    null
            );
            if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                return;
            }
            PluginManager.getController().update(zipFile, new UpdateFromDiskCallback(zipFile, jsCallback));
        } else {
            FRLogger.getLogger().info(Inter.getLocText("FR-Designer-Plugin_Update_Failed"));
            JOptionPane.showMessageDialog(null, result.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
package com.fr.start.module;

import com.fr.design.DesignerEnvManager;
import com.fr.design.env.DesignerWorkspaceGenerator;
import com.fr.design.mainframe.TemplatePane;
import com.fr.general.ComparatorUtils;
import com.fr.module.Activator;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;

/**
 * Created by juhaoyu on 2018/1/8.
 * 设计器启动时的环境相关模块activator
 */
public class DesignerWorkspaceProvider extends Activator {
    
    @Override
    public void start() {
        //检查环境
        DesignerEnvManager.checkNameEnvMap();
        
        if (getModule().leftFindSingleton(StartupArgs.class) != null && getModule().leftFindSingleton(StartupArgs.class).isDemo()) {
            DesignerEnvManager.getEnvManager().setCurrentEnv2Default();
        } else {
            try {
                String current = DesignerEnvManager.getEnvManager().getCurEnvName();
                Workspace workspace = DesignerWorkspaceGenerator.generate(DesignerEnvManager.getEnvManager().getWorkspaceInfo(current));
                if (workspace == null) {
                    TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
                } else {
                    WorkContext.switchTo(workspace);
                }
            } catch (Throwable e) {
                TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
            }
        }
    }
    
    
    @Override
    public void stop() {
    
    }
    
    
}

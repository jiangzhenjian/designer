package com.fr.start.module;


import com.fr.design.mainframe.DesignerContext;
import com.fr.event.Event;
import com.fr.event.Listener;
import com.fr.general.ComparatorUtils;
import com.fr.module.Activator;
import com.fr.record.analyzer.EnableMetrics;
import com.fr.record.analyzer.Metrics;
import com.fr.start.Designer;
import com.fr.start.ServerStarter;
import com.fr.start.SplashContext;
import com.fr.startup.activators.BasicActivator;
import com.fr.workspace.Workspace;
import com.fr.workspace.WorkspaceEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by juhaoyu on 2018/1/8.
 */
@EnableMetrics
public class DesignerStartup extends Activator {

    @Override
    @Metrics
    public void start() {
    
        startSub(PreStartActivator.class);
        //启动基础部分
        startSub(BasicActivator.class);
        final String[] args = getModule().upFindSingleton(StartupArgs.class).get();
        final Designer designer = new Designer(args);
    
        startSub(DesignerWorkspaceProvider.class);
        registerEnvListener();
        //启动env
        startSub(EnvBasedModule.class);
        //designer模块启动好后，查看demo
        browserDemo();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
        
            @Override
            public void run() {
            
                designer.show(args);
                DesignerContext.getDesignerFrame().getProgressDialog().dispose();
            }
        });
        service.shutdown();
        DesignerContext.getDesignerFrame().setVisible(true);
        //启动画面结束
        SplashContext.getInstance().hide();
    
        DesignerContext.getDesignerFrame().getProgressDialog().setVisible(true);
        startSub(StartFinishActivator.class);
    
    }
    
    private void browserDemo() {
        
        final String[] args = getModule().upFindSingleton(StartupArgs.class).get();
        
        if (args != null) {
            for (String arg : args) {
                if (ComparatorUtils.equals(arg, "demo")) {
                    ServerStarter.browserDemoURL();
                    break;
                }
            }
        }
    }
    
    /**
     * 切换环境时，重新启动所有相关模块
     */
    private void registerEnvListener() {

        listenEvent(WorkspaceEvent.BeforeSwitch, new Listener<Workspace>() {

            @Override
            public void on(Event event, Workspace param) {

                getSub(EnvBasedModule.class).stop();
            }
        });
        listenEvent(WorkspaceEvent.AfterSwitch, new Listener<Workspace>() {

            @Override
            public void on(Event event, Workspace param) {

                getSub(EnvBasedModule.class).start();
            }
        });
    }


    @Override
    public void stop() {

    }
}

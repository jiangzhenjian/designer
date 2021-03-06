package com.fr.design.mainframe.app;

import com.fr.design.mainframe.App;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.module.Activator;
import com.fr.module.extension.Prepare;

import java.util.List;

/**
 * Created by juhaoyu on 2018/6/27.
 */
public class DesignerAppActivator extends Activator implements Prepare {

    @Override
    public void start() {

        List<App> appList = rightCollectMutable(App.KEY);
        for (App app : appList) {
            DesignerFrame.registApp(app);
        }
    }

    @Override
    public void stop() {

        List<App> appList = rightCollectMutable(App.KEY);
        for (App app : appList) {
            DesignerFrame.removeApp(app);
        }
    }

    @Override
    public void prepare() {

        addMutable(App.KEY, new CptApp(), new FormApp(), new XlsApp(), new XlsxApp());

    }
}

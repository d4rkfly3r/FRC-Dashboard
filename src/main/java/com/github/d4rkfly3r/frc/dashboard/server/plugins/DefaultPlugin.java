package com.github.d4rkfly3r.frc.dashboard.server.plugins;

import com.github.d4rkfly3r.frc.dashboard.api.Inject;
import com.github.d4rkfly3r.frc.dashboard.api.Listener;
import com.github.d4rkfly3r.frc.dashboard.api.Plugin;
import com.github.d4rkfly3r.frc.dashboard.api.events.PluginInitEvent;
import com.github.d4rkfly3r.frc.dashboard.api.util.Logger;
import com.github.d4rkfly3r.frc.dashboard.server.MainGUI;
import com.github.d4rkfly3r.frc.dashboard.server.modules.ProgressModule;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
@Plugin(name = "General Plugin Example")
public class DefaultPlugin {

    @Inject
    ProgressModule progressModule;

    @Inject
    Logger logger;

    @Inject
    MainGUI mainGUI;

    @Listener
    public void onInit(PluginInitEvent event) {
        logger.debug("Success");
        progressModule.setLocation(5, 5);
        mainGUI.add(progressModule);
        progressModule.repaint();
    }
}

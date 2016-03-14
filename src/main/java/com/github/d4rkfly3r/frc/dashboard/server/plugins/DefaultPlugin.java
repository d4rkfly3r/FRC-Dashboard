package com.github.d4rkfly3r.frc.dashboard.server.plugins;

import com.github.d4rkfly3r.frc.dashboard.api.Inject;
import com.github.d4rkfly3r.frc.dashboard.api.Listener;
import com.github.d4rkfly3r.frc.dashboard.api.Plugin;
import com.github.d4rkfly3r.frc.dashboard.api.events.PluginInitEvent;
import com.github.d4rkfly3r.frc.dashboard.api.util.DragListener;
import com.github.d4rkfly3r.frc.dashboard.api.util.Logger;
import com.github.d4rkfly3r.frc.dashboard.server.MainGUI;
import com.github.d4rkfly3r.frc.dashboard.server.modules.HorizontalProgressModule;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
@Plugin(name = "General Plugin Example")
public class DefaultPlugin {

    @Inject
    HorizontalProgressModule horizontalProgressModule;

    @Inject
    Logger logger;

    @Inject
    MainGUI mainGUI;

    @Listener
    public void onInit(PluginInitEvent event) {
        DragListener dragListener = new DragListener();
        horizontalProgressModule.addMouseMotionListener(dragListener);
        horizontalProgressModule.addMouseListener(dragListener);
        horizontalProgressModule.addMouseWheelListener(e -> horizontalProgressModule.setValue(horizontalProgressModule.getValue() + (e.getWheelRotation())));

        logger.debug("Success");
        horizontalProgressModule.setLocation(5, 5);
        mainGUI.add(horizontalProgressModule);
        horizontalProgressModule.repaint();
    }
}

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016. Joshua Freedman
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.d4rkfly3r.frc.dashboard.server.plugins;

import com.github.d4rkfly3r.frc.dashboard.api.Inject;
import com.github.d4rkfly3r.frc.dashboard.api.Listener;
import com.github.d4rkfly3r.frc.dashboard.api.Plugin;
import com.github.d4rkfly3r.frc.dashboard.api.events.PluginInitEvent;
import com.github.d4rkfly3r.frc.dashboard.api.util.DragListener;
import com.github.d4rkfly3r.frc.dashboard.api.util.Logger;
import com.github.d4rkfly3r.frc.dashboard.server.MainGUI;
import com.github.d4rkfly3r.frc.dashboard.server.modules.HorizontalProgressModule;

import javax.swing.*;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
@Plugin(name = "General Plugin Example")
public class DefaultPlugin {

    @Inject
    HorizontalProgressModule horizontalProgressModule;

    JProgressBar jProgressBar = new JProgressBar();

    @Inject
    Logger logger;

    @Inject
    MainGUI mainGUI;

    @Listener
    public void onInit(PluginInitEvent event) {
        DragListener dragListener = new DragListener();
        horizontalProgressModule.addMouseListener(dragListener);
        horizontalProgressModule.addMouseMotionListener(dragListener);
        horizontalProgressModule.addMouseWheelListener(e -> horizontalProgressModule.setValue(horizontalProgressModule.getValue() + (e.getWheelRotation())));
        horizontalProgressModule.setLocation(50, 50);
        mainGUI.add(horizontalProgressModule);
        horizontalProgressModule.repaint();

        new Thread(() -> {
            while (true) {
                int nVal = horizontalProgressModule.getValue() + 1;
                nVal = nVal > 100 ? 0 : nVal;
                horizontalProgressModule.setValue(nVal);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}

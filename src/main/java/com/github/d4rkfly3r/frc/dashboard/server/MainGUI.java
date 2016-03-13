package com.github.d4rkfly3r.frc.dashboard.server;

import javax.swing.*;
import java.awt.*;

/**
 * Created by d4rkfly3r on 3/13/2016.
 * Project: FRC-Dashboard-Server
 */
public class MainGUI extends JFrame {
    private static final int SCALE = 400;

    private static MainGUI ourInstance = new MainGUI();

    public static MainGUI getInstance() {
        return ourInstance;
    }

    private MainGUI() {
        super("Main GUI");
        this.setSize(5 * SCALE, 3 * SCALE);
        this.setUndecorated(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public MainGUI lockWindow() {
        this.setVisible(false);
        this.setAlwaysOnTop(true);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, 2 * (Toolkit.getDefaultToolkit().getScreenSize().height / 3));
        this.setVisible(true);
        return this;
    }

    public MainGUI unlockWindow() {
        this.setVisible(false);
        this.setAlwaysOnTop(false);
        this.setSize(5 * SCALE, 3 * SCALE);
        this.setVisible(true);
        return this;
    }

    public MainGUI setup() {
//        PluginBus.getInstance().fireEvent(new Packet99(PluginBus.getInstance().plugins));
        return this;
    }

}

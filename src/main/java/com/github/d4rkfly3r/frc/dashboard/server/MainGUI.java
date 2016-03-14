package com.github.d4rkfly3r.frc.dashboard.server;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

/**
 * Created by d4rkfly3r on 3/13/2016.
 * Project: FRC-Dashboard-Server
 */
public class MainGUI extends JFrame {
    private static final int SCALE = 300;

    private static MainGUI ourInstance = new MainGUI();

    @Nonnull
    public static MainGUI getInstance() {
        return ourInstance;
    }

    private MainGUI() {
        super("Main GUI");
        this.setSize(5 * SCALE, 3 * SCALE);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Nonnull
    public MainGUI lockWindow() {
        this.setVisible(false);
        this.setAlwaysOnTop(true);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, 2 * (Toolkit.getDefaultToolkit().getScreenSize().height / 3));
        this.setVisible(true);
        return this;
    }

    @Nonnull
    public MainGUI unlockWindow() {
        this.setVisible(false);
        this.setAlwaysOnTop(false);
        this.setSize(5 * SCALE, 3 * SCALE);
        this.setVisible(true);
        return this;
    }

    @Nonnull
    public MainGUI setup() {
//        applyStyles();
        this.setVisible(true);
        return this;
    }
/*
    private void applyStyles() {
        Gson gson = new Gson();
        Styles styles = gson.fromJson("{\"styles\":{\"progressBar\":{\"background-color\":\"orange\",\"color\":\"yeller\"}}}", Styles.class);
        System.out.println(styles.styles);
        getAllComponents(this).stream().filter(component -> styles.styles.containsKey(component.getName())).forEach(component -> {
            System.out.println("Style Found");
            styles.styles.get(component.getName()).forEach((s, s2) -> {
                switch (s.toLowerCase()) {
                    case "color":

                        break;
                }
            });
        });
    }

    @Nonnull
    public static List<Component> getAllComponents(@Nonnull final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container) {
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        return compList;
    }


    class Styles {

        HashMap<String, HashMap<String, String>> styles = new HashMap<String, HashMap<String, String>>() {{
            put(".lols", new HashMap<String, String>() {{
                put("color", "red");
                put("background-color", "blue");
            }});
        }};
    }

    */


}

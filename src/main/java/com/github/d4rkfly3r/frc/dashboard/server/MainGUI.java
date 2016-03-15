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
        this.setLayout(null);
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
        this.setVisible(true);
        return this;
    }
}

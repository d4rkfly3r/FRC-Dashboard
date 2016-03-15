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

import com.github.d4rkfly3r.frc.dashboard.api.events.Event;
import com.github.d4rkfly3r.frc.dashboard.api.events.PluginInitEvent;
import com.github.d4rkfly3r.frc.dashboard.api.util.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by d4rkfly3r on 3/13/2016.
 * Project: FRC-Dashboard-Server
 */
public class MainServer {

    private static boolean running = true;
    private static ThreadGroup tg = new ThreadGroup("FRC-Dashboard Threads");

    public MainServer() {

        Logger logger = new Logger();
        ModuleBus.getInstance().init();
        PluginBus.getInstance().init();
        ModuleBus.getInstance().modules.forEach((aClass, o) -> Injector.getInstance().inject(o));
        Injector.getInstance().inject(MainGUI.getInstance());
        Injector.getInstance().inject(Logger.class);

        PluginBus.getInstance().fireEvent(new PluginInitEvent(PluginBus.getInstance().plugins));

        try {
            ServerSocket serverSocket = new ServerSocket(7093, 3);
            Socket client;
            MainGUI.getInstance().setup();

            while (!serverSocket.isClosed()) {
                client = serverSocket.accept();
                final Socket finalClient = client;
                new Thread(tg, () -> {
                    logger.debug("Thread started: " + finalClient.toString());
                    try (ObjectInputStream objectInputStream = new ObjectInputStream(finalClient.getInputStream())) {
                        while (finalClient.isConnected() && MainServer.isRunning()) {
                            try {
                                Object unknown = objectInputStream.readObject();
                                if (unknown instanceof Event) {
                                    PluginBus.getInstance().fireEvent((Event) unknown);
                                }
                            } catch (EOFException ignored) {
                                logger.debugError(ignored.getMessage());
                            } catch (SocketException e0) {
                                break;
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, "Client (P: " + client.getPort() + ")").start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRunning() {
        return running;
    }

    public static void main(String[] args) {
        new MainServer();
    }
}

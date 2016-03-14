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
        ModuleBus.getInstance().modules.forEach((aClass, o) -> {
            Injector.getInstance().inject(o);
        });
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

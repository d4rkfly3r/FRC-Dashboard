package com.github.d4rkfly3r.frc.dashboard.server;

import com.github.d4rkfly3r.frc.dashboard.api.events.Event;
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

    public MainServer() {
        Logger logger = new Logger();
        PluginBus.getInstance().init();
        Injector.getInstance().inject(MainGUI.getInstance());
        Injector.getInstance().inject(Logger.class);
        try {
            ServerSocket serverSocket = new ServerSocket(7093, 3);
            Socket client;
            MainGUI.getInstance().setup();

            while (!serverSocket.isClosed()) {
                client = serverSocket.accept();
                try (ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream())) {
                    while (client.isConnected()) {
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
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

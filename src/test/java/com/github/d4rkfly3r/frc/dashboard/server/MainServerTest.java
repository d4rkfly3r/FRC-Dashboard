package com.github.d4rkfly3r.frc.dashboard.server;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Joshua on 3/13/2016.
 * Project: FRC-Dashboard-Server
 */
public class MainServerTest {

    @Test
    public void serverTest() {
        new MainServer();
    }

    @Test
    public void clientTest() throws IOException {
        Socket socket = new Socket();
        Socket socket2 = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 7093), 10000);
        socket2.connect(new InetSocketAddress("127.0.0.1", 7093), 10000);
    }
}
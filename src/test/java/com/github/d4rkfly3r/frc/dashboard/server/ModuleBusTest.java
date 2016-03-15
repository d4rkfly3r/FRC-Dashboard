package com.github.d4rkfly3r.frc.dashboard.server;

import org.junit.Test;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
public class ModuleBusTest {

    @Test
    public void testInit() throws Exception {
        ModuleBus.getInstance().init();
    }
}
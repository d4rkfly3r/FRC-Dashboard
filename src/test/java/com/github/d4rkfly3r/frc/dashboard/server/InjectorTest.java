package com.github.d4rkfly3r.frc.dashboard.server;

import com.github.d4rkfly3r.frc.dashboard.api.util.Logger;
import org.junit.Test;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
public class InjectorTest {

    // TODO: Finish these tests

    @Test
    public void testInject() throws Exception {
        Injector.getInstance().inject(MainGUI.getInstance());
    }

    @Test
    public void testInjectObj() throws Exception {
        Injector.getInstance().inject(Logger.class);
    }
}
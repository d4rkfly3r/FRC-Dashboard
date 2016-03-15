package com.github.d4rkfly3r.frc.dashboard.server;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
public class MainGUITest {

    @Test
    public void testLockWindow() throws Exception {
        Assert.assertNotEquals(MainGUI.getInstance().lockWindow(), null);
        Assert.assertEquals(MainGUI.getInstance().lockWindow(), MainGUI.getInstance());
    }

    @Test
    public void testUnlockWindow() throws Exception {
        Assert.assertNotEquals(MainGUI.getInstance().unlockWindow(), null);
        Assert.assertEquals(MainGUI.getInstance().unlockWindow(), MainGUI.getInstance());
    }

    @Test
    public void testSetup() throws Exception {
        Assert.assertNotEquals(MainGUI.getInstance().setup(), null);
        Assert.assertEquals(MainGUI.getInstance().setup(), MainGUI.getInstance());
    }
}
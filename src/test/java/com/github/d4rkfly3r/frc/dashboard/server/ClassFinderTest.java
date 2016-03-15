package com.github.d4rkfly3r.frc.dashboard.server;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
public class ClassFinderTest {

    @Test
    public void testGetPluginClasses() throws Exception {
        Assert.assertNotEquals(ClassFinder.getPluginClasses(), null);
    }

    @Test
    public void testGetModuleClasses() throws Exception {
        Assert.assertNotEquals(ClassFinder.getModuleClasses(), null);
    }
}
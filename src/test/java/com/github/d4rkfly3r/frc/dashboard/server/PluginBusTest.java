package com.github.d4rkfly3r.frc.dashboard.server;

import com.github.d4rkfly3r.frc.dashboard.api.Listener;
import com.github.d4rkfly3r.frc.dashboard.api.events.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
public class PluginBusTest {

    @Before
    public void setUp() throws Exception {
        ModuleBus.getInstance().init();
    }

    @Test
    public void testFireEvent() throws Exception {
        PluginBus.getInstance().plugins.put(Object.class, new Object() {
            @Listener
            public void onModuleInit(Event event) {
                System.out.println("Event: " + event);
            }
        });
        PluginBus.getInstance().fireEvent(new ModuleInitEvent());
        PluginBus.getInstance().fireEvent(new PluginPreInitEvent());
        PluginBus.getInstance().fireEvent(new PluginInitEvent(PluginBus.getInstance().plugins));
        PluginBus.getInstance().fireEvent(new PacketEvent());
    }

    @Test
    public void testFireEventToObject() throws Exception {
        PluginBus.getInstance().fireEventToObject(new Object() {
            @Listener
            public void onPacket(PacketEvent event) {
                System.out.println("Event: " + event);
            }
        }, new PacketEvent());
    }

    @Test
    public void testInit() throws Exception {
        PluginBus.getInstance().init();
    }
}
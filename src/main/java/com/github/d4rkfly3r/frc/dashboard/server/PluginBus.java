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

import com.github.d4rkfly3r.frc.dashboard.api.Listener;
import com.github.d4rkfly3r.frc.dashboard.api.Plugin;
import com.github.d4rkfly3r.frc.dashboard.api.events.Event;
import com.github.d4rkfly3r.frc.dashboard.api.events.PluginPreInitEvent;
import com.github.d4rkfly3r.frc.dashboard.api.util.Logger;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by d4rkfly3r on 3/13/2016.
 * Project: FRC-Dashboard-Server
 */
public class PluginBus {

    private Logger logger = new Logger(PluginBus.class);

    private static PluginBus ourInstance = new PluginBus();

    public static PluginBus getInstance() {
        return ourInstance;
    }

    public final HashMap<Class<?>, Object> plugins;

    private PluginBus() {
        plugins = new HashMap<>();
    }

    @Nonnull
    public PluginBus fireEvent(@Nonnull Event event) {
        plugins.forEach((aClass, instance) -> {
            List<Method> methods = new ArrayList<>();
            Collections.addAll(methods, aClass.getDeclaredMethods());
            invokeMethods(instance, methods, event);
        });
        return this;
    }

    private void invokeMethods(@Nonnull Object instance, @Nonnull List<Method> methods, @Nonnull Event event) {
        methods.stream().forEach(method -> {
            if (method.isAnnotationPresent(Listener.class)) {
                if (method.getParameterCount() > 0) {
                    if (method.getParameterTypes()[0] == event.getClass()) {
                        try {
                            method.invoke(instance, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Nonnull
    public PluginBus fireEventToObject(@Nonnull Object instance, @Nonnull Event event) {
        List<Method> methods = new ArrayList<>();
        Collections.addAll(methods, instance.getClass().getDeclaredMethods());
        invokeMethods(instance, methods, event);
        return this;
    }

    public void init() {
        ClassFinder.getClasses(Plugin.class).forEach(aClass1 -> {
            try {
                plugins.put(aClass1, aClass1.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        logger.log("Plugins: ");
        plugins.forEach((aClass, instance) -> {
            logger.log("\t" + aClass.getName());
            fireEventToObject(instance, new PluginPreInitEvent());
        });
    }
}

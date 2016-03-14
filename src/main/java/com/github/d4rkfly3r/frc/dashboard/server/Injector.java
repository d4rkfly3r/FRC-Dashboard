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

import com.github.d4rkfly3r.frc.dashboard.api.Inject;
import com.github.d4rkfly3r.frc.dashboard.api.util.Logger;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by d4rkfly3r on 3/13/2016.
 * Project: FRC-Dashboard-Server
 */
class Injector {
    private Logger logger = new Logger(Injector.class);

    private static Injector ourInstance = new Injector();

    private ArrayList<Class<?>> nonUniqueFields = new ArrayList<Class<?>>() {{
        add(MainGUI.class);
    }};

    @Nonnull
    public static Injector getInstance() {
        return ourInstance;
    }

    private Injector() {
    }

    @Nonnull
    public <T> Injector inject(@Nonnull T t) {
        PluginBus.getInstance().plugins.forEach((aClass, o) -> {
            List<Field> fields = new ArrayList<>();
            Collections.addAll(fields, aClass.getDeclaredFields());
            fields.stream()
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .filter(field -> field.getType().equals(t.getClass()))
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            if (field.getAnnotation(Inject.class).unique() && !nonUniqueFields.contains(field.getType())) {
                                field.set(o, t.getClass().getConstructor().newInstance());
                            } else {
                                field.set(o, t);
                            }
                            logger.debug("Injecting " + Logger.ANSI_GREEN + t.getClass().getName() + Logger.ANSI_RESET + " into field " + Logger.ANSI_GREEN + field.getDeclaringClass().getName() + ":" + field.getName());
                        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                            e.printStackTrace();
                        }
                    });
        });
        return this;
    }

    @Nonnull
    public Injector inject(@Nonnull Class<?> t) {
        PluginBus.getInstance().plugins.forEach((aClass, o) -> {
            List<Field> fields = new ArrayList<>();
            Collections.addAll(fields, aClass.getDeclaredFields());
            fields.stream()
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .filter(field -> field.getType().equals(t))
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            field.set(o, t.getConstructor(Class.class).newInstance(aClass));
                            logger.debug(field.get(o));
                        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    });
        });
        return this;
    }
}

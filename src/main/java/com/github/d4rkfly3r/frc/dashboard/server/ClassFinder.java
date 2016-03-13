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

import com.github.d4rkfly3r.frc.dashboard.api.Module;
import com.github.d4rkfly3r.frc.dashboard.api.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Created by d4rkfly3r on 3/13/2016.
 * Project: FRC-Dashboard-Server
 */
class ClassFinder {

    @Nonnull
    static Vector<Class<?>> getPluginClasses() {
        return findSubclasses(Plugin.class, getClasspathLocations());
    }

    @Nonnull
    static Vector<Class<?>> getModuleClasses() {
        return findSubclasses(Module.class, getClasspathLocations());
    }

    @Nonnull
    private static Vector<Class<?>> findSubclasses(@Nonnull Class<?> annotationClass, @Nonnull Map<URL, String> locations) {
        Vector<Class<?>> v = new Vector<>();
        Vector<Class<?>> w;

        for (URL url : locations.keySet()) {
            w = findSubclasses(url, locations.get(url), annotationClass);
            if ((w.size() > 0)) v.addAll(w);
        }

        return v;
    }

    @Nonnull
    private static Vector<Class<?>> findSubclasses(@Nonnull URL location, @Nonnull String packageName, @Nonnull Class<?> annotationClass) {

        if (location.getFile().contains("/jre/lib/") || location.getFile().contains("idea_rt.jar"))
            return new Vector<>();

        System.out.println(location);

        Map<Class<?>, URL> thisResult = new TreeMap<>((c1, c2) -> String.valueOf(c1).compareTo(String.valueOf(c2)));
        Vector<Class<?>> v = new Vector<>();

        // TODO: double-check for null search class
        String fqcn = annotationClass.getName();

        List<URL> knownLocations = new ArrayList<>();
        knownLocations.add(location);
        // TODO: add getResourceLocations() to this list

        for (URL url : knownLocations) {
            File directory = new File(url.getFile());

            if (directory.exists()) {
                String[] files = directory.list();
                for (String file : files) {
                    if (file.endsWith(".class")) {
                        String classname = file.substring(0, file.length() - 6);
                        try {
                            Class c = Class.forName(packageName + "." + classname);
                            if (c.isAnnotationPresent(annotationClass) && !fqcn.equals(packageName + "." + classname)) {
                                thisResult.put(c, url);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } else {
                try {
                    JarURLConnection conn = (JarURLConnection) url.openConnection();
                    JarFile jarFile = conn.getJarFile();

                    Enumeration<JarEntry> e = jarFile.entries();
                    while (e.hasMoreElements()) {
                        JarEntry entry = e.nextElement();
                        String entryName = entry.getName();

                        if (!entry.isDirectory() && entryName.endsWith(".class")) {
                            String classname = entryName.substring(0, entryName.length() - 6);
                            if (classname.startsWith("/")) {
                                classname = classname.substring(1);
                            }
                            classname = classname.replace('/', '.');

                            try {
                                // TODO: verify this block
                                Class c = Class.forName(classname);

                                if (c.isAnnotationPresent(annotationClass) && !fqcn.equals(classname)) {
                                    thisResult.put(c, url);
                                }
                            } catch (Error | Exception ignored) {
                            }
                        }
                    }
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }
            }
        }

        v.addAll(thisResult.keySet().stream().collect(Collectors.toList()));
        return v;
    }

    @Nonnull
    private static Map<URL, String> getClasspathLocations() {
        Map<URL, String> map = new TreeMap<>((u1, u2) -> String.valueOf(u1).compareTo(String.valueOf(u2)));
        File file;

        String pathSep = System.getProperty("path.separator");
        String classpath = System.getProperty("java.class.path");

        StringTokenizer st = new StringTokenizer(classpath, pathSep);
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            file = new File(path);
            include(null, file, map);
        }

        return map;
    }

    private static void include(@Nullable String name, @Nonnull File file, @Nonnull Map<URL, String> map) {
        if (!file.exists()) return;
        if (!file.isDirectory()) {
            includeJar(file, map);
            return;
        }

        if (name == null) {
            name = "";
        } else {
            name += ".";
        }

        File[] dirs = file.listFiles(f -> f.exists() && f.isDirectory());
        for (File dir : dirs) {
            try {
                map.put(new URL("file://" + dir.getCanonicalPath()), name + dir.getName());
            } catch (IOException ioe) {
                return;
            }
            include(name + dir.getName(), dir, map);
        }
    }

    private static void includeJar(@Nonnull File file, @Nonnull Map<URL, String> map) {
        if (file.isDirectory()) return;

        URL jarURL;
        JarFile jar;
        try {
            jarURL = new URL("file:/" + file.getCanonicalPath());
            jarURL = new URL("jar:" + jarURL.toExternalForm() + "!/");
            JarURLConnection conn = (JarURLConnection) jarURL.openConnection();
            jar = conn.getJarFile();
        } catch (Exception e) {
            return;
        }

        if (jar == null) return;

        map.put(jarURL, "");

        Enumeration<JarEntry> e = jar.entries();
        while (e.hasMoreElements()) {
            JarEntry entry = e.nextElement();

            if (entry.isDirectory()) {
                if (entry.getName().toUpperCase().equals("META-INF/")) continue;

                try {
                    map.put(new URL(jarURL.toExternalForm() + entry.getName()), packageNameFor(entry));
                } catch (MalformedURLException ignored) {
                }
            }
        }
    }

    @Nonnull
    private static String packageNameFor(@Nullable JarEntry entry) {
        if (entry == null) return "";
        String s = entry.getName();
        if (s == null) return "";
        if (s.length() == 0) return s;
        if (s.startsWith("/")) s = s.substring(1, s.length());
        if (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s.replace('/', '.');
    }
}

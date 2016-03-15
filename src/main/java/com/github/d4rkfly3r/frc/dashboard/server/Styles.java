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

import com.google.gson.Gson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
public class Styles {
    public HashMap<String, HashMap<String, String>> styles = new HashMap<>();

    public static HashMap<String, HashMap<String, String>> fStyles = new HashMap<>();

    static {
        Gson gson = new Gson();

        try {
            File file = new File("style.json");
            if (!file.exists()) {
                System.out.println("File Missing! Created: " + file.createNewFile());
            }
            fStyles = gson.fromJson(new FileReader(file), Styles.class).styles;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nonnull
    public static <T> String getOrDefault(String s, @Nonnull T i) {
        String[] st = s.split("\\|");
        if (fStyles.containsKey(st[0])) {
            if (fStyles.get(st[0]).containsKey(st[1])) {
                return fStyles.get(st[0]).get(st[1]);
            }
            return String.valueOf(i);
        }
        return String.valueOf(i);
    }

    @Nullable
    public static String getOrNull(String s) {
        String[] st = s.split("\\|");
        if (fStyles.containsKey(st[0])) {
            if (fStyles.get(st[0]).containsKey(st[1])) {
                return fStyles.get(st[0]).get(st[1]);
            }
            return null;
        }
        return null;
    }
}

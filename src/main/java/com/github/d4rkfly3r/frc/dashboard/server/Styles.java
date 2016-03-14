package com.github.d4rkfly3r.frc.dashboard.server;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
public class Styles {
    public HashMap<String, HashMap<String, String>> styles = new HashMap<String, HashMap<String, String>>() {{

    }};

    public static HashMap<String, HashMap<String, String>> fStyles = new HashMap<String, HashMap<String, String>>() {{

    }};

    static {
        Gson gson = new Gson();
        try {
            new File("style.json").createNewFile();
            fStyles = gson.fromJson(new FileReader(new File("style.json")), Styles.class).styles;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> String getOrDef(String s, T i) {
        String[] st = s.split("\\|");
        if (fStyles.containsKey(st[0])) {
            if (fStyles.get(st[0]).containsKey(st[1])) {
                return fStyles.get(st[0]).get(st[1]);
            }
            return String.valueOf(i);
        }
        return String.valueOf(i);
    }
}

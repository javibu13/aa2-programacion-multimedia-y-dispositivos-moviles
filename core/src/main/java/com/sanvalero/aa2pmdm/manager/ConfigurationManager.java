package com.sanvalero.aa2pmdm.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import static com.sanvalero.aa2pmdm.util.Constants.GAME_NAME;

public class ConfigurationManager {

    private static Preferences prefs;

    public static void loadPreferences() {
        prefs = Gdx.app.getPreferences(GAME_NAME);
    }

    public static boolean isSoundEnabled() {
        return prefs.getBoolean("sound", true);
    }
}
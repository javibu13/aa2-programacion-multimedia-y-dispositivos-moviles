package com.sanvalero.aa2pmdm.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static com.sanvalero.aa2pmdm.util.Constants.GAME_NAME;

public class WindowSize {
    public static void resize(Stage stage, int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.getViewport().setWorldSize(width, height);
        if (!Gdx.app.getPreferences(GAME_NAME).getBoolean("fullscreen", false)) {
            Gdx.app.getPreferences(GAME_NAME).putInteger("windowWidth", width).putInteger("windowHeight", height).flush();
        }
    }

    public static void setFullScreen(Stage stage) {
        
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static void setWindowed(Stage stage, int width, int height) {
        Gdx.graphics.setWindowedMode(width, height);
        resize(stage, width, height);
    }

    public static void setWindowed(Stage stage) {
        int width = Gdx.app.getPreferences(GAME_NAME).getInteger("windowWidth", 640);
        int height = Gdx.app.getPreferences(GAME_NAME).getInteger("windowHeight", 360);
        setWindowed(stage, width, height);
    }
}

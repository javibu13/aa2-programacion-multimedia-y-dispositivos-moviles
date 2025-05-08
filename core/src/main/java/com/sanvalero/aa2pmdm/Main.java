package com.sanvalero.aa2pmdm;

import com.badlogic.gdx.Game;
import com.sanvalero.aa2pmdm.screen.SplashScreen;

public class Main extends Game {

    public boolean pause;
    public boolean debug;
    public static float musicVolume = 0.5f;
    public static float soundVolume = 0.5f;

    public Main() {
        this.pause = false;
        this.debug = false;
    }

    @Override
    public void create() {
        setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public static void setMusicVolume(float volume) {
        musicVolume = volume;
    }
    public static void setSoundVolume(float volume) {
        soundVolume = volume;
    }
    public static float getMusicVolume() {
        return musicVolume;
    }
    public static float getSoundVolume() {
        return soundVolume;
    }
}

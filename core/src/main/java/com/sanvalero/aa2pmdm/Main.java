package com.sanvalero.aa2pmdm;

import com.badlogic.gdx.Game;
import com.sanvalero.aa2pmdm.screen.SplashScreen;

public class Main extends Game {

    public boolean pause;

    public Main() {
        this.pause = false;
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
}

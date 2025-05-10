package com.sanvalero.aa2pmdm.screen;

import com.badlogic.gdx.Screen;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.manager.*;
import com.sanvalero.aa2pmdm.util.WindowSize;

public class GameScreen implements Screen {

    private Main game;
    private int level;
    private LogicManager logicManager;
    private RenderManager renderManager;
    private LevelManager levelManager;

    public GameScreen(Main game, int level) {
        this.game = game;
        this.level = level;
        ConfigurationManager.loadPreferences();
        loadManagers();
    }

    private void loadManagers() {
        logicManager = new LogicManager(game, level);
        levelManager = new LevelManager(logicManager, level);
        renderManager = new RenderManager(logicManager, levelManager.getLevelMap());
    }

    @Override
    public void show() {
        game.pause = false;
    }

    @Override
    public void render(float delta) {
        if (!game.pause) {
            logicManager.update(delta);
        }
        renderManager.render();
    }

    @Override
    public void resize(int i, int i1) {
        // WindowSize.resize(renderManager.getStage(), i, i1);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

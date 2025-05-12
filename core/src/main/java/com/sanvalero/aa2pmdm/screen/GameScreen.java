package com.sanvalero.aa2pmdm.screen;

import com.badlogic.gdx.Screen;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.manager.*;

public class GameScreen implements Screen {

    private Main game;
    private int level;
    private LogicManager logicManager;
    private RenderManager renderManager;
    private LevelManager levelManager;
    private CameraManager cameraManager;

    public GameScreen(Main game, int level) {
        this.game = game;
        this.level = level;
        ConfigurationManager.loadPreferences();
        loadManagers();
    }

    private void loadManagers() {
        logicManager = new LogicManager(game, level);
        levelManager = new LevelManager(logicManager, level);
        // Update level in LogicManager after loading LevelManager because it can be changed in case of Game Over
        this.level = levelManager.getLevel();
        logicManager.setLevel(level);
        cameraManager = new CameraManager(levelManager.getLevelMap());
        renderManager = new RenderManager(logicManager, cameraManager, levelManager.getLevelMap(), this);
    }

    public void setScreenToLeaderboard() {
        game.setScreen(new LeaderboardScreen(game));
    }

    public boolean isPause() {
        return game.pause;
    }

    @Override
    public void show() {
        game.pause = false;
    }

    @Override
    public void render(float delta) {
        if (!game.pause) {
            logicManager.update(delta);
            if (logicManager.isGameOver()) {
                // Change to Game Over screen
                System.out.println("Game Over! You lose!");
                // TODO: Dispose?
                game.setScreen(new GameOverScreen(game));
            }
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

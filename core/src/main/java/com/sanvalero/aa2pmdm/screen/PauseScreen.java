package com.sanvalero.aa2pmdm.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.util.WindowSize;

public class PauseScreen implements Screen {

    private Main game;
    private Screen backScreen;
    private Stage stage;

    public PauseScreen(Main game, Screen backScreen) {
        this.game = game;
        this.backScreen = backScreen;
    }

    private void loadStage() {
        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage(new ScreenViewport());

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Super Lobster.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        BitmapFont titleFont = generator.generateFont(parameter);
        generator.dispose();
        VisLabel title = new VisLabel("PAUSE", new LabelStyle(titleFont, Color.WHITE));

        VisTextButton resumeButton = new VisTextButton("Resume");
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(backScreen);
            }
        });

        VisTextButton configButton = new VisTextButton("Settings");
        configButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ConfigurationScreen(game, game.getScreen()));
            }
        });

        VisTextButton menuButton = new VisTextButton("Main Menu");
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                backScreen.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        VisTextButton quitButton = new VisTextButton("Exit Game");
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                Gdx.app.exit();
            }
        });

        table.row();
        table.add(title).center();
        table.row();
        table.add(resumeButton).center();
        table.row();
        table.add(configButton).center();
        table.row();
        table.add(menuButton).center();
        table.row();
        table.add(quitButton).center();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        loadStage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.537f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        // Resize the stage to match the new window size
        WindowSize.resize(stage, i, i1);
        loadStage();
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
        stage.dispose();
    }
}

package com.sanvalero.aa2pmdm.screen;

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

public class GameOverScreen implements Screen {

    private Main game;
    private Stage stage;
    private VisTable table;

    public GameOverScreen(Main game) {
        this.game = game;
    }

    private void loadStage() {
        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage(new ScreenViewport());

        table = new VisTable(true);
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Super Lobster.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        BitmapFont titleFont = generator.generateFont(parameter);
        parameter.size = 24;
        BitmapFont textFont = generator.generateFont(parameter);
        generator.dispose();
        VisLabel title = new VisLabel("GAME OVER", new LabelStyle(titleFont, Color.WHITE));
        VisLabel textLabel = new VisLabel("You lost all your lives", new LabelStyle(textFont, Color.WHITE));
        VisTextButton continueButton = new VisTextButton("Main Menu");
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.row();
        table.add(title).center();
        table.row();
        table.add(textLabel).center();
        table.row();
        table.add(continueButton).center();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        loadStage();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0.2f, 0.537f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(dt);
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

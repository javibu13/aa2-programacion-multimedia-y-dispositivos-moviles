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

public class HowToPlayScreen implements Screen {

    private Main game;
    private Stage stage;
    private VisTable table;
    private Screen backScreen;

    public HowToPlayScreen(Main game, Screen backScreen) {
        this.game = game;
        this.backScreen = backScreen;
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
        VisLabel title = new VisLabel("How To Play", new LabelStyle(titleFont, Color.WHITE));

        VisTextButton backButton = new VisTextButton("Return");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(backScreen);
            }
        });

        VisLabel instructions = new VisLabel(
                  "1. Use the Left and Right arrow keys to move.\n\n"
                + "2. Press Space key or Up arrow key to jump.\n\n"
                + "3. Collect coins to increase score.\n\n"
                + "4. Get the key to open the exit door.\n\n"
                + "5. Avoid enemies and obstacles.\n\n"
                + "6. Reach the exit to complete the level.\n\nESCAPE key or P key to pause the game.\n\n",
                new LabelStyle(textFont, Color.WHITE));
        instructions.setColor(Color.WHITE);

        table.row();
        table.add(title).center();
        table.row();
        table.add(instructions).center().pad(20);
        table.row();
        table.add(backButton).center();

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

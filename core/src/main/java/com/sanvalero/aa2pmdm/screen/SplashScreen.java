package com.sanvalero.aa2pmdm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.manager.R;

import static com.sanvalero.aa2pmdm.util.Constants.GAME_NAME;

public class SplashScreen implements Screen {

    private Main game;
    private Stage stage;
    private boolean splashDone = false;

    private float timer = 1f;   // TODO: Delete this when R.update() is implemented

    public SplashScreen(Main game) {
        this.game = game;

        stage = new Stage();
    }

    @Override
    public void show() {
        if (!VisUI.isLoaded())
            VisUI.load();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Super Lobster.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        BitmapFont titleFont = generator.generateFont(parameter);
        generator.dispose();

        VisLabel title = new VisLabel(GAME_NAME, new LabelStyle(titleFont, Color.WHITE));

        table.row();
        table.add(title).center();

        R.loadAllResources();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.537f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_CLEAR_VALUE);

        stage.act();
        stage.draw();

        if (R.update()) {
            if (splashDone) {
                game.setScreen(new MainMenuScreen(game));
            }
        }

        timer -= delta;
        if (timer <= 0) {
            splashDone = true;
        }
    }

    @Override
    public void resize(int i, int i1) {

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

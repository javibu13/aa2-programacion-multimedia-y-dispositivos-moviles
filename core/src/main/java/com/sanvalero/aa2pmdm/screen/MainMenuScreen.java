package com.sanvalero.aa2pmdm.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.sanvalero.aa2pmdm.manager.R;
import com.sanvalero.aa2pmdm.util.WindowSize;

import static com.sanvalero.aa2pmdm.util.Constants.BACKGROUND_MUSIC;
import static com.sanvalero.aa2pmdm.util.Constants.GAME_NAME;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private Preferences prefs;
    private Main game;

    public MainMenuScreen(Main game) {
        this.game = game;
        prefs = Gdx.app.getPreferences(GAME_NAME);
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
        VisLabel title = new VisLabel(GAME_NAME, new LabelStyle(titleFont, Color.WHITE));

        VisTextButton playButton = new VisTextButton("Play");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new GameScreen(game, 0));
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

        VisTextButton quitButton = new VisTextButton("Exit Game");
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                R.dispose();
                Gdx.app.exit();
            }
        });

        table.row();
        table.add(title).center();
        table.row();
        table.add(playButton).center();
        table.row();
        table.add(configButton).center();
        table.row();
        table.add(quitButton).center();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        loadStage();
        if (prefs.getBoolean("fullscreen", false)) {
            WindowSize.setFullScreen(stage);
        }
        if (prefs.getBoolean("sound", true)) {
            Main.setMusicVolume(0.5f);
            Main.setSoundVolume(0.5f);
            R.getMusic(BACKGROUND_MUSIC).setVolume(Main.getMusicVolume());
        } else {
            Main.setMusicVolume(0f);
            Main.setSoundVolume(0f);
            R.getMusic(BACKGROUND_MUSIC).setVolume(Main.getMusicVolume());
        }
        R.getMusic(BACKGROUND_MUSIC).play();
        R.getMusic(BACKGROUND_MUSIC).setLooping(true);
        // FIXME: Initial window size is not set correctly when MainMenuScreen is shown at the beginning
        //  else {
        //     System.out.println("Windowed mode: " + prefs.getInteger("windowWidth", 640) + "x" + prefs.getInteger("windowHeight", 360));
        //     WindowSize.resize(stage, prefs.getInteger("windowWidth", 640), prefs.getInteger("windowHeight", 360));
        // }
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

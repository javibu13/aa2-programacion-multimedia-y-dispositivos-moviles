package com.sanvalero.aa2pmdm.screen;

import java.time.LocalDateTime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.util.ScoreEntry;
import com.sanvalero.aa2pmdm.util.WindowSize;

public class LeaderboardScreen implements Screen {

    private Main game;
    private Stage stage;
    private VisTable table;
    private Array<ScoreEntry> leaderboard;
    private boolean newEntryRegistered = false;

    public LeaderboardScreen(Main game) {
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
        VisLabel title = new VisLabel("TOP 10 SCORES", new LabelStyle(titleFont, Color.WHITE));

        VisTextButton continueButton = new VisTextButton("Continue");
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        
        // Register the new score and get the leaderboard
        if (!newEntryRegistered) {
            newEntryRegistered = true;
            registerNewScore();
        }

        table.row();
        table.add(title).center();
        for (int i = 0; i < leaderboard.size; i++) {
            ScoreEntry entry = leaderboard.get(i);
            VisLabel entryLabel = new VisLabel((i + 1) + ". " + entry.toString(), new LabelStyle(textFont, entry.isNewEntry() ? Color.YELLOW : Color.WHITE));
            table.row();
            table.add(entryLabel).center();
        }
        table.row();
        table.add(continueButton).center();

        Gdx.input.setInputProcessor(stage);
    }

    private void registerNewScore() {
        // Takes from Main class the player name, score and time to compare with the leaderboard stored in a json file
        // and add the new score if it is in the top 10
        FileHandle file = Gdx.files.local("leaderboard.json");
        // Load existing leaderboard or create new list
        if (file.exists()) {
            leaderboard = new Json().fromJson(Array.class, ScoreEntry.class, file);
        } else {
            leaderboard = new Array<>();
        }
        // Add new score to filter with the previous ones
        ScoreEntry newEntry = new ScoreEntry(Main.playerName, Main.playerScore, Main.playerTime, LocalDateTime.now(), true);
        leaderboard.add(newEntry);
        // Sort by score descending, then time ascending (less time is better)
        leaderboard.sort((a, b) -> {
            if (b.getScore() != a.getScore()) return Integer.compare(b.getScore(), a.getScore());
            return Float.compare(a.getTime(), b.getTime());
        });
        // Keep top 10
        if (leaderboard.size > 10) {
            leaderboard.truncate(10);
        }
        // Save updated leaderboard
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        file.writeString(json.toJson(leaderboard), false);
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

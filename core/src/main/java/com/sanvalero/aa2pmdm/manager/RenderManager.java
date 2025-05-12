package com.sanvalero.aa2pmdm.manager;

import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.entity.Enemy;
import com.sanvalero.aa2pmdm.entity.Item;
import com.sanvalero.aa2pmdm.entity.Spaceship;
import com.sanvalero.aa2pmdm.screen.GameScreen;

public class RenderManager {
    
    private LogicManager logicManager;
    private CameraManager cameraManager;
    private Batch batch;
    private OrthogonalTiledMapRenderer mapRenderer;
    // private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    // UI elements - Game Over
    private Stage uiStage;
    private VisTextField nameField;


    public RenderManager(LogicManager logicManager, CameraManager cameraManager, TiledMap levelMap, GameScreen gameScreen) {
        this.logicManager = logicManager;

        this.mapRenderer = new OrthogonalTiledMapRenderer(levelMap);
        this.batch = mapRenderer.getBatch();

        // camera = new OrthographicCamera();
        // camera.setToOrtho(false, TILE_SIZE * 32, TILE_SIZE * 16);
        // camera.update();
        this.cameraManager = cameraManager;

        shapeRenderer = new ShapeRenderer();

        // Set up the UI stage
        uiStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(null); // Disable input for the UI stage until is shown
        VisTable table = new VisTable(true);
        table.setFillParent(true);
        table.bottom().padBottom(90);
        uiStage.addActor(table);
        // Text field for entering the name
        nameField = new VisTextField("");
        nameField.setMessageText("Enter your name");
        // Button to submit the player name
        VisTextButton continueButton = new VisTextButton("Continue");
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playerName = nameField.getText();
                uiStage.dispose();
                gameScreen.setScreenToLeaderboard();
            }
        });
        table.row();
        table.add(nameField).width(200f).height(50f).pad(10f);
        table.row();
        table.add(continueButton).width(200f).height(50f).pad(10f);
    }

    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        cameraManager.updateCameraPosition(logicManager.player);
        cameraManager.update();
        OrthographicCamera camera = cameraManager.getCamera();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        for (Item item : logicManager.items) {
            if (item.isVisible()) {
                if (item instanceof Spaceship) {
                    batch.draw(item.getCurrentFrame(), item.getPosition().x, item.getPosition().y, 96f, 96f);
                } else {
                    batch.draw(item.getCurrentFrame(), item.getPosition().x, item.getPosition().y);
                }
            }
        }
        for (Enemy enemy : logicManager.enemies) {
            if (enemy.isVisible()) {
                batch.draw(enemy.getCurrentFrame(), enemy.getPosition().x, enemy.getPosition().y);
            }
        }
        batch.draw(logicManager.exit.getCurrentFrame(), logicManager.exit.getPosition().x, logicManager.exit.getPosition().y);
        if (logicManager.player.isVisible()) {
            batch.draw(logicManager.player.getCurrentFrame(), logicManager.player.getPosition().x, logicManager.player.getPosition().y);
        }
        // ...
        
        // Draw HUD elements
        if (logicManager.getLevel() >= 0) {
            Vector2 cameraPositionBottomLeft = cameraManager.getCameraBottomLeft();
            float leftPadding = 4f;
            float topPadding = 4f;
            // // Lives
            for (int i = 0; i < logicManager.player.getMaxHealth(); i++) {
                if (i < logicManager.player.getHealth()) {
                    batch.draw(R.getRegions("heart").get(1), cameraPositionBottomLeft.x + leftPadding + (i * 20f), cameraPositionBottomLeft.y + camera.viewportHeight - 20f - topPadding, 20f, 20f);
                } else {
                    batch.draw(R.getRegions("heart").get(0), cameraPositionBottomLeft.x + leftPadding + (i * 20f), cameraPositionBottomLeft.y + camera.viewportHeight - 20f - topPadding, 20f, 20f);
                }
            }
            // // Score
            batch.draw(R.getTexture("coin"), cameraPositionBottomLeft.x + leftPadding, cameraPositionBottomLeft.y + camera.viewportHeight - 40f - topPadding, 20f, 20f); 
            String score = String.valueOf(logicManager.player.getScore());
            for (int i = 0; i < score.length(); i++) {
                char digit = score.charAt(i);
                // Transform the character to an integer
                int digitValue = digit - '0';
                batch.draw(R.getRegions("number").get(digitValue), cameraPositionBottomLeft.x + leftPadding + 20f + (i * 20f), cameraPositionBottomLeft.y + camera.viewportHeight - 40f - topPadding, 20f, 20f);
            }
            // // Key
            int keySpriteIndex = logicManager.player.isKey() ? 1 : 0;
            batch.draw(R.getRegions("key").get(keySpriteIndex), cameraPositionBottomLeft.x + leftPadding + 3f, cameraPositionBottomLeft.y + camera.viewportHeight - 60f - topPadding, 20f, 20f);
            // // Level
        }
        batch.end();
        
        // Draw player's collision shapes for debugging 
        if (logicManager.isDebugMode()) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            // PLAYER
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(logicManager.player.collisionShape.x, logicManager.player.collisionShape.y, logicManager.player.collisionShape.width, logicManager.player.collisionShape.height);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(logicManager.player.collisionShapeTop.x, logicManager.player.collisionShapeTop.y, logicManager.player.collisionShapeTop.width, logicManager.player.collisionShapeTop.height);
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.rect(logicManager.player.collisionShapeBottom.x, logicManager.player.collisionShapeBottom.y, logicManager.player.collisionShapeBottom.width, logicManager.player.collisionShapeBottom.height);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(logicManager.player.collisionShapeLeft.x, logicManager.player.collisionShapeLeft.y, logicManager.player.collisionShapeLeft.width, logicManager.player.collisionShapeLeft.height);
            shapeRenderer.setColor(Color.ORANGE);
            shapeRenderer.rect(logicManager.player.collisionShapeRight.x, logicManager.player.collisionShapeRight.y, logicManager.player.collisionShapeRight.width, logicManager.player.collisionShapeRight.height);
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.rect(logicManager.player.getItemCollisionShape().x, logicManager.player.getItemCollisionShape().y, logicManager.player.getItemCollisionShape().width, logicManager.player.getItemCollisionShape().height);
            // EXIT
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(logicManager.exit.getCollisionShape().x, logicManager.exit.getCollisionShape().y, logicManager.exit.getCollisionShape().width, logicManager.exit.getCollisionShape().height);
            // ITEMS
            for (Item item : logicManager.items) {
                if (item.isActive()) {
                    shapeRenderer.setColor(Color.BLACK);
                    shapeRenderer.rect(item.getCollisionShape().x, item.getCollisionShape().y, item.getCollisionShape().width, item.getCollisionShape().height);
                }
            }
            // ENEMIES
            for (Enemy enemy : logicManager.enemies) {
                if (enemy.isActive()) {
                    shapeRenderer.setColor(Color.BLACK);
                    shapeRenderer.rect(enemy.getCollisionShape().x, enemy.getCollisionShape().y, enemy.getCollisionShape().width, enemy.getCollisionShape().height);
                    shapeRenderer.setColor(Color.BLUE);
                    shapeRenderer.rect(enemy.collisionShapeTop.x, enemy.collisionShapeTop.y, enemy.collisionShapeTop.width, enemy.collisionShapeTop.height);
                }
            }
            // CameraLimits
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(cameraManager.getCameraLeftLimit(), camera.position.y - (cameraManager.getCAMERA_HORIZONTAL_SAFE_ZONE()*TILE_SIZE), cameraManager.getCAMERA_HORIZONTAL_SAFE_ZONE()*2*TILE_SIZE, cameraManager.getCAMERA_VERTICAL_SAFE_ZONE()*2*TILE_SIZE);
            shapeRenderer.end();
            // deathLayer visible
            if (logicManager.deathLayer != null && !logicManager.deathLayer.isVisible()) {
                logicManager.deathLayer.setVisible(true);
            }
        } else {
            if (logicManager.deathLayer != null && logicManager.deathLayer.isVisible()) {
                logicManager.deathLayer.setVisible(false);
            }
        }

        // Draw UI stage
        if (logicManager.imageLayer != null && logicManager.imageLayer.isVisible()) {
            if (Gdx.input.getInputProcessor() == null || Gdx.input.getInputProcessor() != uiStage) {
                Gdx.input.setInputProcessor(uiStage);
            }
            uiStage.act(Gdx.graphics.getDeltaTime());
            uiStage.draw();
        }
    }
}

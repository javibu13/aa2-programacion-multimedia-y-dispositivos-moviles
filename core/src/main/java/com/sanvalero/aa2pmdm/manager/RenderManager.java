package com.sanvalero.aa2pmdm.manager;

import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sanvalero.aa2pmdm.entity.Item;
import com.sanvalero.aa2pmdm.entity.Player;

public class RenderManager {
    
    private LogicManager logicManager;
    private Batch batch;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    public RenderManager(LogicManager logicManager, TiledMap levelMap) {
        this.logicManager = logicManager;

        this.mapRenderer = new OrthogonalTiledMapRenderer(levelMap);
        this.batch = mapRenderer.getBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, TILE_SIZE * 32, TILE_SIZE * 16);
        camera.update();
        
        this.font = new BitmapFont();

        shapeRenderer = new ShapeRenderer();
    }

    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f); // TODO: Change to a background color

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        for (Item item : logicManager.items) {
            if (item.isVisible()) {
                batch.draw(item.getCurrentFrame(), item.getPosition().x, item.getPosition().y);
            }
        }
        batch.draw(logicManager.player.getCurrentFrame(), logicManager.player.getPosition().x, logicManager.player.getPosition().y);
        // ...
        
        // Draw HUD elements
        float leftPadding = 4f;
        float topPadding = 4f;
        // // Lives
        for (int i = 0; i < logicManager.player.getMaxHealth(); i++) {
            if (i < logicManager.player.getHealth()) {
                batch.draw(R.getRegions("heart").get(1), leftPadding + (i * 20f), camera.viewportHeight - 20f - topPadding, 20f, 20f);
            } else {
                batch.draw(R.getRegions("heart").get(0), leftPadding + (i * 20f), camera.viewportHeight - 20f - topPadding, 20f, 20f);
            }
        }
        // // Score
        batch.draw(R.getTexture("coin"), leftPadding, camera.viewportHeight - 40f - topPadding, 20f, 20f); 
        String score = String.valueOf(logicManager.player.getScore());
        for (int i = 0; i < score.length(); i++) {
            char digit = score.charAt(i);
            // Transform the character to an integer
            int digitValue = digit - '0';
            batch.draw(R.getRegions("number").get(digitValue), leftPadding + 20f + (i * 20f), camera.viewportHeight - 40f - topPadding, 20f, 20f);
        }
        // // Key
        int keySpriteIndex = logicManager.player.isKey() ? 1 : 0;
        batch.draw(R.getRegions("key").get(keySpriteIndex), leftPadding + 3f, camera.viewportHeight - 60f - topPadding, 20f, 20f);
        // // Level
        batch.end();
        
        // Draw player's collision shapes for debugging 
        if (logicManager.isDebugMode()) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
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
            for (Item item : logicManager.items) {
                if (item.isActive()) {
                    shapeRenderer.setColor(Color.BLACK);
                    shapeRenderer.rect(item.getCollisionShape().x, item.getCollisionShape().y, item.getCollisionShape().width, item.getCollisionShape().height);
                }
            }
            shapeRenderer.end();
        }
    }
}

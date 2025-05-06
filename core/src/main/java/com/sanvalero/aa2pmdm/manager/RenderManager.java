package com.sanvalero.aa2pmdm.manager;

import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class RenderManager {
    
    private LogicManager logicManager;
    private Batch batch;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private BitmapFont font;

    public RenderManager(LogicManager logicManager, TiledMap levelMap) {
        this.logicManager = logicManager;

        this.mapRenderer = new OrthogonalTiledMapRenderer(levelMap);
        this.batch = mapRenderer.getBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, TILE_SIZE * 32, TILE_SIZE * 16);
        camera.update();
        
        this.font = new BitmapFont();
    }

    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f); // TODO: Change to a background color

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        batch.draw(logicManager.player.getCurrentFrame(), logicManager.player.getPosition().x, logicManager.player.getPosition().y);
        // ...

        // Draw UI elements
        // batch.draw(R.getTexture("coin"), 20, SCREEN_HEIGHT - 50);
        // font.draw(batch, String.valueOf(logicManager.player.getScore()), 40, SCREEN_HEIGHT - 40);
        batch.end();
    }
}

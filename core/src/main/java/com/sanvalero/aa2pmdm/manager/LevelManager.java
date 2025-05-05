package com.sanvalero.aa2pmdm.manager;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import lombok.Data;

@Data
public class LevelManager {

    private LogicManager logicManager;
    private int level;
    private TiledMap levelMap;
    private TiledMapTileLayer groundLayer;
    private MapLayer itemLayer;
    private MapLayer enemyLayer;

    public LevelManager(LogicManager logicManager, int level) {
        this.logicManager = logicManager;
        this.level = level;
        loadLevel(level);
    }

    private void loadLevel(int level) {
        levelMap = new TmxMapLoader().load(null); // TODO: Load the level map file
        // groundLayer
        // itemLayer
        // enemyLayer
        
        initializeLevel();
    }
    
    private void initializeLevel() {
        // logicManager.player = new Player();
        // logicManager.items = new Array<Item>();
        // ...
        loadItems();
        loadEnemies();
    }

    private void loadItems() {
    }

    private void loadEnemies() {
    }

}

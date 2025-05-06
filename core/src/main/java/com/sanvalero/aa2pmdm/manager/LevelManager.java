package com.sanvalero.aa2pmdm.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.entity.Player;

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
        System.out.println(getLevelFiles().get(0).path());
        levelMap = new TmxMapLoader().load("levels/level1.tmx"); // TODO: Load the level map file
        // levelMap = new TmxMapLoader().load(getLevelFiles().get(level).path()); // TODO: Load the level map file
        // groundLayer
        // itemLayer
        // enemyLayer
        
        initializeLevel();
    }

    private Array<FileHandle> getLevelFiles() {
        FileHandle levelsFolder = Gdx.files.internal("levels/");
        FileHandle[] allFiles = levelsFolder.list();
        Array<FileHandle> levelFiles = new Array<>();
        for (FileHandle file : allFiles) {
            if (file.extension().equals("tmx")) {
                levelFiles.add(file);
            }
        }
        return levelFiles;
    }
    
    private void initializeLevel() {
        // Initialize the player
        MapObject mapPlayer = levelMap.getLayers().get("player").getObjects().get(0);
        System.out.println(mapPlayer.getProperties());
        float mapPlayerX = mapPlayer.getProperties().get("x", Float.class);
        float mapPlayerY = mapPlayer.getProperties().get("y", Float.class);
        logicManager.player = new Player(new Vector2(mapPlayerX, mapPlayerY));
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

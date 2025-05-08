package com.sanvalero.aa2pmdm.manager;

import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.entity.Player;

import lombok.Data;

@Data
public class LevelManager {

    private LogicManager logicManager;
    private int level;
    private TiledMap levelMap;
    private static TiledMapTileLayer groundLayer;
    private static MapLayer itemLayer;
    private static MapLayer enemyLayer;

    public LevelManager(LogicManager logicManager, int level) {
        this.logicManager = logicManager;
        this.level = level;
        loadLevel(level);
    }

    private void loadLevel(int level) {
        System.out.println(getLevelFiles().get(0).path());
        levelMap = new TmxMapLoader().load(getLevelFiles().get(level).path());
        groundLayer = (TiledMapTileLayer) levelMap.getLayers().get("ground");
        itemLayer = levelMap.getLayers().get("items");
        // enemyLayer = levelMap.getLayers().get("enemies");

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
        logicManager.items = new Array<>();
        // ...
        loadItems();
        loadEnemies();
    }

    private void loadItems() {
        for (MapObject mapObject : itemLayer.getObjects()) {
            if (!((TiledMapTileMapObject) mapObject).getTile().getProperties().containsKey("type")) {
                System.out.println("No type property found in mapObject: " + mapObject.getName());
                continue;
            }
            String type = ((TiledMapTileMapObject) mapObject).getTile().getProperties().get("type", String.class);
            float x = mapObject.getProperties().get("x", Float.class);
            float y = mapObject.getProperties().get("y", Float.class);
            switch (type) {
                case "coin":
                    logicManager.items.add(new com.sanvalero.aa2pmdm.entity.Coin(new Vector2(x, y)));
                    break;
                default:
                    System.out.println("Unknown item type: " + type);
                    break;
            }
        }
    }

    private void loadEnemies() {
    }

    public static Array<Rectangle> getGroundTiles(Vector2 playerPosition) {
        Array<Rectangle> groundTileCollisionShapes = new Array<>();
        int playerMapTileX = (int) playerPosition.x / TILE_SIZE;
        int playerMapTileY = (int) playerPosition.y / TILE_SIZE;
        for (int y = playerMapTileY - 2; y <= playerMapTileY + 2; y++) {
            for (int x = playerMapTileX - 2; x <= playerMapTileX + 2; x++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                if (cell != null && cell.getTile().getProperties().containsKey("ground")) {
                    Rectangle tileCollisionShape = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    groundTileCollisionShapes.add(tileCollisionShape);
                }
            }
        }
        return groundTileCollisionShapes;
    }

}

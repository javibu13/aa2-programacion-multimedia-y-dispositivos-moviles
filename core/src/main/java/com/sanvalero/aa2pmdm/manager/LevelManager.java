package com.sanvalero.aa2pmdm.manager;

import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.entity.Ally;
import com.sanvalero.aa2pmdm.entity.Coin;
import com.sanvalero.aa2pmdm.entity.Exit;
import com.sanvalero.aa2pmdm.entity.Fly;
import com.sanvalero.aa2pmdm.entity.Key;
import com.sanvalero.aa2pmdm.entity.Player;
import com.sanvalero.aa2pmdm.entity.Spaceship;

import lombok.Data;

@Data
public class LevelManager {

    private LogicManager logicManager;
    private int level;
    private TiledMap levelMap;
    private static TiledMapTileLayer groundLayer;
    private static TiledMapTileLayer deathLayer;
    private static MapLayer itemLayer;
    private static MapLayer enemyLayer;

    public LevelManager(LogicManager logicManager, int level) {
        this.logicManager = logicManager;
        this.level = level;
        loadLevel(level);
    }

    private void loadLevel(int level) {
        Array<FileHandle> levelList = getLevelFiles();
        System.out.println("Level list size: " + levelList.size);
        System.out.println("Level number: " + level);
        if (level < 0 || level > levelList.size) {
            // Invalid level number
            System.out.println("Invalid level number: " + level);
            throw new IllegalArgumentException("Level number must be between 0 and " + (levelList.size - 1));
        } else if (level == levelList.size) {
            // Game Over level
            System.out.println("No more levels available. Game Over!");
            this.level = -1; // Set level to -1 for Game Over
            levelMap = new TmxMapLoader().load("levels" + File.separator + "gameOver" + File.separator + "gameOver.tmx");
            // Get imageLayer with game over image
            logicManager.imageLayer = levelMap.getLayers().get("gameOver");
        } else {
            // Load the level
            System.out.println(levelList.get(level).path());
            levelMap = new TmxMapLoader().load(levelList.get(level).path());
        }
        groundLayer = (TiledMapTileLayer) levelMap.getLayers().get("ground");
        logicManager.groundLayer = groundLayer;
        deathLayer = getDeathLayer();
        logicManager.deathLayer = deathLayer;
        deathLayer.setVisible(false); // Hide death layer
        itemLayer = levelMap.getLayers().get("items");
        enemyLayer = getEnemyLayer();

        initializeLevel();
    }

    private Array<FileHandle> getLevelFiles() {
        FileHandle levelsFolder = Gdx.files.internal("levels" + File.separator);
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
        MapObject mapExit = levelMap.getLayers().get("exit").getObjects().get(0);
        System.out.println(mapPlayer.getProperties());
        float mapPlayerX = mapPlayer.getProperties().get("x", Float.class);
        float mapPlayerY = mapPlayer.getProperties().get("y", Float.class);
        float mapExitX = mapExit.getProperties().get("x", Float.class);
        float mapExitY = mapExit.getProperties().get("y", Float.class);
        logicManager.player = new Player(new Vector2(mapPlayerX, mapPlayerY));
        logicManager.exit = new Exit(new Vector2(mapExitX, mapExitY));
        logicManager.items = new Array<>();
        logicManager.enemies = new Array<>();
        loadItems();
        loadEnemies();
    }

    private TiledMapTileLayer getDeathLayer() {
        deathLayer = (TiledMapTileLayer) levelMap.getLayers().get("death");
        if (deathLayer == null) {
            // Get properties of the levelMap
            MapProperties props = levelMap.getProperties();
            int mapWidth = props.get("width", Integer.class);
            int mapHeight = props.get("height", Integer.class);
            int tileWidth = props.get("tilewidth", Integer.class);
            int tileHeight = props.get("tileheight", Integer.class);
            // Create a new and empty death layer
            deathLayer = new TiledMapTileLayer(mapWidth, mapHeight, tileWidth, tileHeight);
            deathLayer.setName("death");
            levelMap.getLayers().add(deathLayer);
        }
        return deathLayer;
    }

    private MapLayer getEnemyLayer() {
        enemyLayer = levelMap.getLayers().get("enemies");
        if (enemyLayer == null) {
            // Get properties of the levelMap
            MapProperties props = levelMap.getProperties();
            int mapWidth = props.get("width", Integer.class);
            int mapHeight = props.get("height", Integer.class);
            int tileWidth = props.get("tilewidth", Integer.class);
            int tileHeight = props.get("tileheight", Integer.class);
            // Create a new and empty enemy layer
            enemyLayer = new TiledMapTileLayer(mapWidth, mapHeight, tileWidth, tileHeight);
            enemyLayer.setName("enemies");
            levelMap.getLayers().add(enemyLayer);
        }
        return enemyLayer;
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
                    logicManager.items.add(new Coin(new Vector2(x, y)));
                    break;
                case "key":
                    logicManager.items.add(new Key(new Vector2(x, y)));
                    break;
                case "ally":
                    logicManager.items.add(new Ally(new Vector2(x, y)));
                    break;
                case "spaceship":
                    logicManager.items.add(new Spaceship(new Vector2(x, y)));
                    break;
                default:
                    System.out.println("Unknown item type: " + type);
                    break;
            }
        }
    }

    private void loadEnemies() {
        for (MapObject mapObject : enemyLayer.getObjects()) {
            if (!((TiledMapTileMapObject) mapObject).getTile().getProperties().containsKey("enemy")) {
                System.out.println("No 'enemy' type property found in mapObject: " + mapObject.getName());
                continue;
            }
            String type = ((TiledMapTileMapObject) mapObject).getTile().getProperties().get("enemy", String.class);
            float x = mapObject.getProperties().get("x", Float.class);
            float y = mapObject.getProperties().get("y", Float.class);
            switch (type) {
                case "fly":
                    logicManager.enemies.add(new Fly(new Vector2(x, y)));
                    System.out.println("Fly enemy added at: " + x + ", " + y);
                    break;
                default:
                    System.out.println("Unknown enemy type: " + type);
                    break;
            }
        }
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

    // TODO: Refactor getGroundTiles and getDeathTiles to avoid code duplication
    public static Array<Rectangle> getDeathTiles(Vector2 playerPosition) {
        Array<Rectangle> deathTileCollisionShapes = new Array<>();
        int playerMapTileX = (int) playerPosition.x / TILE_SIZE;
        int playerMapTileY = (int) playerPosition.y / TILE_SIZE;
        for (int y = playerMapTileY - 2; y <= playerMapTileY + 2; y++) {
            for (int x = playerMapTileX - 2; x <= playerMapTileX + 2; x++) {
                TiledMapTileLayer.Cell cell = deathLayer.getCell(x, y);
                if (cell != null && cell.getTile().getProperties().containsKey("death")) {
                    Rectangle tileCollisionShape = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    deathTileCollisionShapes.add(tileCollisionShape);
                }
            }
        }
        return deathTileCollisionShapes;
    }

}

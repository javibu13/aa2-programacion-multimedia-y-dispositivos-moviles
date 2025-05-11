package com.sanvalero.aa2pmdm.manager;

import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.sanvalero.aa2pmdm.entity.Player;

import lombok.Data;

@Data
public class CameraManager {
    private OrthographicCamera camera;
    private final int CAMERA_N_TILES_WIDTH = 32;
    private final int CAMERA_N_TILES_HEIGHT = 16;
    private final int CAMERA_HORIZONTAL_SAFE_ZONE = 4;
    private final int CAMERA_VERTICAL_SAFE_ZONE = 3;
    private int mapNTilesWidth;
    private int mapNTilesHeight;
    private int tileWidth;
    private int tileHeight;
    private float cameraRightLimit;
    private float cameraLeftLimit;
    private float cameraTopLimit;
    private float cameraBottomLimit;

    public CameraManager(TiledMap map) {
        MapProperties props = map.getProperties();
        this.mapNTilesWidth = props.get("width", Integer.class);
        this.mapNTilesHeight = props.get("height", Integer.class);
        this.tileWidth = props.get("tilewidth", Integer.class);
        this.tileHeight = props.get("tileheight", Integer.class);
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, TILE_SIZE * 32, TILE_SIZE * 16);
        this.camera.position.set(TILE_SIZE * 16, TILE_SIZE * 8, 0);
        this.camera.update();
    }

    public void updateCameraPosition(Player player) {
        cameraRightLimit = camera.position.x + (CAMERA_HORIZONTAL_SAFE_ZONE * TILE_SIZE);
        cameraLeftLimit = camera.position.x - (CAMERA_HORIZONTAL_SAFE_ZONE * TILE_SIZE);
        cameraTopLimit = camera.position.y + (CAMERA_VERTICAL_SAFE_ZONE * TILE_SIZE);
        cameraBottomLimit = camera.position.y - (CAMERA_VERTICAL_SAFE_ZONE * TILE_SIZE);
        if (player.getPosition().x > cameraRightLimit) {
            if ((camera.position.x + (CAMERA_N_TILES_WIDTH/2f * TILE_SIZE) + (player.getPosition().x - cameraRightLimit)) > mapNTilesWidth * tileWidth) {
                // Set the camera to the right limit of the map
                camera.position.x = mapNTilesWidth * tileWidth - (CAMERA_N_TILES_WIDTH/2f * TILE_SIZE);
            } else {
                // Move the camera to the right
                camera.position.x += player.getPosition().x - cameraRightLimit;
            }
        } else if (player.getPosition().x < cameraLeftLimit) {
            if ((camera.position.x - (CAMERA_N_TILES_WIDTH/2f * TILE_SIZE) - (cameraLeftLimit - player.getPosition().x)) < 0) {
                // Set the camera to the left limit of the map
                camera.position.x = (CAMERA_N_TILES_WIDTH/2f * TILE_SIZE);
            } else {
                camera.position.x -= cameraLeftLimit - player.getPosition().x;
            }
        }
        if (player.getPosition().y > cameraTopLimit) {
            if ((camera.position.y + (CAMERA_N_TILES_HEIGHT/2f * TILE_SIZE) + (player.getPosition().y - cameraTopLimit)) > mapNTilesHeight * tileHeight) {
                // Set the camera to the top limit of the map
                camera.position.y = mapNTilesHeight * tileHeight - (CAMERA_N_TILES_HEIGHT/2f * TILE_SIZE);
            } else {
                // Move the camera to the top
                camera.position.y += player.getPosition().y - cameraTopLimit;
            }
        } else if (player.getPosition().y < cameraBottomLimit) {
            if ((camera.position.y - (CAMERA_N_TILES_HEIGHT/2f * TILE_SIZE) - (cameraBottomLimit - player.getPosition().y)) < 0) {
                // Set the camera to the bottom limit of the map
                camera.position.y = (CAMERA_N_TILES_HEIGHT/2f * TILE_SIZE);
            } else {
                // Move the camera to the bottom
                camera.position.y -= cameraBottomLimit - player.getPosition().y;
            }
        }
    }

    public void update() {
        camera.update();
    }
}

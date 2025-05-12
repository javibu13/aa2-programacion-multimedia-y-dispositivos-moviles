package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy extends Character {
    
    public Enemy(TextureRegion currentFrame) {
        super(currentFrame);
        initEnemy();
    }

    public Enemy(TextureRegion currentFrame, Vector2 position) {
        super(currentFrame, position);
        initEnemy();
    }

    public Enemy(TextureRegion currentFrame, Vector2 position, Vector2 velocity) {
        super(currentFrame, position, velocity);
        initEnemy();
    }

    private void initEnemy() {
        this.isActive = true;
        this.isVisible = true;
    }

    public abstract void update(float deltaTime, Player player);

    public abstract void collideWithPlayer(Player player);

    public float getDistanceToPlayer(Player player) {
        float distanceX = Math.abs(player.getPosition().x - this.position.x);
        float distanceY = Math.abs(player.getPosition().y - this.position.y);
        float distance = (float) Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        return distance;
    }

    public static float distanceToVolumeMultiplier(float distance) {
        int tileDistance = (int) (distance / TILE_SIZE);
        // If tileDistance is 32 or more, set volume multiplayer to 0.1f
        // If tileDistance is less than 4, set volume multiplayer to 1.0f
        float volumeMultiplier = 1.0f - (tileDistance / 32f);
        if (tileDistance < 4) {
            volumeMultiplier = 1.0f;
        } else if (tileDistance >= 32) {
            volumeMultiplier = 0.1f;
        }
        return volumeMultiplier;
    }
}

package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FISH_JUMP_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FISH_WATER_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.GRAVITY;
import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FISH_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FISH_BITE_INTERVAL;
import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FISH_BITE_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.manager.R;

public class Fish extends Enemy {
    private int distance; // Distance to move
    private Animation<TextureRegion> attackAnimation;
    private TextureRegion idleFrame;
    private Vector2 startVelocity;
    private float stateTime = 0f;
    private float deathTimeLimit = 0f;
    private float deathTime = 0f;
    private float biteTimer = 0f;

    public Fish(Vector2 position) {
        super(R.getRegions("enemy_fish_attack").get(0), position);
        this.setAnimations();
        this.initFish(new Vector2(ENEMY_FISH_JUMP_SPEED, 0f));
    }

    public Fish(Vector2 position, Vector2 velocity) {
        super(R.getRegions("enemy_fish_attack").get(0), position, velocity);
        this.setAnimations();
        this.initFish(velocity);
    }

    private void initFish(Vector2 velocity) {
        this.startVelocity = velocity.cpy();
        this.velocity = velocity;
        collisionShape = new Rectangle(position.x + (currentFrame.getRegionWidth()/16*5), position.y + (currentFrame.getRegionHeight()/16*3), currentFrame.getRegionWidth()/2f, currentFrame.getRegionHeight()/1.6f);
    }

    private void setAnimations() {
        // Set animations using the attack animation and flipping it for the falling animation
        Array<AtlasRegion> attackRegions = R.getRegions("enemy_fish_attack");
        AtlasRegion flippedRegion = new AtlasRegion(attackRegions.get(0));
        flippedRegion.flip(false, true);

        attackAnimation = new Animation<>(ENEMY_FISH_ANIMATION_SPEED, attackRegions);
        idleFrame = flippedRegion;
    }

    public void update(float deltaTime, Player player) {
        if (!isActive) {
            if (isVisible && deathTime < deathTimeLimit) {
                deathTime += deltaTime;
            } else if (isVisible) {
                velocity = startVelocity.cpy(); // Reset velocity
                isActive = true; // Reset active state
                deathTime = 0f; // Reset death time
            }
            return; // Skip update if not active
        }
        stateTime += deltaTime;
        biteTimer += deltaTime;

        velocity.y -= GRAVITY * deltaTime; // Apply gravity

        if (velocity.y > 0) {
            position.y += velocity.y * deltaTime;
            currentFrame = attackAnimation.getKeyFrame(stateTime, true);
            if (biteTimer >= ENEMY_FISH_BITE_INTERVAL) {
                // Calculate distance to player to adjust sound volume
                float distanceToPlayer = getDistanceToPlayer(player);
                float volumeMultiplier = distanceToVolumeMultiplier(distanceToPlayer);
                // Play bite sound with adjusted volume and random pitch between 1f and 1.3f
                R.getSound(ENEMY_FISH_BITE_SOUND).play(Main.getSoundVolume() * 0.075f * volumeMultiplier, 1.0f + (MathUtils.random(0.0f, 0.3f)), 0.0f);
                biteTimer = 0f; // Reset bite timer
            }
        } else if (velocity.y < 0) {
            position.y += velocity.y * deltaTime;
            currentFrame = idleFrame; // Set idle frame when falling
        } else {
            currentFrame = idleFrame; // Set idle frame when not moving
        }

        if (position.y <= startPosition.y) {
            // Play water splash sound
            float distanceToPlayer = getDistanceToPlayer(player);
            float volumeMultiplier = distanceToVolumeMultiplier(distanceToPlayer);
            R.getSound(ENEMY_FISH_WATER_SOUND).play(Main.getSoundVolume() * 0.2f * volumeMultiplier);
            position = startPosition.cpy(); // Reset position
            velocity = Vector2.Zero; // Reset velocity
            isActive = false; // Reset active state
            // Generate a new random death time between 1.5 and 3 seconds
            deathTimeLimit = (float) (Math.random() * 1.5f + 1.5f);
        }

        // Update collision shape position
        collisionShape.setPosition(position.x + (currentFrame.getRegionWidth()/16*5), position.y + (currentFrame.getRegionHeight()/16*3));
    }

    public void collideWithPlayer(Player player) {
        if (!isActive || !player.isActive()) {
            return; // Skip collision if not active
        }
        if (player.getItemCollisionShape().overlaps(collisionShape)) { // Hurt player
            player.hurt(1); // Hurt player
            player.resetPosition();
        }
    }
    
}

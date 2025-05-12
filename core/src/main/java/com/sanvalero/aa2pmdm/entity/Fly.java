package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FLY_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FLY_MOVE_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FLY_WING_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FLY_WING_INTERVAL;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_JUMP_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.manager.R;

public class Fly extends Enemy {
    private int distance; // Distance to move
    private boolean moveRight = true; // Direction of movement
    private Animation<TextureRegion> animationRight, animationLeft;
    private float stateTime = 0f;
    private float wingTimer = 0f;

    public Fly(Vector2 position, int tileDistance) {
        super(R.getRegions("enemy_fly").get(0), position);
        this.setAnimations();
        this.initFly(tileDistance, new Vector2(ENEMY_FLY_MOVE_SPEED, 0f));
    }

    public Fly(Vector2 position, Vector2 velocity, int tileDistance) {
        super(R.getRegions("enemy_fly").get(0), position, velocity);
        this.setAnimations();
        this.initFly(tileDistance, velocity);
    }

    private void initFly(int tileDistance, Vector2 velocity) {
        this.distance = tileDistance * TILE_SIZE; // Convert tile distance to pixels
        this.velocity = new Vector2(velocity.x, 0f);
        collisionShape.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight()/1.55f);
    }

    private void setAnimations() {
        // Set animations using the move right animation and flipping it for the left animation
        Array<AtlasRegion> flyRegionsRight = R.getRegions("enemy_fly");
        Array<AtlasRegion> flyRegionsLeft = new Array<>();
        for (AtlasRegion region : flyRegionsRight) {
            AtlasRegion flippedRegion = new AtlasRegion(region);
            flippedRegion.flip(true, false);
            flyRegionsLeft.add(flippedRegion);
        }

        Array<TextureRegion> animationSpritesRight = new Array<>();
        Array<TextureRegion> animationSpritesLeft = new Array<>();
        animationSpritesRight.add(flyRegionsRight.get(0));
        animationSpritesRight.add(flyRegionsRight.get(1));
        animationSpritesRight.add(flyRegionsRight.get(2));
        animationSpritesRight.add(flyRegionsRight.get(1));
        animationSpritesLeft.add(flyRegionsLeft.get(0));
        animationSpritesLeft.add(flyRegionsLeft.get(1));
        animationSpritesLeft.add(flyRegionsLeft.get(2));
        animationSpritesLeft.add(flyRegionsLeft.get(1));
        animationRight = new Animation<>(ENEMY_FLY_ANIMATION_SPEED, animationSpritesRight);
        animationLeft = new Animation<>(ENEMY_FLY_ANIMATION_SPEED, animationSpritesLeft);
    }

    public void update(float deltaTime, Player player) {
        if (!isActive) {
            return; // Skip update if not active
        }
        stateTime += deltaTime;
        wingTimer += deltaTime;
        
        if (moveRight) {
            currentFrame = animationRight.getKeyFrame(stateTime, true);
            position.x += velocity.x * deltaTime;
            if (position.x >= startPosition.x + distance) {
                moveRight = false; // Change direction
            }
        } else {
            currentFrame = animationLeft.getKeyFrame(stateTime, true);
            position.x -= velocity.x * deltaTime;
            if (position.x <= startPosition.x) {
                moveRight = true; // Change direction
            }
        }
        // Play wing sound
        if (wingTimer >= ENEMY_FLY_WING_INTERVAL) {
            // Calculate distance to player to adjust sound volume
            float distanceToPlayer = getDistanceToPlayer(player);
            int tileDistance = (int) (distanceToPlayer / TILE_SIZE);
            // If tileDistance is 32 or more, set volume multiplayer to 0.1f
            // If tileDistance is less than 4, set volume multiplayer to 1.0f
            float volumeMultiplier = 1.0f - (tileDistance / 32f);
            if (tileDistance < 4) {
                volumeMultiplier = 1.0f;
            } else if (tileDistance >= 32) {
                volumeMultiplier = 0.1f;
            }            
            R.getSound(ENEMY_FLY_WING_SOUND).play(Main.getSoundVolume() * 0.5f * volumeMultiplier, 1.0f, 0.0f);
            wingTimer = 0f;
        }
        
        // Update collision shape position
        updateCollisionShapes();
    }

    public void collideWithPlayer(Player player) {
        System.out.println("Fly collided with player!");
        if (isActive && player.isActive() && player.collisionShapeBottom.overlaps(collisionShape)) { // Stop player movement
            System.out.println("Player collided with Fly! so... Jump!");
            player.playJumpSound();
            player.setGrounded(false);
            player.setJumping(true);
            player.setVelocityY(PLAYER_JUMP_SPEED);
        }
    }
    
}

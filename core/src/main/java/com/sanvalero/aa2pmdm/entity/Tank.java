package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_TANK_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_TANK_MOVE_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_TANK_DEATH_TIME;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_JUMP_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.manager.R;

public class Tank extends Enemy {
    private int distance; // Distance to move
    private boolean moveRight = true; // Direction of movement
    private Animation<TextureRegion> moveAnimationRight, moveAnimationLeft;
    private float stateTime = 0f;
    private float deathTime = 0f;

    public Tank(Vector2 position, int tileDistance) {
        super(R.getRegions("enemy_tank_move").get(0), position);
        this.setAnimations();
        this.initTank(tileDistance, new Vector2(ENEMY_TANK_MOVE_SPEED, 0f));
    }

    public Tank(Vector2 position, Vector2 velocity, int tileDistance) {
        super(R.getRegions("enemy_tank_move").get(0), position, velocity);
        this.setAnimations();
        this.initTank(tileDistance, velocity);
    }

    private void initTank(int tileDistance, Vector2 velocity) {
        this.distance = tileDistance * TILE_SIZE; // Convert tile distance to pixels
        this.velocity = new Vector2(velocity.x, 0f);
        collisionShape.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight() - (currentFrame.getRegionHeight()/4f));
        this.collisionShapeTop = new Rectangle(position.x + (currentFrame.getRegionWidth()/16f), position.y + (currentFrame.getRegionHeight()/1.1f) - (currentFrame.getRegionHeight()/8f), currentFrame.getRegionWidth() - (currentFrame.getRegionWidth()/16f)*2, currentFrame.getRegionHeight()/4f);
    }

    private void setAnimations() {
        // Set move animations using the move right animation and flipping it for the left animation
        Array<AtlasRegion> moveRegionsRight = R.getRegions("enemy_tank_move");
        Array<AtlasRegion> moveRegionsLeft = new Array<>();
        for (AtlasRegion region : moveRegionsRight) {
            AtlasRegion flippedRegion = new AtlasRegion(region);
            flippedRegion.flip(true, false);
            moveRegionsLeft.add(flippedRegion);
        }
        moveAnimationRight = new Animation<>(ENEMY_TANK_ANIMATION_SPEED, moveRegionsRight);
        moveAnimationLeft = new Animation<>(ENEMY_TANK_ANIMATION_SPEED, moveRegionsLeft);
    }

    public void update(float deltaTime, Player player) {
        if (!isActive) {
            if (isVisible && deathTime < ENEMY_TANK_DEATH_TIME) {
                deathTime += deltaTime;
                currentFrame = R.getRegions("enemy_tank_death").get(0); // Set death sprite
            } else if (isVisible) {
                isActive = true; // Reset active state
                deathTime = 0f; // Reset death time
            }
            return; // Skip update if not active
        }
        stateTime += deltaTime;

        if (moveRight) {
            position.x += velocity.x * deltaTime;
            currentFrame = moveAnimationRight.getKeyFrame(stateTime, true);
            if (position.x >= startPosition.x + distance) {
                moveRight = false; // Change direction
            }
        } else {
            position.x -= velocity.x * deltaTime;
            currentFrame = moveAnimationLeft.getKeyFrame(stateTime, true);
            if (position.x <= startPosition.x) {
                moveRight = true; // Change direction
            }
        }
        // Update collision shape position
        collisionShape.setPosition(position.x, position.y);
        collisionShapeTop.setPosition(position.x + (currentFrame.getRegionWidth()/16f), position.y + (currentFrame.getRegionHeight()/1.1f) - (currentFrame.getRegionHeight()/8f));
    }

    public void collideWithPlayer(Player player) {
        if (!isActive || !player.isActive()) {
            return; // Skip collision if not active
        }
        if (player.collisionShapeBottom.overlaps(collisionShapeTop)) { // Stop player movement
            player.playJumpSound();
            player.setGrounded(false);
            player.setJumping(true);
            player.setVelocityY(PLAYER_JUMP_SPEED);
            isActive = false; // Deactivate tank
            currentFrame = R.getRegions("enemy_tank_death").get(0); // Set death sprite
        } else if (player.getItemCollisionShape().overlaps(collisionShape)) { // Hurt player
            player.hurt(1); // Hurt player
            player.resetPosition();
        }
    }
    
}

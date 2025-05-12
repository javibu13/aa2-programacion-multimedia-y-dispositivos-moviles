package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FLY_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FLY_MOVE_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_JUMP_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.TILE_SIZE;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.manager.R;

public class Fly extends Enemy {
    private int distance; // Distance to move
    private boolean moveRight = true; // Direction of movement
    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

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
        Array<AtlasRegion> sprites = R.getRegions("enemy_fly");
        Array<TextureRegion> animationSprites = new Array<>();
        animationSprites.add(sprites.get(0));
        animationSprites.add(sprites.get(1));
        animationSprites.add(sprites.get(2));
        animationSprites.add(sprites.get(1));
        animation = new Animation<>(ENEMY_FLY_ANIMATION_SPEED, animationSprites);
    }

    public void update(float deltaTime) {
        if (!isActive) {
            return; // Skip update if not active
        }
        stateTime += deltaTime;
        currentFrame = animation.getKeyFrame(stateTime, true);

        if (moveRight) {
            position.x += velocity.x * deltaTime;
            if (position.x >= startPosition.x + distance) {
                moveRight = false; // Change direction
            }
        } else {
            position.x -= velocity.x * deltaTime;
            if (position.x <= startPosition.x) {
                moveRight = true; // Change direction
            }
        }
        // Update collision shape position
        collisionShape.setPosition(position.x, position.y);
    }

    public void collideWithPlayer(Player player) {
        System.out.println("Fly collided with player!");
        if (isActive && player.isActive() && player.collisionShapeBottom.overlaps(collisionShape)) { // Stop player movement
            System.out.println("Player collided with Fly! so... Jump!");
            player.setGrounded(false);
            player.setJumping(true);
            player.setVelocityY(PLAYER_JUMP_SPEED);
        }
    }
    
}

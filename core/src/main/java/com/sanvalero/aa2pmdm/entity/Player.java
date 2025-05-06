package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.GRAVITY;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_IDLE_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_JUMP_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_MOVE_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_MOVE_SPEED;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.manager.LevelManager;
import com.sanvalero.aa2pmdm.manager.R;

import lombok.Data;

@Data
public class Player extends Character {
    
    public static enum State {
        IDLE_RIGHT, IDLE_LEFT, MOVE_RIGHT, MOVE_LEFT, IS_JUMPING_RIGHT, IS_JUMPING_LEFT
    }

    private int score;
    private int lives;
    private int health;
    private int maxHealth;
    private float moveSpeed;
    private float jumpSpeed;
    private boolean isJumping;
    private boolean isGrounded;
    private State state;
    private float stateTime;
    private Animation<TextureRegion> idleRightAnim, idleLeftAnim, rightAnim, leftAnim, jumpRightAnim, jumpLeftAnim;

    public Player(Vector2 startPosition) {
        super(R.getRegions("player_jump").get(0), startPosition);
        // Set animations
        this.setAnimations();
        // Set initial state
        score = 0;
        maxHealth = 3;
        health = maxHealth;
        lives = 3;
        moveSpeed = PLAYER_MOVE_SPEED;
        jumpSpeed = PLAYER_JUMP_SPEED;
        isJumping = true;
        isGrounded = false;
        state = State.IS_JUMPING_RIGHT;
        stateTime = 0f;
    }

    public void setAnimations() {
        // Set idle animations using the idle right animation and flipping it for the left animation
        Array<AtlasRegion> idleRegionsRight = R.getRegions("player_idle");
        Array<AtlasRegion> idleRegionsLeft = new Array<>();
        for (AtlasRegion region : idleRegionsRight) {
            AtlasRegion flippedRegion = new AtlasRegion(region);
            flippedRegion.flip(true, false);
            idleRegionsLeft.add(flippedRegion);
        }
        idleRightAnim = new Animation<>(PLAYER_IDLE_ANIMATION_SPEED, idleRegionsRight);
        idleLeftAnim = new Animation<>(PLAYER_IDLE_ANIMATION_SPEED, idleRegionsLeft);
        // Set move animations using the move right animation and flipping it for the left animation
        Array<AtlasRegion> moveRegionsRight = R.getRegions("player_move");
        Array<AtlasRegion> moveRegionsLeft = new Array<>();
        for (AtlasRegion region : moveRegionsRight) {
            AtlasRegion flippedRegion = new AtlasRegion(region);
            flippedRegion.flip(true, false);
            moveRegionsLeft.add(flippedRegion);
        }
        rightAnim = new Animation<>(PLAYER_MOVE_ANIMATION_SPEED, moveRegionsRight);
        leftAnim = new Animation<>(PLAYER_MOVE_ANIMATION_SPEED, moveRegionsLeft);
        // Set jump animations using the jump right animation and flipping it for the left animation
        Array<AtlasRegion> jumpRegionsRight = R.getRegions("player_jump");
        Array<AtlasRegion> jumpRegionsLeft = new Array<>();
        for (AtlasRegion region : jumpRegionsRight) {
            AtlasRegion flippedRegion = new AtlasRegion(region);
            flippedRegion.flip(true, false);
            jumpRegionsLeft.add(flippedRegion);
        }
        jumpRightAnim = new Animation<>(PLAYER_MOVE_ANIMATION_SPEED, jumpRegionsRight);
        jumpLeftAnim = new Animation<>(PLAYER_MOVE_ANIMATION_SPEED, jumpRegionsLeft);
    }
    
    public void update(float delta) {
        stateTime += delta;
        switch (state) {
            case IDLE_RIGHT:
                currentFrame = idleRightAnim.getKeyFrame(stateTime, true);
                break;
            case IDLE_LEFT:
                currentFrame = idleLeftAnim.getKeyFrame(stateTime, true);
                break;
            case MOVE_RIGHT:
                currentFrame = rightAnim.getKeyFrame(stateTime, true);
                break;
            case MOVE_LEFT:
                currentFrame = leftAnim.getKeyFrame(stateTime, true);
                break;
            case IS_JUMPING_RIGHT:
                currentFrame = jumpRightAnim.getKeyFrame(stateTime, true);
                break;
            case IS_JUMPING_LEFT:
                currentFrame = jumpLeftAnim.getKeyFrame(stateTime, true);
                break;
        }
        velocity.y -= GRAVITY;
        if (velocity.y < -PLAYER_JUMP_SPEED) {
            velocity.y = -PLAYER_JUMP_SPEED;
        }
        this.move(velocity.x, velocity.y * delta);
        this.checkGroundCollisions();
    }

    public void jump() {
        if (isGrounded) {
            isJumping = true;
            isGrounded = false;
            velocity.y = PLAYER_JUMP_SPEED;
        }
    }

    public void checkGroundCollisions() {
        // Check if the player is colliding with ground and wall tiles to prevent going through them
        Array<Rectangle> groundTileCollisionShapesArround = LevelManager.getGroundTiles(position);
        for (Rectangle tile : groundTileCollisionShapesArround) {
            if (collisionShape.overlaps(tile)) {
                // Check if the player is falling and colliding with the ground
                if (velocity.y < 0) {
                    position.y = tile.getY() + tile.getHeight();
                    collisionShape.setPosition(position.x, position.y);
                    velocity.y = 0;
                    isGrounded = true;
                    isJumping = false;
                } else if (velocity.x > 0 && collisionShape.getX() + collisionShape.getWidth() > tile.getX()) {
                    position.x = tile.getX() - collisionShape.getWidth();
                    collisionShape.setPosition(position.x, position.y);
                    velocity.x = 0;
                } else if (velocity.x < 0 && collisionShape.getX() < tile.getX() + tile.getWidth()) {
                    position.x = tile.getX() + tile.getWidth();
                    collisionShape.setPosition(position.x, position.y);
                    velocity.x = 0;
                }
            }
        }
    }
}

package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.GRAVITY;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_FOOTSTEPS_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_IDLE_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_JUMP_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_JUMP_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_LANDING_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_MOVE_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_MOVE_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_FOOTSTEP_INTERVAL;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.Main;
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
    private boolean isKey;
    private float moveSpeed;
    private float jumpSpeed;
    private boolean isJumping;
    private boolean isGrounded;
    private State state;
    private float stateTime;
    private float footstepTimer;
    private Animation<TextureRegion> idleRightAnim, idleLeftAnim, rightAnim, leftAnim, jumpRightAnim, jumpLeftAnim;
    private Rectangle itemCollisionShape;

    public Player(Vector2 startPosition) {
        super(R.getRegions("player_jump").get(0), startPosition);
        // Set animations
        this.setAnimations();
        // Set initial state
        score = 0;
        maxHealth = 3;
        health = maxHealth;
        lives = 3;
        isKey = false;
        moveSpeed = PLAYER_MOVE_SPEED;
        jumpSpeed = PLAYER_JUMP_SPEED;
        isJumping = true;
        isGrounded = false;
        state = State.IS_JUMPING_RIGHT;
        stateTime = 0f;
        footstepTimer = 0f;
        // Set item collision shapes
        itemCollisionShape = new Rectangle(position.x + (collisionShape.getWidth()/16*3), position.y + (collisionShape.getHeight()/16*3), collisionShape.getWidth()/16*10, collisionShape.getHeight()/16*10);
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
        boolean isWalking = false;
        switch (state) {
            case IDLE_RIGHT:
                currentFrame = idleRightAnim.getKeyFrame(stateTime, true);
                break;
            case IDLE_LEFT:
                currentFrame = idleLeftAnim.getKeyFrame(stateTime, true);
                break;
            case MOVE_RIGHT:
                currentFrame = rightAnim.getKeyFrame(stateTime, true);
                isWalking = true;
                break;
            case MOVE_LEFT:
                currentFrame = leftAnim.getKeyFrame(stateTime, true);
                isWalking = true;
                break;
            case IS_JUMPING_RIGHT:
                currentFrame = jumpRightAnim.getKeyFrame(stateTime, true);
                break;
            case IS_JUMPING_LEFT:
                currentFrame = jumpLeftAnim.getKeyFrame(stateTime, true);
                break;
        }
        if (isWalking) {
            // Play footstep sound
            if (isGrounded && !isJumping) {
                footstepTimer += delta;
                if (footstepTimer >= PLAYER_FOOTSTEP_INTERVAL) {
                    R.getSound(PLAYER_FOOTSTEPS_SOUND).play(Main.getSoundVolume() * 0.5f, 1.0f, 0.0f);
                    footstepTimer = 0f;
                }
            }
        } else {
            // Stop footstep sound
            R.getSound(PLAYER_FOOTSTEPS_SOUND).stop();
        }
        velocity.y -= GRAVITY;
        if (velocity.y < -PLAYER_JUMP_SPEED) {
            velocity.y = -PLAYER_JUMP_SPEED;
        }
        this.move(velocity.x, velocity.y * delta);
        this.checkGroundCollisions();
    }

    @Override
    public void move(float x, float y) {
        super.move(x, y);
        itemCollisionShape.setPosition(position.x + (collisionShape.getWidth()/16*3), position.y + (collisionShape.getHeight()/16*3));
    }

    public void jump() {
        if (isGrounded) {
            isJumping = true;
            isGrounded = false;
            velocity.y = PLAYER_JUMP_SPEED;
            R.getSound(PLAYER_JUMP_SOUND).play(Main.getSoundVolume() * 0.3f, 1.5f, 0.0f);
        }
    }

    public void checkGroundCollisions() {
        // Check if the player is colliding with ground and wall tiles to prevent going through them
        Array<Rectangle> groundTileCollisionShapesArround = LevelManager.getGroundTiles(position);
        // Variables to control if player has collided with a tile horizontally or vertically to end the loop
        boolean collidedX = false;
        boolean collidedY = false;
        boolean bottomCollision = false;
        for (Rectangle tile : groundTileCollisionShapesArround) {
            if (collisionShape.overlaps(tile)) {
                // Check if right collision overlaps with the tile
                if (collisionShapeRight.overlaps(tile)) {
                    position.x = tile.getX() - collisionShape.getWidth();
                    this.updateCollisionShapes();
                    velocity.x = 0;
                } // Check if left collision overlaps with the tile
                else if (collisionShapeLeft.overlaps(tile)) {
                    position.x = tile.getX() + tile.getWidth();
                    this.updateCollisionShapes();
                    velocity.x = 0;
                } // Check if top collision overlaps with the tile
                else if (collisionShapeTop.overlaps(tile)) {
                    position.y = tile.getY() - collisionShape.getHeight();
                    this.updateCollisionShapes();
                    velocity.y = 0;
                } // Check if bottom collision overlaps with the tile
                else if (collisionShapeBottom.overlaps(tile)) {
                    position.y = tile.getY() + tile.getHeight();
                    this.updateCollisionShapes();
                    velocity.y = 0;
                    bottomCollision = true;
                }
            }
            // Check if the player is colliding with a tile horizontally or vertically to end the loop
            if (collidedX && collidedY) {
                break;
            }
        }
        if (bottomCollision) {
            if (!isGrounded) {
                // Play sound when the player lands on the ground
                R.getSound(PLAYER_LANDING_SOUND).play(Main.getSoundVolume());
            }
            isJumping = false;
            isGrounded = true;
        } else {
            isGrounded = false;
        }
    }

    public boolean isCollidingWithItem(Rectangle item) {
        return itemCollisionShape.overlaps(item);
    }

}

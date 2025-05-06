package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.CHARACTER_IDLE_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.CHARACTER_MOVE_ANIMATION_SPEED;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.manager.R;

import lombok.Data;

@Data
public class Player extends Character {
    
    public enum State {
        IDLE_RIGHT, IDLE_LEFT, MOVE_RIGHT, MOVE_LEFT, IS_JUMPING
    }

    private int score;
    private int lives;
    private int health;
    private int maxHealth;
    private boolean isJumping;
    private boolean isGrounded;
    private State state;
    private float stateTime;
    private Animation<TextureRegion> idleAnim, rightAnim, leftAnim, jumpAnim;

    public Player(Vector2 startPosition) {
        super(R.getRegions("player_jump").get(0), startPosition);
        // Set animations
        this.setAnimations();
        // Set initial state
        score = 0;
        maxHealth = 3;
        health = maxHealth;
        lives = 3;
        isJumping = true;
        isGrounded = false;
        state = State.IS_JUMPING;
        stateTime = 0f;
    }

    public void setAnimations() {
        // Set idle animations using the idle right animation and flipping it for the left animation
        Array<AtlasRegion> idleRegionsRight = R.getRegions("player_idle");
        Array<AtlasRegion> idleRegionsLeft = new Array<>();
        for (AtlasRegion region : idleRegionsRight) {
            region.flip(true, false);
            idleRegionsLeft.add(region);
        }
        idleAnim = new Animation<>(CHARACTER_IDLE_ANIMATION_SPEED, idleRegionsRight);
        // Set move animations using the move right animation and flipping it for the left animation
        Array<AtlasRegion> moveRegionsRight = R.getRegions("player_move");
        Array<AtlasRegion> moveRegionsLeft = new Array<>();
        for (AtlasRegion region : moveRegionsRight) {
            region.flip(true, false);
            moveRegionsLeft.add(region);
        }
        rightAnim = new Animation<>(CHARACTER_MOVE_ANIMATION_SPEED, moveRegionsRight);
        leftAnim = new Animation<>(CHARACTER_MOVE_ANIMATION_SPEED, moveRegionsLeft);
        // Set jump animations using the jump right animation and flipping it for the left animation
        Array<AtlasRegion> jumpRegionsRight = R.getRegions("player_jump");
        Array<AtlasRegion> jumpRegionsLeft = new Array<>();
        for (AtlasRegion region : jumpRegionsRight) {
            region.flip(true, false);
            jumpRegionsLeft.add(region);
        }
        jumpAnim = new Animation<>(CHARACTER_MOVE_ANIMATION_SPEED, jumpRegionsRight);
    }
    
}

package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_FOOTSTEPS_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_FOOTSTEP_INTERVAL;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_MOVE_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_MOVE_SPEED;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.manager.R;

import lombok.Data;

@Data
public class Ally extends Item {
    private float speed = PLAYER_MOVE_SPEED;
    private float spaceshipX = 800f; // default X position of the spaceship
    private Animation<TextureRegion> animation_move;
    private float stateTime = 0f;
    private float footstepTimer;

    public Ally() {
        super(R.getRegions("ally").get(0));
        this.setAnimations();
        this.collisionShape = new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight() * 5); // Adjusted height to prevent player from passing through
    }

    public Ally(Vector2 position) {
        super(R.getRegions("ally").get(0), position);
        this.setAnimations();
    }

    public void setAnimations() {
        animation_move = new Animation<>(PLAYER_MOVE_ANIMATION_SPEED, R.getRegions("ally"));
    }

    public void update(float deltaTime) {
        if (isCollected && isActive) {
            stateTime += deltaTime;
            currentFrame = animation_move.getKeyFrame(stateTime, true);
            // Move the ally to the right until it reaches spaceship
            position.x += speed * deltaTime;
            collisionShape.setPosition(position.x, position.y);
            // Play footstep sound at intervals
            footstepTimer += deltaTime;
            if (footstepTimer >= PLAYER_FOOTSTEP_INTERVAL) {
                R.getSound(PLAYER_FOOTSTEPS_SOUND).play(Main.getSoundVolume() * 0.5f, 1.0f, 0.0f);
                footstepTimer = 0f;
            }
        }
    }

    public void collectByPlayer(Player player) {
        if (isCollected) {
            return; // Already collected
        }
        System.out.println("Interaction with ally!");
        isCollected = true; 
    }

    public boolean isCollidingWithSpaceship(Rectangle item) {
        if (!isActive) {
            return false; // Ally is not active
        }
        return collisionShape.overlaps(item);
    }

}

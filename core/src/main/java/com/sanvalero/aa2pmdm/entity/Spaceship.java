package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.GAME_COMPLETE_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.KEY_COLLECT_SOUND;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.manager.R;

import lombok.Data;

@Data
public class Spaceship extends Item {

    private float speedY = 0f; // Speed of the spaceship
    private float maxSpeedY = 50000f; // Speed of the spaceship
    private float acceleration = 0.5f; // Acceleration of the spaceship
    private float timeTakingOff = 0f; // Time elapsed since the spaceship was collected

    public Spaceship() {
        super(R.getRegions("spaceship").get(0));
    }

    public Spaceship(Vector2 position) {
        super(R.getRegions("spaceship").get(0), position);
    }

    public void update(float deltaTime) {
        if (isCollected && isActive) {
            // Move the spaceship to the top until it reaches the target position
            speedY += acceleration * deltaTime; // Increase speed over time
            acceleration += 10 * deltaTime; // Increase acceleration over time
            if (speedY > maxSpeedY) { // Limit maximum speed
                speedY = maxSpeedY;
            }
            position.y += speedY * deltaTime;
            if (position.y > 800f) { // Assuming 800f is the target Y position
                isVisible = false;
                isActive = false;
            }
            // Update the time taking off
            timeTakingOff += deltaTime;
        }
    }

    public void collectByPlayer(Player player) {
        if (isCollected) {
            return; // Already collected
        }
        isCollected = true;
        currentFrame = R.getRegions("spaceship").get(3);
        R.getSound(GAME_COMPLETE_SOUND).play(Main.getSoundVolume() * 0.4f);
        player.setVisible(false);
        System.out.println("Game completed!");
    }

    public void addAlly() {
        currentFrame = R.getRegions("spaceship").get(1);
    }
}

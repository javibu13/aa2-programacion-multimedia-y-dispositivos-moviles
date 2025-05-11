package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.COIN_ANIMATION_SPEED;
import static com.sanvalero.aa2pmdm.util.Constants.COIN_COLLECT_SOUND;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.manager.R;

import lombok.Data;

@Data
public class Coin extends Item {
    private int value = 1;
    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

    public Coin() {
        super(R.getRegions("coin").get(0));
        this.setAnimations();
    }

    public Coin(Vector2 position) {
        super(R.getRegions("coin").get(0), position);
        this.setAnimations();
    }

    public void setAnimations() {
        animation = new Animation<>(COIN_ANIMATION_SPEED, R.getRegions("coin"));
    }

    public void update(float deltaTime) {
        if (isActive) {
            stateTime += deltaTime;
            currentFrame = animation.getKeyFrame(stateTime, true);
            // collisionShape.setPosition(position.x, position.y);
        }
    }

    public void collectByPlayer(Player player) {
        isActive = false;
        isVisible = false;
        isCollected = true;
        R.getSound(COIN_COLLECT_SOUND).play(Main.getSoundVolume() * 0.4f);
        player.setScore(player.getScore() + value);
        System.out.println("Coin collected! Score: " + player.getScore());
    }

}

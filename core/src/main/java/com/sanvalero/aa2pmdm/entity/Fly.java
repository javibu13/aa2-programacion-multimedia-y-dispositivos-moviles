package com.sanvalero.aa2pmdm.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sanvalero.aa2pmdm.manager.R;

public class Fly extends Enemy {
    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

    public Fly(Vector2 position) {
        super(R.getRegions("enemy_fly").get(0), position);
        this.setAnimations();
    }

    public Fly(Vector2 position, Vector2 velocity) {
        super(R.getRegions("enemy_fly").get(0), position, velocity);
        this.setAnimations();
    }

    public void setAnimations() {
        animation = new Animation<>(0.1f, R.getRegions("enemy_fly"));
    }

    public void update(float deltaTime) {
        if (!isActive) {
            return; // Skip update if not active
        }
        stateTime += deltaTime;
        currentFrame = animation.getKeyFrame(stateTime, true);
    }
    
}

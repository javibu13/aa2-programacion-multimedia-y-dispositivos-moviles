package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.ENEMY_FLY_ANIMATION_SPEED;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
    }
    
}

package com.sanvalero.aa2pmdm.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy extends Character {
    
    public Enemy(TextureRegion currentFrame) {
        super(currentFrame);
        initEnemy();
    }

    public Enemy(TextureRegion currentFrame, Vector2 position) {
        super(currentFrame, position);
        initEnemy();
    }

    public Enemy(TextureRegion currentFrame, Vector2 position, Vector2 velocity) {
        super(currentFrame, position, velocity);
        initEnemy();
    }

    private void initEnemy() {
        this.isActive = true;
        this.isVisible = true;
    }

    public abstract void update(float deltaTime);
}

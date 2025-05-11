package com.sanvalero.aa2pmdm.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Character {
    
    public Enemy(TextureRegion currentFrame) {
        super(currentFrame);
    }

    public Enemy(TextureRegion currentFrame, Vector2 position) {
        super(currentFrame, position);
    }

    public Enemy(TextureRegion currentFrame, Vector2 position, Vector2 velocity) {
        super(currentFrame, position, velocity);
    }

}

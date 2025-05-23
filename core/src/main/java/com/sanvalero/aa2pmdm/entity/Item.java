package com.sanvalero.aa2pmdm.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import lombok.Data;

@Data
public abstract class Item extends Entity { 
    protected boolean isCollected; // Indicates if the item has been collected by the player

    public Item(TextureRegion currentFrame) {
        super(currentFrame);
        this.isActive = true;
        this.isVisible = true;
        this.isCollected = false;
        createCollisionShape();
    }

    public Item(TextureRegion currentFrame, Vector2 position) {
        super(currentFrame, position);
        this.isActive = true;
        this.isVisible = true;
        this.isCollected = false;
        createCollisionShape();
    }

    public abstract void update(float deltaTime);
}

package com.sanvalero.aa2pmdm.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import lombok.Data;

@Data
public class Entity {
    protected TextureRegion currentFrame;
    protected Vector2 position;
    protected boolean isVisible; // Indicates if the entity is visible on the screen
    protected boolean isActive; // Indicates if the item is active in the game to be interacted with or controlled
    public Rectangle collisionShape;

    public Entity(TextureRegion currentFrame) {
        this.currentFrame = currentFrame;
        this.position = new Vector2(0, 0);
    }

    public Entity(TextureRegion currentFrame, Vector2 position) {
        this.currentFrame = currentFrame;
        this.position = position;
    }

    public void createCollisionShape() {
        this.collisionShape = new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public float getWidth() {
        return currentFrame.getRegionWidth();
    }

    public float getHeight() {
        return currentFrame.getRegionHeight();
    }
}

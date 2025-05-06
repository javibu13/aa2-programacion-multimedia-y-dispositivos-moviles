package com.sanvalero.aa2pmdm.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public abstract class Character {
    protected TextureRegion currentFrame;
    protected Vector2 position;
    protected Vector2 velocity;
    protected Rectangle collisionShape;

    public Character(TextureRegion currentFrame) {
        this.currentFrame = currentFrame;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.collisionShape = new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public Character(TextureRegion currentFrame, Vector2 position) {
        this.currentFrame = currentFrame;
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.collisionShape = new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public Character(TextureRegion currentFrame, Vector2 position, Vector2 velocity) {
        this.currentFrame = currentFrame;
        this.position = position;
        this.velocity = velocity;
        this.collisionShape = new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public float getWidth() {
        return currentFrame.getRegionWidth();
    }

    public float getHeight() {
        return currentFrame.getRegionHeight();
    }

    public void move(float x, float y) {
        position.add(x, y);
        collisionShape.setPosition(position.x, position.y);
    }
}

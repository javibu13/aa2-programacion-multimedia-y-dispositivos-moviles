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
    public Rectangle collisionShape;
    public Rectangle collisionShapeRight;
    public Rectangle collisionShapeLeft;
    public Rectangle collisionShapeTop;
    public Rectangle collisionShapeBottom;

    public Character(TextureRegion currentFrame) {
        this.currentFrame = currentFrame;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        createCollisionShapes();
    }

    public Character(TextureRegion currentFrame, Vector2 position) {
        this.currentFrame = currentFrame;
        this.position = position;
        this.velocity = new Vector2(0, 0);
        createCollisionShapes();
    }

    public Character(TextureRegion currentFrame, Vector2 position, Vector2 velocity) {
        this.currentFrame = currentFrame;
        this.position = position;
        this.velocity = velocity;
        createCollisionShapes();
    }
    
    private void createCollisionShapes() {
        this.collisionShape = new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        this.collisionShapeRight = new Rectangle(position.x + (currentFrame.getRegionWidth()/2f), position.y + (currentFrame.getRegionHeight()/4f), currentFrame.getRegionWidth()/2f, currentFrame.getRegionHeight()/2f);
        this.collisionShapeLeft = new Rectangle(position.x, position.y + (currentFrame.getRegionHeight()/4f), currentFrame.getRegionWidth()/2f, currentFrame.getRegionHeight()/2f);
        this.collisionShapeTop = new Rectangle(position.x + (currentFrame.getRegionWidth()/4f), position.y + (currentFrame.getRegionHeight()/2f), currentFrame.getRegionWidth()/2f, currentFrame.getRegionHeight()/2f);
        this.collisionShapeBottom = new Rectangle(position.x + (currentFrame.getRegionWidth()/4f), position.y, currentFrame.getRegionWidth()/2f, currentFrame.getRegionHeight()/2f);        
    }

    public float getWidth() {
        return currentFrame.getRegionWidth();
    }

    public float getHeight() {
        return currentFrame.getRegionHeight();
    }

    public void move(float x, float y) {
        position.add(x, y);
        updateCollisionShapes();
        System.out.println("Position: " + position.x + ", " + position.y);
    }

    public void updateCollisionShapes() {
        collisionShape.setPosition(position.x, position.y);
        collisionShapeRight.setPosition(position.x + (currentFrame.getRegionWidth()/2f), position.y + (currentFrame.getRegionHeight()/4f));
        collisionShapeLeft.setPosition(position.x, position.y + (currentFrame.getRegionHeight()/4f));
        collisionShapeTop.setPosition(position.x + (currentFrame.getRegionWidth()/4f), position.y + (currentFrame.getRegionHeight()/2f));
        collisionShapeBottom.setPosition(position.x + (currentFrame.getRegionWidth()/4f), position.y);
    }

    public void setVelocityX(float speedX) {
        velocity.x = speedX;
    }

    public void setVelocityY(float speedY) {
        velocity.y = speedY;
    }
}

package com.sanvalero.aa2pmdm.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public abstract class Character extends Entity {
    protected Vector2 velocity;
    public Rectangle collisionShapeRight;
    public Rectangle collisionShapeLeft;
    public Rectangle collisionShapeTop;
    public Rectangle collisionShapeBottom;

    public Character(TextureRegion currentFrame) {
        super(currentFrame);
        this.velocity = new Vector2(0, 0);
        createCollisionShapes();
    }

    public Character(TextureRegion currentFrame, Vector2 position) {
        super(currentFrame, position);
        this.velocity = new Vector2(0, 0);
        createCollisionShapes();
    }

    public Character(TextureRegion currentFrame, Vector2 position, Vector2 velocity) {
        super(currentFrame, position);
        this.velocity = velocity;
        createCollisionShapes();
    }
    
    private void createCollisionShapes() {
        createCollisionShape();
        this.collisionShapeRight = new Rectangle(position.x + (currentFrame.getRegionWidth()/2f), position.y + (currentFrame.getRegionHeight()/4f), currentFrame.getRegionWidth()/2f, currentFrame.getRegionHeight()/2f);
        this.collisionShapeLeft = new Rectangle(position.x, position.y + (currentFrame.getRegionHeight()/4f), currentFrame.getRegionWidth()/2f, currentFrame.getRegionHeight()/2f);
        this.collisionShapeTop = new Rectangle(position.x + (currentFrame.getRegionWidth()/4f), position.y + (currentFrame.getRegionHeight()/2f), currentFrame.getRegionWidth()/2f, currentFrame.getRegionHeight()/2f);
        this.collisionShapeBottom = new Rectangle(position.x + (currentFrame.getRegionWidth()/4f), position.y, currentFrame.getRegionWidth()/2f, currentFrame.getRegionHeight()/2f);        
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

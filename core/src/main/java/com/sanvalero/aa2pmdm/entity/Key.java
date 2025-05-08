package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.KEY_COLLECT_SOUND;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sanvalero.aa2pmdm.manager.R;

import lombok.Data;

@Data
public class Key extends Item {

    private float baseY;
    private float elapsedTime = 0f;
    private float floatingAmplitude = 2.6f;
    private float floatingFrequency = 1.8f;

    public Key() {
        super(R.getRegions("key").get(1));
        this.baseY = position.y;
    }

    public Key(Vector2 position) {
        super(R.getRegions("key").get(1), position);
        this.baseY = position.y;
    }

    public void update(float deltaTime) {
        if (isActive) {
            elapsedTime += deltaTime;
            float offsetY = MathUtils.sin(elapsedTime * floatingFrequency) * floatingAmplitude;
            position.y = baseY + offsetY;
        }
    }

    public void collectByPlayer(Player player) {
        isActive = false;
        isVisible = false;
        isCollected = true;
        R.getSound(KEY_COLLECT_SOUND).play(0.4f);
        player.setKey(true);
        System.out.println("Key collected!");
    }

}

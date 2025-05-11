package com.sanvalero.aa2pmdm.entity;

import static com.sanvalero.aa2pmdm.util.Constants.KEY_COLLECT_SOUND;

import com.badlogic.gdx.math.Vector2;
import com.sanvalero.aa2pmdm.manager.R;

import lombok.Data;

@Data
public class Exit extends Item {
    private boolean isOpen = false;

    public Exit() {
        super(R.getRegions("door").get(0));
    }

    public Exit(Vector2 position) {
        super(R.getRegions("door").get(0), position);
    }

    public void update(float deltaTime) {
        // Not used for Exit at this moment
    }

    // public void interactedByPlayer(Player player) {
    //     if (player.isKey()) {
    //         R.getSound(KEY_COLLECT_SOUND).play(0.4f); // TODO: Change to a different sound
    //         System.out.println("Go to next level!");
    //     } else {
    //         R.getSound(KEY_COLLECT_SOUND).play(0.4f); // TODO: Change to a different sound
    //         System.out.println("You need a key to open this door!");
    //     }
    // }

    public void open() {
        currentFrame = R.getRegions("door").get(1);
        isOpen = true;
    }

    public void close() {
        currentFrame = R.getRegions("door").get(0);
        isOpen = false;
    }

}

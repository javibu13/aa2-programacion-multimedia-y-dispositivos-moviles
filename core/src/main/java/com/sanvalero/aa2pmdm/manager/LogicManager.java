package com.sanvalero.aa2pmdm.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.entity.Coin;
import com.sanvalero.aa2pmdm.entity.Item;
import com.sanvalero.aa2pmdm.entity.Player;
import com.sanvalero.aa2pmdm.screen.GameScreen;
import com.sanvalero.aa2pmdm.screen.PauseScreen;

import lombok.Data;

@Data
public class LogicManager {

    private Main game;
    public Player player;
    public Array<Item> items;
    
    public LogicManager(Main game) {
        this.game = game;
    }

    public boolean isDebugMode() {
        return game.debug;
    }

    private void manageInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setState(Player.State.MOVE_RIGHT);
            player.setVelocityX(delta * player.getMoveSpeed());
            // player.move(player.getMoveSpeed() * delta, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setState(Player.State.MOVE_LEFT);
            player.setVelocityX(-delta * player.getMoveSpeed());
            // player.move(-player.getMoveSpeed() * delta, 0); 
        } else {
            if ((player.getState() == Player.State.MOVE_LEFT) || (player.getState() == Player.State.IDLE_LEFT) || (player.getState() == Player.State.IS_JUMPING_LEFT)) {
                player.setState(Player.State.IDLE_LEFT);
                player.setVelocityX(0f);
            } else {
                player.setState(Player.State.IDLE_RIGHT);
                player.setVelocityX(0f);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.jump();
        }
        if (player.isJumping()) {
            if (player.getState() == Player.State.MOVE_LEFT || player.getState() == Player.State.IDLE_LEFT) {
                player.setState(Player.State.IS_JUMPING_LEFT);
            } else {
                player.setState(Player.State.IS_JUMPING_RIGHT);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            // Pause the game and show the pause menu
            game.pause = true;
            game.setScreen(new PauseScreen(game, game.getScreen()));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            game.debug = !game.debug;
        }
    }

    public void manageCollision() {
        // Check for collisions between player and items
        Array<Item> itemsToDelete = new Array<>(); // It could be replaced by a inverted for loop to impove performance
        for (Item item : items) {
            if (item.isActive() && player.isCollidingWithItem(item.getCollisionShape())) {
                if (item instanceof Coin) {
                    // Handle collision with COIN
                    ((Coin) item).collectByPlayer(player);
                    // Add the coin to be deleted
                    itemsToDelete.add(item);
                }
            }
        }
        // Remove items marked for deletion to avoid concurrent issues, memory leaks and item skipping
        for (Item item : itemsToDelete) { // It could be replaced by a inverted for loop to impove performance
            items.removeValue(item, true);
        }
    }

    public void update(float delta) {
        // Logic game loop
        manageCollision();
        manageInput(delta);
        // Update
        // // Update player
        player.update(delta);
        // // Update items
        for (Item item : items) {
            item.update(delta);
        }
    }
}

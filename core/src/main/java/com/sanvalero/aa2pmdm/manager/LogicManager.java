package com.sanvalero.aa2pmdm.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.entity.Ally;
import com.sanvalero.aa2pmdm.entity.Coin;
import com.sanvalero.aa2pmdm.entity.Enemy;
import com.sanvalero.aa2pmdm.entity.Exit;
import com.sanvalero.aa2pmdm.entity.Item;
import com.sanvalero.aa2pmdm.entity.Key;
import com.sanvalero.aa2pmdm.entity.Player;
import com.sanvalero.aa2pmdm.entity.Spaceship;
import com.sanvalero.aa2pmdm.screen.GameOverScreen;
import com.sanvalero.aa2pmdm.screen.GameScreen;
import com.sanvalero.aa2pmdm.screen.PauseScreen;

import lombok.Data;

@Data
public class LogicManager {

    private Main game;
    private int level;
    public Player player;
    public Array<Item> items;
    public Array<Enemy> enemies;
    public Exit exit;
    public TiledMapTileLayer groundLayer;
    public TiledMapTileLayer deathLayer;
    // GameOver level
    public Ally ally;
    public Spaceship spaceship;
    public MapLayer imageLayer;
    
    public LogicManager(Main game) {
        this.game = game;
        this.level = 1;
    }

    public LogicManager(Main game, int level) {
        this.game = game;
        this.level = level;
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2) && game.debug) {
            player.heal(1);
        }

    }

    public void manageCollision() {
        manageItemCollision();
        manageEnemyCollision();
        manageExitCollision();
    }

    public void manageItemCollision() {
        // Check for collisions between player and items
        Array<Item> itemsToDelete = new Array<>(); // It could be replaced by a inverted for loop to impove performance
        for (Item item : items) {
            if (item.isActive() && player.isCollidingWithItem(item.getCollisionShape())) {
                if (item instanceof Coin) {
                    // Handle collision with COIN
                    ((Coin) item).collectByPlayer(player);
                    // Add the coin to be deleted
                    itemsToDelete.add(item);
                } else if (item instanceof Key) {
                    // Handle collision with KEY
                    ((Key) item).collectByPlayer(player);
                    // Add the key to be deleted
                    itemsToDelete.add(item);
                    exit.open();
                } else if (item instanceof Ally) {
                    // Handle collision with ALLY
                    ((Ally) item).collectByPlayer(player);
                } else if (item instanceof Spaceship) {
                    // Handle collision with SPACESHIP
                    ((Spaceship) item).collectByPlayer(player);
                }
            }
            if (level == -1) {
                // GAME OVER - LEVEL
                if (ally == null || spaceship == null) {
                    if (item instanceof Ally) {
                        ally = (Ally) item;
                    } else if (item instanceof Spaceship) {
                        spaceship = (Spaceship) item;
                    }
                } else if (item instanceof Ally && ally.isCollidingWithSpaceship(spaceship.getCollisionShape())) {
                    System.out.println("Sapceship reached!");
                    // Ally has reached the spaceship
                    ally.setActive(false);
                    ally.setVisible(false);
                    spaceship.addAlly();
                }
                if (spaceship != null && spaceship.getTimeTakingOff() > 3f) {
                    imageLayer.setVisible(true);
                }
            }
        }
        // Remove items marked for deletion to avoid concurrent issues, memory leaks and item skipping
        for (Item item : itemsToDelete) { // It could be replaced by a inverted for loop to impove performance
            items.removeValue(item, true);
        }
    }

    public void manageEnemyCollision() {
        // Check for collisions between player and enemies
        for (Enemy enemy : enemies) {
            if (enemy.isActive() && player.getItemCollisionShape().overlaps(enemy.getCollisionShape())) {
                enemy.collideWithPlayer(player);
                System.out.println("Player collided with enemy!");
                if (player.getHealth() <= 0) {
                    // Player is dead
                    System.out.println("Game Over!");
                    // Set the game over screen
                    game.setScreen(new GameOverScreen(game));
                }
            }
        }
    }

    public void manageExitCollision() {
        // Check for collisions between player and exit
        if (exit.isOpen() && player.isCollidingWithItem(exit.getCollisionShape())) {
            // Change to the next level
            System.out.println("Go to next level! (Level " + (level + 1) + ")");
            // Update the global run variable
            Main.playerScore += player.getScore();
            Main.playerTime += player.getPlayedTime();
            // Create a new GameScreen with the next level and set it as the current screen
            game.setScreen(new GameScreen(game, level + 1));
        } else if (!exit.isOpen() && player.isCollidingWithItem(exit.getCollisionShape())) {
            // Show message to player
            System.out.println("You need a key to open this door!");
        }
    }

    public boolean isGameOver() {
        // Check if the player is dead
        if (player.getHealth() <= 0) {
            return true;
        } else {
            return false;
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
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
    }
}

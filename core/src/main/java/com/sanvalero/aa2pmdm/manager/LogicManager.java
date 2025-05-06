package com.sanvalero.aa2pmdm.manager;

import com.sanvalero.aa2pmdm.Main;
import com.sanvalero.aa2pmdm.entity.Player;

import lombok.Data;

@Data
public class LogicManager {

    private Main game;
    public Player player;
    
    public LogicManager(Main game) {
        this.game = game;
    }

    public void update(float delta) {
        // Logic game loop
    }
}

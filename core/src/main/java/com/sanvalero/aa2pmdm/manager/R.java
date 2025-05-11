package com.sanvalero.aa2pmdm.manager;

import static com.sanvalero.aa2pmdm.util.Constants.BACKGROUND_MUSIC;
import static com.sanvalero.aa2pmdm.util.Constants.COIN_COLLECT_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.GAME_COMPLETE_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.KEY_COLLECT_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_FOOTSTEPS_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_HEAL_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_HURT_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_JUMP_SOUND;
import static com.sanvalero.aa2pmdm.util.Constants.PLAYER_LANDING_SOUND;

import java.io.File;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class R {
    private static String TEXTURE_ATLAS = "atlas/aa2pmdm.atlas";
    private static String SOUNDS = "sounds";
    private static String MUSIC = "music";

    private static AssetManager assetManager = new AssetManager();

    public static boolean update() {
        return assetManager.update();
    }

    public static void loadAllResources() {
        assetManager.load(TEXTURE_ATLAS, TextureAtlas.class);
        loadAllSoundsAndMusic(); 
    }

    private static void loadAllSoundsAndMusic() {
        // Load all sounds
        assetManager.load(SOUNDS + File.separator + PLAYER_FOOTSTEPS_SOUND, Sound.class);
        assetManager.load(SOUNDS + File.separator + PLAYER_JUMP_SOUND, Sound.class);
        assetManager.load(SOUNDS + File.separator + PLAYER_LANDING_SOUND, Sound.class);
        assetManager.load(SOUNDS + File.separator + PLAYER_HURT_SOUND, Sound.class);
        assetManager.load(SOUNDS + File.separator + PLAYER_HEAL_SOUND, Sound.class);
        assetManager.load(SOUNDS + File.separator + COIN_COLLECT_SOUND, Sound.class);
        assetManager.load(SOUNDS + File.separator + KEY_COLLECT_SOUND, Sound.class);
        assetManager.load(SOUNDS + File.separator + GAME_COMPLETE_SOUND, Sound.class);
        // Load all music
        assetManager.load(MUSIC + File.separator + BACKGROUND_MUSIC, Music.class);
    }

    public static Sound getSound(String name) {
        return assetManager.get(SOUNDS + File.separator + name, Sound.class);
    }

    public static Music getMusic(String name) {
        return assetManager.get(MUSIC + File.separator + name, Music.class);
    }

    public static TextureRegion getTexture(String name) {
        return assetManager.get(TEXTURE_ATLAS, TextureAtlas.class).findRegion(name);
    }

    public static Array<TextureAtlas.AtlasRegion> getRegions(String name) {
        return assetManager.get(TEXTURE_ATLAS, TextureAtlas.class).findRegions(name);
    }

    public static void dispose() {
        assetManager.dispose();
    }
}

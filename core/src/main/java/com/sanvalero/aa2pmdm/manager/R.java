package com.sanvalero.aa2pmdm.manager;

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
        // TODO: Load all sounds and music files from the SOUNDS and MUSIC directories
        // assetManager.load(SOUNDS + File.separator + "sound.mp3", Sound.class);
        // assetManager.load(MUSIC + File.separator + "music.mp3", Music.class);
    }

    public static Sound getSound(String name) {
        return assetManager.get(SOUNDS + File.separator + name + ".mp3", Sound.class);
    }

    public static Music getMusic(String name) {
        return assetManager.get(SOUNDS + File.separator + name + ".mp3", Music.class);
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

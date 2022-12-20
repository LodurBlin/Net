package net.game.Utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class B2dAssetManager {
    public final AssetManager manager = new AssetManager();

    // Textures
    public final String playerImages = "images/Nick/Nick.pack";
    public final String mainImages = "images/testLvl/test.pack";
    public final String loadingImages = "loading/load.pack";
    public final String menuImages = "images/menu/menu.pack";
    // Sounds
    public final String shockedSound = "sounds/shocked.mp3";
    public final String hurtSound = "sounds/hurt.mp3";
    // Music
    public final String playingSong = "music/Rolemusic - pl4y1ng.mp3";
    public final String menuSong = "music/Menu.mp3";
    // Skin
    public final String skin = "skin/glassy-ui.json";
    //Particles
    public final String smokeEffect = "Particles/smoke.pe";
    public final String waterEffect = "Particles/water.pe";
    public final String fireEffect = "Particles/fire.pe";

    public void queueAddFonts(){

    }

    public void queueAddParticleEffects(){
        ParticleEffectLoader.ParticleEffectParameter pep = new ParticleEffectLoader.ParticleEffectParameter();
        pep.atlasFile = "loading/load.pack";
        /*
        manager.load(smokeEffect, ParticleEffect.class, pep);
        manager.load(waterEffect, ParticleEffect.class, pep);
        manager.load(fireEffect, ParticleEffect.class, pep);

         */
    }

    public void queueAddSkin(){
        SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
        manager.load(skin, Skin.class, params);
    }

    public void queueAddMusic(){
        manager.load(playingSong, Music.class);
        manager.load(menuSong, Music.class);
    }

    public void queueAddSounds(){
        manager.load(shockedSound, Sound.class);
        manager.load(hurtSound, Sound.class);
    }

    public void queueAddImages(){
        manager.load(playerImages, TextureAtlas.class);
        manager.load(mainImages, TextureAtlas.class);
        manager.load(menuImages, TextureAtlas.class);
    }

    // a small set of images used by the loading screen
    public void queueAddLoadingImages(){
        manager.load(loadingImages, TextureAtlas.class);
    }


}

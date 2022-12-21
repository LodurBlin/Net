package net.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import net.game.Screens.*;
import net.game.Screens.Levels.*;
import net.game.Utils.B2dAssetManager;

import com.badlogic.gdx.Game;

import static net.game.Utils.Constants.*;


public class CoolGame extends Game {
    //TODO add remove body from world code
    //TODO remove items off screen
    //TODO make enemies die if shot and fall offscreen(off screen should remove from abouve task)



    private LoadingScreen loadingScreen;
    private PreferencesScreen preferencesScreen;
    private Menu menuScreen;
    private MainScreen mainScreen;
    private EndScreen endScreen;
    private AppPreferences preferences;
    public B2dAssetManager assMan = new B2dAssetManager();
    private Music playingSong;



    public int lastScore = 0;

    @Override
    public void create () {
        loadingScreen = new LoadingScreen(this);
        preferences = new AppPreferences();
        setScreen(loadingScreen);

        // tells our asset manger that we want to load the images set in loadImages method
        assMan.queueAddMusic();
        // tells the asset manager to load the images and wait until finished loading.
        assMan.manager.finishLoading();
        // loads the 2 sounds we use
        playingSong = assMan.manager.get("music/Rolemusic - pl4y1ng.mp3");

        //playingSong.play();

    }

    @Override
    public void render () {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        super.render();
    }

    public void changeScreen(Screens s){
        switch(s){
            case MENU:
                if(menuScreen == null) menuScreen = new Menu(this);
                this.setScreen(menuScreen);
                break;
            case PREFERENCES:
                if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
                this.setScreen(preferencesScreen);
                break;
            case MAIN:
                // always make new game screen so game can't start midway
                mainScreen = new MainScreen(this);
                if(mainScreen == null){
                    mainScreen = new MainScreen(this);
                }else{
                    mainScreen.resetWorld();
                }
                this.setScreen(mainScreen);
                break;
            case ENDGAME:
                if(endScreen == null) endScreen = new EndScreen(this);
                this.setScreen(endScreen);
                break;
        }
    }

    public AppPreferences getPreferences(){
        return this.preferences;
    }

    @Override
    public void dispose(){
        playingSong.dispose();
        assMan.manager.dispose();
    }
}

package net.game.Screens;
import net.game.CoolGame;
import net.game.Sprites.LoadingBar;

import static net.game.Utils.Constants.MENU;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class LoadingScreen implements Screen {
    private CoolGame game;
    private Stage stage;
    private TextureAtlas atlas;
    private AtlasRegion title; // title, background, copyright;
    private AssetManager man;
    private Animation flameAnimation;
    private float stateTime;
    private Table table, loadingTable = new Table();
    private Image titleImage;

    //public final int IMAGE = 0;		// loading images
    public final int FONT = 1;		// loading fonts
    public final int PARTY = 2;		// loading particle effects
    public final int SOUND = 3;		// loading sounds
    public final int MUSIC = 4;		// loading music

    private int currentLoadingStage = 0;
    // timer for exiting loading screen
    public float countDown = 4f;

    public LoadingScreen(CoolGame coolGame){
        game = coolGame;
        stage = new Stage(new ScreenViewport());
        man = game.assMan.manager;
        loadAssets();
        game.assMan.queueAddImages();
        System.out.println("Loading images....");
    }

    private void loadAssets() {
        game.assMan.queueAddLoadingImages();
        man.finishLoading();
        atlas = man.get(game.assMan.loadingImages);

        title = atlas.findRegion("Title");
        flameAnimation = new Animation(0.3f, atlas.findRegions("dot"), Animation.PlayMode.LOOP);
    }
    @Override
    public void show() {
        titleImage = new Image(title);
        table = new Table();
        loadingTable = new Table();
        table.setFillParent(true);
        table.setDebug(false);

        for (int j=0; j<4; j++){
            loadingTable.add(new LoadingBar(flameAnimation));
        }

        table.add(titleImage).align(Align.center).pad(10, 0, 0, 0).colspan(10);
        table.row(); // move to next row
        table.add(loadingTable).width(400);

        stage.addActor(table);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0f,0f,0f); //  clear the screen
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += delta; // Accumulate elapsed animation time
        // Get current frame of animation for the current stateTime

        // check if the asset manager has finished loading
        if (game.assMan.manager.update()) { // Load some, will return true if done loading
            currentLoadingStage+= 1;
            if(currentLoadingStage <= 2){ //Два - число ячеек, в данном случае тайтл и анимация
                loadingTable.getCells().get((currentLoadingStage-1)*2).getActor().setVisible(true);
                loadingTable.getCells().get((currentLoadingStage-1)*2+1).getActor().setVisible(true);
            }
            switch(currentLoadingStage){
                case FONT:
                    System.out.println("Loading fonts....");
                    game.assMan.queueAddFonts();
                    break;
                case PARTY:
                    System.out.println("Loading Particle Effects....");
                    game.assMan.queueAddParticleEffects();
                    break;
                case SOUND:
                    System.out.println("Loading Sounds....");
                    game.assMan.queueAddSounds();
                    break;
                case MUSIC:
                    System.out.println("Loading fonts....");
                    game.assMan.queueAddMusic();
                    break;
                case 5:
                    System.out.println("Finished");
                    break;
            }
            if (currentLoadingStage >5){
                countDown -= delta;  // timer to stay on loading screen for short period once done loading
                currentLoadingStage = 5;  // cap loading stage to 5 as will use later to display progress bar and more than 5 would go off the screen
                if(countDown < 0){ // countdown is complete
                    game.changeScreen(MENU);  /// go to menu screen
                }
            }
        }
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height,true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        man.dispose();
        atlas.dispose();
        game.dispose();
        stage.dispose();
    }
}


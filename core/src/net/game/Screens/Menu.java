package net.game.Screens;
import net.game.CoolGame;
import static net.game.Utils.Constants.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class Menu implements Screen {
    private CoolGame game;
    private Stage stage;
    private Skin skin;
    private Music music;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion background;
    public Menu(CoolGame game){
        this.game = game;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        game.assMan.queueAddSkin();
        game.assMan.queueAddMusic();
        game.assMan.manager.finishLoading();
        skin = game.assMan.manager.get(game.assMan.skin);
        atlas = game.assMan.manager.get(game.assMan.menuImages);
        background = atlas.findRegion("background");
        music = game.assMan.manager.get(game.assMan.menuSong);
        music.play();
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton level0 = new TextButton("level0", skin);
        //TextButton level1 = new TextButton("level1", skin);
        TextButton exit = new TextButton("Exit", skin);
        TextButton preferences = new TextButton("Preferences", skin);


        table.add(level0).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        //table.add(level1).fillX().uniformX();
        //table.row();
        table.add(exit).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fillX().uniformX();

        table.setBackground(new TiledDrawable(background));

        //button listeners

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        level0.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                music.pause();
                game.changeScreen(MAIN);

            }
        });

        /*
        level1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                music.pause();
                game.changeScreen(TEST);

            }
        });

         */

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(PREFERENCES);
            }
        });

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0,0.5f,0.2f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        game.dispose();
        skin.dispose();
        music.dispose();
    }

}


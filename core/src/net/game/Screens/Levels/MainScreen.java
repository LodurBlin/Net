package net.game.Screens.Levels;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.game.Controllers.MyKeyboard;
import net.game.CoolGame;
import net.game.Entity.Components.PlayerComponent;
import net.game.Entity.Systems.*;
import net.game.Utils.DFUtils;
import net.game.Utils.LevelFactory;
import static net.game.Utils.Constants.Screens.*;


public class MainScreen implements Screen {
    private CoolGame parent;
    private OrthographicCamera cam;
    private SpriteBatch sb;
    private PooledEngine engine;
    private LevelFactory lvlFactory;
    private MyKeyboard controller;
    private Entity player;
    private Music music;

    /*
    Здесь вы можете видеть, что мы начали использовать класс DFUtils для создания некоторых
    изображений с использованием некоторых HEX-значений вместо создания изображений и их загрузки.
     */
    public MainScreen(CoolGame box2dTutorial) {
        parent = box2dTutorial;
        parent.assMan.queueAddMusic();
        parent.assMan.manager.finishLoading();
        music = parent.assMan.manager.get(parent.assMan.playingSong);
        music.play();
        controller = new MyKeyboard();
        engine = new PooledEngine();
        lvlFactory = new LevelFactory(engine,parent.assMan);


        sb = new SpriteBatch();
        RenderingSystem renderingSystem = new RenderingSystem(sb);
        cam = renderingSystem.getCamera();
        ParticleEffectSystem particleSystem = new ParticleEffectSystem(sb,cam);
        sb.setProjectionMatrix(cam.combined);

        engine.addSystem(new AnimationSystem());
        engine.addSystem(new PhysicsSystem(lvlFactory.world));
        engine.addSystem(renderingSystem);
        // not a fan of splitting batch into rendering and particles, but I like the separation of the systems
        engine.addSystem(particleSystem); // particle get drawns on top so should be placed after normal rendering
        //engine.addSystem(new PhysicsDebugSystem(lvlFactory.world, renderingSystem.getCamera())); //ДЕБАГИ
        engine.addSystem(new CollisionSystem(parent.assMan));
        engine.addSystem(new SteeringSystem());
        engine.addSystem(new PlayerControlSystem(controller));
        player = lvlFactory.createPlayer(cam);
        engine.addSystem(new EnemySystem(lvlFactory));
        engine.addSystem(new WallSystem(lvlFactory));
        engine.addSystem(new WaterFloorSystem(lvlFactory));
        engine.addSystem(new BulletSystem(lvlFactory));
        engine.addSystem(new LevelGenerationSystem(lvlFactory));


        lvlFactory.createFloor();
        lvlFactory.createWaterFloor();
        lvlFactory.createBackground();
        lvlFactory.createWalls();
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);

        //check if player is dead. if so show end screen
        PlayerComponent pc = (player.getComponent(PlayerComponent.class));
        if(pc.isDead){
            DFUtils.log("YOU DIED : back to menu you go!");
            parent.lastScore = (int) pc.cam.position.y;
            parent.changeScreen(ENDGAME);
            music.pause();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
            parent.changeScreen(MENU);
            music.pause();
        }

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        sb.dispose();
        engine.clearPools();
    }
    public void resetWorld(){
        System.out.println("Resetting world");
        engine.removeAllEntities();
        lvlFactory.resetWorld();

        player = lvlFactory.createPlayer(cam);
        lvlFactory.createFloor();
        lvlFactory.createWaterFloor();
        lvlFactory.createBackground();
        lvlFactory.createWalls();

        // reset controller controls (fixes bug where controller stuck on directrion if died in that position)
        controller.left = false;
        controller.right = false;
        controller.up = false;
        controller.down = false;
        controller.isMouse1Down = false;
        controller.isMouse2Down = false;
        controller.isMouse3Down = false;
        music.play();

    }
}

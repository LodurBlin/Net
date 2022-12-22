package net.game.Utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import net.game.Entity.Components.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import net.game.SimplexNoise.OpenSimplexNoise;
import net.game.ai.SteeringPresets;

import static net.game.Utils.Constants.*;

public class LevelFactory {
    private BodyFactory bodyFactory;
    public World world;
    private PooledEngine engine;
    public int currentLevel = 0;
    private TextureRegion floorTex, enemyTex, waterTex, platformTex, bulletTex, seekerTex, wallTex;
    private TextureAtlas atlas;
    private OpenSimplexNoise openSim;
    private ParticleEffectManager pem;
    public Entity player;
    private int PPM = 16;
    //public B2dAssetManager assman;

    public LevelFactory(PooledEngine en, B2dAssetManager assMan){
        engine = en;
        this.atlas = assMan.manager.get(assMan.gameImages, TextureAtlas.class);
        assMan.manager.finishLoadingAsset(assMan.gameImages);

        floorTex = atlas.findRegion("floor");
        enemyTex = atlas.findRegion("Peppa");
        seekerTex  = atlas.findRegion("Masha");
        waterTex  = atlas.findRegion("water");
        bulletTex = atlas.findRegion("bullet");
        platformTex = atlas.findRegion("tileset");
        wallTex = atlas.findRegion("wall");

        /*
        this.assman = assMan;
        pem = new ParticleEffectManager();
        pem.addParticleEffect(ParticleEffectManager.FIRE, assMan.manager.get(assMan.fireEffect, ParticleEffect.class),1f/128f);
        pem.addParticleEffect(ParticleEffectManager.WATER, assMan.manager.get(assMan.waterEffect,ParticleEffect.class),1f/8f);
        pem.addParticleEffect(ParticleEffectManager.SMOKE, assMan.manager.get(assMan.smokeEffect,ParticleEffect.class),1f/64f);

         */



        world = new World(new Vector2(0,-10f), true);
        world.setContactListener(new B2dContactListener());
        bodyFactory = BodyFactory.getInstance(world);

        openSim = new OpenSimplexNoise(MathUtils.random(2000L));



    }


    /** Creates a pair of platforms per level up to yLevel
     * @param ylevel
     */
    public void generateLevel(int ylevel){
        while(ylevel > currentLevel){
            for(int i = 1; i < 5; i ++){
                generateSingleColumn(i);
            }
            currentLevel++;
        }
    }

    // generate noise for level
    private float genNForL(int level, int height){
        return (float)openSim.eval(height, level);
    }

    private void generateSingleColumn(int i){ //СОЗДАЁТ ВСЕ ВАЩЕ
        int offset = 20 * i; //Отвечает за ширину генерации сущностей
        int range = 50; //Отвечает за высоту генерации сущностей
        if(genNForL(i,currentLevel) > -0.5f){
            createPlatform(genNForL(i * 100,currentLevel) * range + offset ,currentLevel * 2); //создаёт платформы
            if(genNForL(i * 200,currentLevel) > 0.3f){
                // создает пружинистые платформы
                createBouncyPlatform(genNForL(i * 100,currentLevel) * range + offset,currentLevel * 2);
            }
            // создаёт обычных врагов с опред уровня
            if(currentLevel > 7){
                if(genNForL(i * 300,currentLevel) > 0.2f){
                    // add an enemy
                    createEnemy(enemyTex,genNForL(i * 100,currentLevel) * range + offset,currentLevel * 2 + 1);
                }
            }
            //создает летающих врагов начиная с определенного уровня, чтобы избежать моментальных смертей
            if(currentLevel > 0){
                if(genNForL(i * 400,currentLevel) > 0.3f){
                    // add a cloud enemy
                    createSeeker(genNForL(i * 100,currentLevel) * range + offset,currentLevel * 2 + 1);
                }
            }
        }
    }
    public Entity createBouncyPlatform(float x, float y){
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        // 1 pixel = width of body * 16
        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 1f, 1f, BodyFactory.STONE, BodyType.StaticBody);
        //make it a sensor so not to impede movement
        bodyFactory.makeAllFixturesSensors(b2dbody.body);
        stateCom.set(StateComponent.STATE_NORMAL);

        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        Animation anim = new Animation(0.1f,atlas.findRegions("tile"));
        anim.setPlayMode(Animation.PlayMode.LOOP);
        animCom.animations.put(StateComponent.STATE_NORMAL, anim);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = platformTex;

        TransformComponent trans = engine.createComponent(TransformComponent.class);
        trans.position.set(x, y, 0);
        entity.add(trans);

        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SPRING;

        b2dbody.body.setUserData(entity);
        entity.add(b2dbody);
        entity.add(texture);
        entity.add(animCom);
        entity.add(stateCom);
        entity.add(type);
        engine.addEntity(entity);

        return entity;
    }

    public void createPlatform(float x, float y){
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 3f, 0.3f, BodyFactory.STONE, BodyType.StaticBody);
        b2dbody.body.setUserData(entity);
        entity.add(b2dbody);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = platformTex;
        entity.add(texture);

        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY;
        entity.add(type);

        TransformComponent trans = engine.createComponent(TransformComponent.class);
        trans.position.set(x, y, 0);
        entity.add(trans);

        engine.addEntity(entity);

    }

    public void createBackground(){
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);



        position.position.set(46,32,-10);
        texture.region = atlas.findRegion("neon_background");
        type.type = TypeComponent.SCENERY;
        b2dbody.body = bodyFactory.makeBoxPolyBody(screenWidth/PPM/2, screenHeight/PPM/2, screenWidth, screenHeight, BodyFactory.STONE, BodyType.StaticBody);

        bodyFactory.makeAllFixturesSensors(b2dbody.body);


        entity.add(texture);
        entity.add(position);
        entity.add(type);
        entity.add(b2dbody);

        b2dbody.body.setUserData(entity);

        engine.addEntity(entity);
    }


    public Entity createEnemy(TextureRegion tex, float x, float y){
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);

        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 2f, 4f, BodyFactory.STONE, BodyType.KinematicBody,true);
        //b2dbody.body = bodyFactory.makeCirclePolyBody(x,y,1, BodyFactory.STONE, BodyType.KinematicBody,true);
        position.position.set(x,y,0);
        texture.region = tex;
        enemy.xPosCenter = x;
        type.type = TypeComponent.ENEMY;
        b2dbody.body.setUserData(entity);

        entity.add(colComp);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(enemy);
        entity.add(type);

        engine.addEntity(entity);

        return entity;
    }
    public Entity createSeeker(float x, float y) {
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
        SteeringComponent scom = engine.createComponent(SteeringComponent.class);

        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 2f, 4f, BodyFactory.STONE, BodyType.DynamicBody,true);
        b2dbody.body.setGravityScale(0f);  // no gravity for our floating enemy
        b2dbody.body.setLinearDamping(0.3f); // setting linear dampening so the enemy slows down in our box2d world(or it can float on forever)

        position.position.set(x,y,0);
        texture.region = seekerTex;
        type.type = TypeComponent.ENEMY;
        stateCom.set(StateComponent.STATE_NORMAL);
        b2dbody.body.setUserData(entity);
        bodyFactory.makeAllFixturesSensors(b2dbody.body); // seeker  should fly about not fall
        scom.body = b2dbody.body;
        enemy.enemyType = EnemyComponent.Type.CLOUD;

        // set out steering behaviour
        scom.steeringBehavior  = SteeringPresets.getWander(scom);
        scom.currentMode = SteeringComponent.SteeringState.WANDER;

        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(colComp);
        entity.add(type);
        entity.add(enemy);
        entity.add(stateCom);
        entity.add(scom);

        engine.addEntity(entity);
        return entity;

    }
    public Entity createPlayer(OrthographicCamera cam){

        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);

        SteeringComponent scom = engine.createComponent(SteeringComponent.class);


        player.cam = cam;
        b2dbody.body = bodyFactory.makeBoxPolyBody(10,1,1.875f, 5.687f, BodyFactory.STONE, BodyType.DynamicBody,true);
        b2dbody.body.setSleepingAllowed(false); // don't allow unit to sleep or it wil sleep through
        // set object position (x,y,z) z used to define draw order 0 first drawn

        Animation anim = new Animation(0.2f,atlas.findRegions("Nick_walk"));
        Animation flippedAnim = new Animation(0.2f,atlas.findRegions("Nick_walk_left"));

        anim.setPlayMode(Animation.PlayMode.LOOP);
        animCom.animations.put(StateComponent.STATE_NORMAL, new Animation(0, atlas.findRegion("Nick_front")));
        animCom.animations.put(StateComponent.STATE_MOVING_RIGHT, anim);
        animCom.animations.put(StateComponent.STATE_MOVING_LEFT, flippedAnim);
        animCom.animations.put(StateComponent.STATE_JUMPING, new Animation(2f, atlas.findRegions("Nick_jump")));
        animCom.animations.put(StateComponent.STATE_FALLING, new Animation(0f, atlas.findRegions("Nick_falling")));
        animCom.animations.put(StateComponent.STATE_HIT, anim);


        position.position.set(10,1,0);
        texture.region = atlas.findRegion("Nick_front");
        texture.offsetY = 0.1f;
        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_NORMAL);
        b2dbody.body.setUserData(entity);

        scom.body = b2dbody.body;

        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(animCom);
        entity.add(player);
        entity.add(colComp);
        entity.add(type);
        entity.add(stateCom);
        entity .add(scom);

        engine.addEntity(entity);
        this.player = entity;
        return entity;
    }

    public void createWalls(){

        for(int i = 0; i < 2; i++){
            System.out.println("Making wall "+i);
            Entity entity = engine.createEntity();
            B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
            TransformComponent position = engine.createComponent(TransformComponent.class);
            TextureComponent texture = engine.createComponent(TextureComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);
            WallComponent wallComp = engine.createComponent(WallComponent.class);

            //make wall
            b2dbody.body = b2dbody.body = bodyFactory.makeBoxPolyBody(0+(i*(screenWidth/PPM)),30,1,60, BodyFactory.STONE, BodyType.KinematicBody,true);
            position.position.set(0+(i*40), 30, 0);
            texture.region = wallTex;
            type.type = TypeComponent.SCENERY;

            entity.add(b2dbody);
            entity.add(position);
            entity.add(texture);
            entity.add(type);
            entity.add(wallComp);
            b2dbody.body.setUserData(entity);

            engine.addEntity(entity);
        }
    }

    public void createFloor(){
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);

        position.position.set(20,0,0);
        texture.region = floorTex;
        texture.offsetY = -0.4f;
        type.type = TypeComponent.SCENERY;
        b2dbody.body = bodyFactory.makeBoxPolyBody(screenWidth/PPM/2, -16, screenWidth, 32, BodyFactory.STONE, BodyType.StaticBody);

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(position);
        entity.add(type);

        b2dbody.body.setUserData(entity);

        engine.addEntity(entity);
    }
    //Creates the water entity that steadily moves upwards towards player
    public Entity createWaterFloor(){
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        WaterFloorComponent waterFloor = engine.createComponent(WaterFloorComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);


        Animation anim = new Animation(0.1f,atlas.findRegions("water"));
		anim.setPlayMode(Animation.PlayMode.LOOP);
		animCom.animations.put(1, anim);

        type.type = TypeComponent.ENEMY;
        //waterTex.setRegionWidth(640);
        waterTex.setRegionHeight(704);
        texture.region = waterTex;
        texture.offsetY = 1;
        b2dbody.body = bodyFactory.makeBoxPolyBody(screenWidth/PPM/2,-40,screenWidth,44, BodyFactory.STONE, BodyType.KinematicBody,true);
        position.position.set(20,-15,0);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(type);
        entity.add(waterFloor);
        entity.add(animCom);

        b2dbody.body.setUserData(entity);

        engine.addEntity(entity);

        //makeParticleEffect(ParticleEffectManager.WATER, b2dbody,-15,22);
        return entity;
    }

    public Entity createBullet(float x, float y, float xVel, float yVel, BulletComponent.Owner own){
        System.out.println("Making bullet"+x+":"+y+":"+xVel+":"+yVel);
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        BulletComponent bul = engine.createComponent(BulletComponent.class);

        bul.owner = own;

        b2dbody.body = bodyFactory.makeCirclePolyBody(x,y,0.5f, BodyFactory.STONE, BodyType.DynamicBody,true);
        b2dbody.body.setBullet(true); // increase physics computation to limit body travelling through other objects
        bodyFactory.makeAllFixturesSensors(b2dbody.body); // make bullets sensors so they don't move player
        position.position.set(x,y,0);
        texture.region = bulletTex;
        Animation anim = new Animation(0f,atlas.findRegions("bullet"));
        anim.setPlayMode(Animation.PlayMode.LOOP);
        animCom.animations.put(0, anim);

        type.type = TypeComponent.BULLET;
        b2dbody.body.setUserData(entity);
        bul.xVel = xVel;
        bul.yVel = yVel;

        //attach party to bullet
        //bul.particleEffect = makeParticleEffect(ParticleEffectManager.FIRE,b2dbody);

        entity.add(bul);
        entity.add(colComp);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(animCom);
        entity.add(stateCom);
        entity.add(type);

        engine.addEntity(entity);

        return entity;
    }

    /**
     * Make particle effect at xy
     * @param x
     * @param y
     * @return the Particle Effect Entity
     */
    public Entity makeParticleEffect(int type, float x, float y){
        Entity entPE = engine.createEntity();
        ParticleEffectComponent pec = engine.createComponent(ParticleEffectComponent.class);
        pec.particleEffect = pem.getPooledParticleEffect(type);
        pec.particleEffect.setPosition(x, y);
        entPE.add(pec);
        engine.addEntity(entPE);
        return entPE;
    }

    /** Attache particle effect to body from body component
     * @param type the type of particle effect to show
     * @param b2dbody the bodycomponent with the body to attach to
     * @return the Particle Effect Entity
     */
    public Entity makeParticleEffect(int type, B2dBodyComponent b2dbody){
        return makeParticleEffect(type,b2dbody,0,0);
    }

    /**
     * Attache particle effect to body from body component with offsets
     * @param type the type of particle effect to show
     * @param b2dbody the bodycomponent with the body to attach to
     * @param xo x offset
     * @param yo y offset
     * @return the Particle Effect Entity
     */
    public Entity makeParticleEffect(int type, B2dBodyComponent b2dbody, float xo, float yo){
        Entity entPE = engine.createEntity();
        ParticleEffectComponent pec = engine.createComponent(ParticleEffectComponent.class);
        pec.particleEffect = pem.getPooledParticleEffect(type);
        pec.particleEffect.setPosition(b2dbody.body.getPosition().x, b2dbody.body.getPosition().y);
        pec.particleEffect.getEmitters().first().setAttached(true); //manually attach for testing
        pec.xOffset = xo;
        pec.yOffset = yo;
        pec.isattached = true;
        pec.particleEffect.getEmitters().first().setContinuous(true);
        pec.attachedBody = b2dbody.body;
        entPE.add(pec);
        engine.addEntity(entPE);
        return entPE;
    }

    public void removeEntity(Entity ent){
        engine.removeEntity(ent);
    }

    public void resetWorld() {
        currentLevel = 0;
        openSim = new OpenSimplexNoise(MathUtils.random(2000L));
        Array<Body> bods = new Array<Body>();
        world.getBodies(bods);
        for(Body bod:bods){
            world.destroyBody(bod);
        }
    }
}
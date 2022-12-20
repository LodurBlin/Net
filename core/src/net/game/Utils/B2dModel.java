package net.game.Utils;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import net.game.Controllers.Keyboard;


public class B2dModel {
    public World world;
    private OrthographicCamera camera;
    public boolean isSwimming = false;
    public Body player;
    private Keyboard controller;

    private Sound shocked, hurt;
    public static final int SHOCKED_SOUND = 0; // new
    public static final int HURT_SOUND = 1; //new

    public B2dModel(Keyboard cont, OrthographicCamera cam, B2dAssetManager assMan){
        camera = cam;
        controller = cont;
        world = new World(new Vector2(0,-10f), true);
        world.setContactListener(new B2dContactListener());
        createFloor();
        // get our body factory singleton and store it in bodyFactory
        BodyFactory bodyFactory = BodyFactory.getInstance(world);
        player = bodyFactory.makeBoxPolyBody(1, 1, 2, 2, BodyFactory.RUBBER, BodyType.DynamicBody,false);        Body water =  bodyFactory.makeBoxPolyBody(1, -8, 40, 12, BodyFactory.RUBBER, BodyType.StaticBody,false);
        water.setUserData("IAMTHESEA");
        // make the water a sensor, so it doesn't obstruct (препятствовать) our player
        bodyFactory.makeAllFixturesSensors(water);

        assMan.queueAddSounds();
// tells the asset manager to load the images and wait until finsihed loading.
        assMan.manager.finishLoading();
// loads the 2 sounds we use

        shocked = assMan.manager.get(assMan.shockedSound);
        hurt = assMan.manager.get(assMan.hurtSound);

    }

    private void createFloor() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(0, -10);

        Body bodyd = world.createBody(bodyDef);
        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50, 1);
        // create the physical object in our body)
        // without this our body would just be data in the world
        bodyd.createFixture(shape, 0.0f);
        shape.dispose();
    }
    public boolean pointIntersectsBody(Body body, Vector2 mouseLocation){
        Vector3 mousePos = new Vector3(mouseLocation,0); //convert mouseLocation to 3D position
        camera.unproject(mousePos); // convert from screen position to world position
        return body.getFixtureList().first().testPoint(mousePos.x, mousePos.y);

    }

    private void createObject(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(0,0);
        Body bodys = world.createBody(bodyDef);
        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1);
        // set the properties of the object ( shape, weight, restitution(bounciness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        bodys.createFixture(shape, 0.0f);
        shape.dispose();
    }

    private void createMovingObject(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;
        bodyDef.position.set(0,-12);
        Body bodyk = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        bodyk.createFixture(shape, 0.0f);
        shape.dispose();

        bodyk.setLinearVelocity(0, 0.75f);
    }
    public void playSound(int sound){
        switch(sound){
            case SHOCKED_SOUND:
                shocked.play();
                break;
            case HURT_SOUND:
                hurt.play();
                break;
        }
    }

    // our game logic here
    public void logicStep(float delta){

        if(controller.left){
            player.applyForceToCenter(-5, 0,true);
        }else if(controller.right){
            player.applyForceToCenter(5, 0,true);
        }else if(controller.up){
            player.applyForceToCenter(0, 20,true);
        }else if(controller.down){
            player.applyForceToCenter(0, -20,true);
        }

        if(isSwimming){
            player.applyForceToCenter(0, 60, true);
        }
        if(controller.isMouse1Down && pointIntersectsBody(player,controller.mousePos)){
            System.out.println("Player was clicked");
        }

        world.step(delta , 3, 3);
    }
}

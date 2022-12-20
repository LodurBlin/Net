package net.game.Utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

//The BodyFactory is a singleton, so we can load it and create bodies in whatever part of our code we
// like which should make creating enemies and obstacles in the game later on a lot easier.

public class BodyFactory {
    public static final int STEEL = 0;
    public static final int WOOD = 1;
    public static final int RUBBER = 2;
    public static final int STONE = 3;
    private static final double DEGTORAD = 0.0174533f; //например

    private World world;
    private static BodyFactory thisInstance;

    private BodyFactory(World world) {
        this.world = world;
        //Body ourNewBodyObject = makeCirclePolyBody(5, 5, 2, BodyFactory.RUBBER, BodyDef.BodyType.DynamicBody,false);
    }

    public static BodyFactory getInstance(World world) {

        if (thisInstance == null) {
            thisInstance = new BodyFactory(world);
        } else {
            thisInstance.world = world;
        }
        return thisInstance;
    }

    /*
    Sensors behave as if their maskBits is set to zero - they never collide with anything.
    But they do generate BeginContact/EndContact callbacks to let us know when they start or stop overlapping
    another fixture.
    All other features of sensor fixtures remain the same as for a normal fixture.
    They can be added to any type of body. They still have a mass and will affect the overall mass
    of the body they are attached to. Remember you can have more than one fixture on a body, so you
    can have a mix of solid shapes and sensors, allowing for all kinds of neat things. Here are a few ideas:
detect entities entering or leaving a certain area
switches to trigger an event
detect ground under player character
a field of vision for an entity
     */
    public void makeAllFixturesSensors(Body bod) {
        for (Fixture fix : bod.getFixtureList()) {
            fix.setSensor(true);
        }
    }

    //overloading
    public Body makeCirclePolyBody(float posx, float posy, float radius, int material) {
        return makeCirclePolyBody(posx, posy, radius, material, BodyType.DynamicBody, false);
    }

    public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyType bodyType) {
        return makeCirclePolyBody(posx, posy, radius, material, bodyType, false);
    }

    public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyType bodyType, boolean fixedRotation) {
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius / 2);
        boxBody.createFixture(makeFixture(material, circleShape));
        circleShape.dispose();
        return boxBody;
    }

    /////

    public Body makeBoxPolyBody(float posx, float posy, float width, float height, int material, BodyType bodyType) {
        return makeBoxPolyBody(posx, posy, width, height, material, bodyType, false);
    }

    public Body makeBoxPolyBody(float posx, float posy, float width, float height, int material, BodyType bodyType, boolean fixedRotation) {
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width / 2, height / 2);
        boxBody.createFixture(makeFixture(material, poly));
        poly.dispose();

        return boxBody;
    }
    ///////

    public Body makePolygonShapeBody(Vector2[] vertices, float posx, float posy, int material, BodyType bodyType) {
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        Body boxBody = world.createBody(boxBodyDef);

        PolygonShape polygon = new PolygonShape();
        polygon.set(vertices); //Строим по x, y вершин
        boxBody.createFixture(makeFixture(material, polygon));
        polygon.dispose();

        return boxBody;
    }

    //// Для создания области видимости врагов

    public void makeConeSensor(Body body, float size) {

        FixtureDef fixtureDef = new FixtureDef();
        //fixtureDef.isSensor = true; // will add in future

        PolygonShape polygon = new PolygonShape();

        float radius = size;
        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0, 0);
        for (int i = 2; i < 6; i++) {
            float angle = (float) (i / 6.0 * 145 * DEGTORAD); // convert degrees to radians
            vertices[i - 1] = new Vector2(radius * ((float) Math.cos(angle)), radius * ((float) Math.sin(angle)));
        }
        polygon.set(vertices);
        fixtureDef.shape = polygon;
        body.createFixture(fixtureDef);
        polygon.dispose();
    }

    static public FixtureDef makeFixture(int material, Shape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        switch (material) {
            case STEEL:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.3f;
                fixtureDef.restitution = 0.1f;
                break;
            case WOOD:
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.7f;
                fixtureDef.restitution = 0.3f;
                break;
            case RUBBER:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0f;
                fixtureDef.restitution = 1f;
                break;
            case STONE:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.9f;
                fixtureDef.restitution = 0.01f;
            default:
                fixtureDef.density = 7f;
                fixtureDef.friction = 0.5f;
                fixtureDef.restitution = 0.3f;
        }
        return fixtureDef;
    }
}
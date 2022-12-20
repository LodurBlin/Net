package net.game.Entity.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import net.game.Controllers.Keyboard;
import net.game.Entity.Components.B2dBodyComponent;
import net.game.Entity.Components.PlayerComponent;
import net.game.Entity.Components.StateComponent;

public class PlayerControlSystem extends IteratingSystem{

    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<B2dBodyComponent> bodm;
    ComponentMapper<StateComponent> sm;
    Keyboard controller;


    @SuppressWarnings("unchecked")
    public PlayerControlSystem(Keyboard keyCon) {
        super(Family.all(PlayerComponent.class).get());
        controller = keyCon;
        pm = ComponentMapper.getFor(PlayerComponent.class);
        bodm = ComponentMapper.getFor(B2dBodyComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2dBodyComponent b2body = bodm.get(entity);
        StateComponent state = sm.get(entity);
        PlayerComponent player = pm.get(entity);

        player.cam.position.y = b2body.body.getPosition().y;


        if(b2body.body.getLinearVelocity().y > 0 && state.get() != StateComponent.STATE_FALLING){
            state.set(StateComponent.STATE_FALLING);
            System.out.println("setting to Falling");
        }

        if(b2body.body.getLinearVelocity().y == 0){
            if(state.get() == StateComponent.STATE_FALLING){
                state.set(StateComponent.STATE_NORMAL);
                System.out.println("setting to normal");
            }
            if(b2body.body.getLinearVelocity().x != 0 && state.get() != StateComponent.STATE_MOVING){
                state.set(StateComponent.STATE_MOVING);
                System.out.println("setting to moving");
            }
        }

        // make player teleport higher
        if(player.onSpring){
            //b2body.body.applyLinearImpulse(0, 175f, b2body.body.getWorldCenter().x,b2body.body.getWorldCenter().y, true);

            //lvlFactory.makeParticleEffect(ParticleEffectManager.SMOKE, b2body.body.getPosition().x, b2body.body.getPosition().y);

            // move player
            b2body.body.setTransform(b2body.body.getPosition().x, b2body.body.getPosition().y+ 10, b2body.body.getAngle());
            //state.set(StateComponent.STATE_JUMPING);
            player.onSpring = false;
        }


        if(controller.left){
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, -7f, 0.2f),b2body.body.getLinearVelocity().y);
        }
        if(controller.right){
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, 7f, 0.2f),b2body.body.getLinearVelocity().y);
        }

        if(!controller.left && ! controller.right){
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, 0, 0.1f),b2body.body.getLinearVelocity().y);
        }

        if(controller.up &&
                (state.get() == StateComponent.STATE_NORMAL || state.get() == StateComponent.STATE_MOVING)){
            b2body.body.applyLinearImpulse(0, 12f * b2body.body.getMass() , b2body.body.getWorldCenter().x,b2body.body.getWorldCenter().y, true);
            state.set(StateComponent.STATE_JUMPING);
            player.onPlatform = false;
            player.onSpring = false;
            System.out.println("setting to jumping");
        }

        if(controller.down){
            b2body.body.applyLinearImpulse(0, -5f, b2body.body.getWorldCenter().x,b2body.body.getWorldCenter().y, true);
        }
        /*
        if(player.timeSinceLastShot > 0){
            player.timeSinceLastShot -= deltaTime;
        }



        if(controller.isMouse1Down){ // if mouse button is pressed
            //System.out.println(player.timeSinceLastShot+" ls:sd "+player.shootDelay);
            // user wants to fire
            if(player.timeSinceLastShot <=0){ // check the player hasn't just shot
                //player can shoot so do player shoot
                Vector3 mousePos = new Vector3(controller.mouseLocation.x,controller.mouseLocation.y,0); // get mouse position
                player.cam.unproject(mousePos); // convert position from screen to box2d world position

                Vector2 aim = DFUtils.aimTo(b2body.body.getPosition(), mousePos);
                aim.scl(7);
                // create a bullet
                lvlFactory.createBullet(b2body.body.getPosition().x,
                        b2body.body.getPosition().y,
                        aim.x,
                        aim.y,
                        BulletComponent.Owner.PLAYER);
                //reset timeSinceLastShot
                player.timeSinceLastShot = player.shootDelay;
            }
        }

         */
    }
}

package net.game.Entity.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.game.Entity.Components.AnimationComponent;
import net.game.Entity.Components.StateComponent;
import net.game.Entity.Components.TextureComponent;

public class AnimationSystem extends IteratingSystem {

    ComponentMapper<TextureComponent> tm;
    ComponentMapper<AnimationComponent> am;
    ComponentMapper<StateComponent> sm;

    @SuppressWarnings("unchecked")
    public AnimationSystem(){
        super(Family.all(TextureComponent.class,
                AnimationComponent.class,
                StateComponent.class).get());

        tm = ComponentMapper.getFor(TextureComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        AnimationComponent ani = am.get(entity);
        StateComponent state = sm.get(entity);

        if(ani.animations.containsKey(state.get())){
            TextureComponent tex = tm.get(entity);
            tex.region = (TextureRegion) ani.animations.get(state.get()).getKeyFrame(state.time, state.isLooping);
        }

        state.time += deltaTime;
    }
    /*
    public void flipAnimation(Entity entity, float deltaTime){
        AnimationComponent ani = am.get(entity);
        StateComponent state = sm.get(entity);
        if(ani.animations.containsKey(state.get())){
            TextureComponent tex = tm.get(entity);
            TextureRegion r = (TextureRegion) ani.animations.get(state.get()).getKeyFrame(state.time, state.isLooping);
            r.flip(true, false);
            tex.region = r;
        }
    }

     */
}


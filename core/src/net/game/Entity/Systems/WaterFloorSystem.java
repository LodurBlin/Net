package net.game.Entity.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import net.game.Entity.Components.B2dBodyComponent;
import net.game.Entity.Components.WaterFloorComponent;
import net.game.Utils.LevelFactory;

public class WaterFloorSystem extends IteratingSystem {
    private LevelFactory lvlFactory;
    private ComponentMapper<B2dBodyComponent> bm = ComponentMapper.getFor(B2dBodyComponent.class);

    @SuppressWarnings("unchecked")
    public WaterFloorSystem(LevelFactory lvlFactory) {
        super(Family.all(WaterFloorComponent.class).get());
        this.lvlFactory =lvlFactory;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get current y level of player entity
        float currentyLevel = lvlFactory.player.getComponent(B2dBodyComponent.class).body.getPosition().y;
        // get the body component of the wall we're updating
        Body bod = bm.get(entity).body;

        float speed = (currentyLevel / 1000);

        speed = Math.min(speed, 0.05f);

        // make sure water doesn't get too far behind
        if(bod.getPosition().y < currentyLevel - 50){
            bod.setTransform(bod.getPosition().x, currentyLevel - 50, bod.getAngle());
        }


        bod.setTransform(bod.getPosition().x, bod.getPosition().y+speed, bod.getAngle());
    }
}

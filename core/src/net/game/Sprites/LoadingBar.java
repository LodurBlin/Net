package net.game.Sprites;


import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LoadingBar extends Actor {
    private Animation flameAnimation;
    private TextureRegion currentFrame;
    private float stateTime;

    public LoadingBar(Animation an) {
        flameAnimation = an;
        this.setWidth(100);
        this.setHeight(25);
        this.setVisible(false);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.draw(currentFrame, getX() - 5, getY(), 40, 40);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta; // Accumulate elapsed animation time
        currentFrame = (TextureRegion) flameAnimation.getKeyFrame(stateTime, true);
    }
}


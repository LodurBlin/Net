package net.game.Utils;

import com.badlogic.gdx.Gdx;

public class Constants {
    public static final float PPM = 32.0f;
    public static final float SPPM = 16.0f; //SmallPPM
    public static final float PIXELS_TO_METRES = 1.0f / PPM; // get the ratio for converting pixels to metres
    public static float screenHeight = Gdx.graphics.getHeight();
    public static float screenWidth = Gdx.graphics.getWidth();
    public enum Screens{
        MENU,
        PREFERENCES,
        MAIN,
        ENDGAME
    }

}

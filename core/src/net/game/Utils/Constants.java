package net.game.Utils;

public class Constants {
    public static final float PPM = 32.0f;
    public static final float PIXELS_TO_METRES = 1.0f / PPM; // get the ratio for converting pixels to metres
    public enum Screens{
        MENU,
        PREFERENCES,
        MAIN,
        ENDGAME
    }
}

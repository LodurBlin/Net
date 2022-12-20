package net.game;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		Graphics.DisplayMode desktop = Lwjgl3ApplicationConfiguration.getDisplayMode();
		config.setFullscreenMode(desktop);
		config.setTitle("Cool Game");
		new Lwjgl3Application(new CoolGame(), config);

	}
}

package objectOriented;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.roadfury.game.RoadFuryGame;

import ObjectOriented.RoadFuryGameOO;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncherOO {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(600, 1000);
		config.setForegroundFPS(60);
		config.setTitle("RoadFury");

		new Lwjgl3Application(new RoadFuryGameOO(), config);
	}
}



package bouncingBall;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import ObjectOriented.RoadFuryGameOO;

public class BouncingBallLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(600, 1000);
        config.setForegroundFPS(60);
        config.setTitle("BouncingBall");

        new Lwjgl3Application(new BouncingBall(), config);
    }
}

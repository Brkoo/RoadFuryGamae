package DimenzijeSveta;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import DimenzieSveta.RoadFuryDimenzijeSveta;
import bouncingBall.BouncingBall;

public class DimenzijeSvetaLauncher {

    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(600, 1000);
        config.setForegroundFPS(60);
        config.setTitle("Dimenzije sveta");

        new Lwjgl3Application(new RoadFuryDimenzijeSveta(), config);
    }
}

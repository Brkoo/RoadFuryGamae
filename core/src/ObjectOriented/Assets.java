package ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import org.w3c.dom.Text;

public class Assets {

    public static Texture mainBackground;
    public static Texture obstacleCarImage;
    public static Texture ammoImage;
    public static Texture mainCarImage;
    public static Texture gunImage;
    public static Texture bulletImage;
    public static Texture mainCarImagePower;
    public static Sound destruction;
    public static BitmapFont font;
    public static Texture powerUpImage;

    public static void load(){
        mainBackground = new Texture("road.png");
        mainCarImage = new Texture("mainCar.png");
        ammoImage = new Texture("ammo.png");
        gunImage = new Texture("gun.png");
        bulletImage = new Texture ("bullet.png");
        obstacleCarImage = new Texture("obstacleCar.png");
        mainCarImagePower = new Texture("mainCarPower.png");
        destruction  = Gdx.audio.newSound(Gdx.files.internal("destruction.mp3"));
        powerUpImage = new Texture("powerUpImage.png");
        font = new BitmapFont();
        font.getData().setScale(5);
    }

    public static void dispose(){
        mainCarImage.dispose();
        obstacleCarImage.dispose();
        bulletImage.dispose();
        ammoImage.dispose();
        font.dispose();
        destruction.dispose();
        powerUpImage.dispose();
        //batch.dispose();
        mainBackground.dispose();
    }


}


package ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class PowerUp extends DynamicGameObject{
    public static long createNextInTime = Constants.CREATE_POWER_UP;
    public static int powerUpTime;
    public static long lastPowerUp;
    public static float powerUpPickTime = 0;
    public static float powerUpDuration = 5;
    static MainCar mainCar;

    public PowerUp(float x, float y, float width, float height) {

        super(x, y, width, height);
    }

    public PowerUp(MainCar car){
        super(0,Gdx.graphics.getHeight(), Assets.powerUpImage.getWidth(), Assets.powerUpImage.getHeight());
        mainCar = car;

    }

    public static boolean isTimeToCreateNew(){
        return TimeUtils.nanoTime() - lastPowerUp > createNextInTime * 10;

    }
    @Override
    public void update(float deltaTime){
       position.y -=  Constants.SPEED_OBSTACLE_CAR * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void setCreateNextInTime(long time){
        lastPowerUp = time;
    }

    @Override
    public void generateRandomPosition(float height) {

        //position.x = height + this.bounds.height;
        position.x = MathUtils.random(0, Gdx.graphics.getWidth() - Constants.BARRIER);
        this.updateBounds();
    }
    public static void setInvicibility(float time){
        powerUpPickTime = time;
        mainCar.invincibility = true;
    }
    public static void checkTimeInvincibility(float time){
        System.out.println("Power Power");
        System.out.println("\n + power up time : " + powerUpPickTime );
        System.out.println("\n time : " + time);
        if(time > powerUpPickTime + powerUpDuration && mainCar.invincibility){
            System.out.println("Car is out of power up");
            mainCar.invincibility = false;
        }
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(Assets.powerUpImage, position.x, position.y, bounds.width, bounds.height);
    }


}

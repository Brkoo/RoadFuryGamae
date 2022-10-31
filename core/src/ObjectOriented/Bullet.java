package ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;

public class Bullet extends DynamicGameObject{
    public static long createNextInTime = Constants.CREATE_BULLET_TIME;
    public static long lastBulletTime;
    public Bullet(MainCar maincar) {
        super(maincar.position.x + maincar.bounds.width / 2f, maincar.position.y + maincar.bounds.height / 2f, Assets.bulletImage.getWidth(), Assets.bulletImage.getHeight());
        
    }
    /* //Tega ne rabim saj je to bullet
    public static boolean isTimeToCreateNew(){
        return TimeUtils.nanoTime() > createNextInTime;
    }

     */
    @Override
    void update(float deltaTime){
        position.y += Constants.BULLET_SPEED * Gdx.graphics.getDeltaTime();
    }

    public void setCreateNextInTime(long time) {
        lastBulletTime = time;
    }
    public static boolean isTimeToCreateNew(){
        return TimeUtils.nanoTime() - lastBulletTime > createNextInTime;
    }
    @Override
   public void render(SpriteBatch batch){
        batch.draw(Assets.bulletImage, position.x,position.y, bounds.width, bounds.height);

    }
}

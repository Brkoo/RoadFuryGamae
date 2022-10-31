package ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Ammo extends DynamicGameObject{

    public static long createNextInTimeAmmo = Constants.CREATE_AMMO_PACK_TIME * 8;
    public static long lastAmmoPack;

    public Ammo(float x, float y, float width, float height) {
        super(x, y, Assets.ammoImage.getWidth(), Assets.ammoImage.getHeight());
    }
   @Override
    public void update(float deltaTime){
        position.y -= Constants.SPEED_OBSTACLE_CAR * Gdx.graphics.getDeltaTime();

    }

    public static boolean isTimeToCreateNew(){

        return TimeUtils.nanoTime() - lastAmmoPack > createNextInTimeAmmo;
    }

    public void setCreateNextInTimeAmmo(long time){
        lastAmmoPack = time;
    }

    public void generateRandomPosition(float width, float height){
        position.x = MathUtils.random(Constants.BARRIER + Assets.ammoImage.getWidth() / 2f, Gdx.graphics.getWidth() - Constants.BARRIER - Assets.ammoImage.getWidth() /2f);
        position.y = height;

    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(Assets.ammoImage, position.x, position.y, bounds.width, bounds.height);

    }
}

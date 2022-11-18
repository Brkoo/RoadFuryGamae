package ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;

public class Bullet extends DynamicGameObject implements Pool.Poolable {
    public static long createNextInTime = Constants.CREATE_BULLET_TIME;
    public static long lastBulletTime;
    public ParticleEffect bulletTrail;
    public MainCar mainCar;
    public Bullet(MainCar maincar) {
        super(maincar.position.x + maincar.bounds.width / 2f, maincar.position.y + maincar.bounds.height / 2f, Assets.bulletImage.getWidth(), Assets.bulletImage.getHeight());
        this.mainCar = maincar;
        bulletTrail = new ParticleEffect();
        bulletTrail.load(Gdx.files.internal("Particle Park Smoke.p"), Gdx.files.internal(""));
    }

    /* //Tega ne rabim saj je to bullet
    public static boolean isTimeToCreateNew(){
        return TimeUtils.nanoTime() > createNextInTime;
    }


     */
    public Bullet(){
        super(0,0,Assets.bulletImage.getWidth(),Assets.bulletImage.getHeight());

    }
    @Override
    void update(float deltaTime){
        position.y += Constants.BULLET_SPEED * Gdx.graphics.getDeltaTime();
        bulletTrail.setPosition(this.position.x, this.position.y);
        //bulletTrail.update(deltaTime);
        //bulletTrail.update(deltaTime);

    }
    @Override
    public void setCreateNextInTime(long time) {
        lastBulletTime = time;
    }
    public static boolean isTimeToCreateNew(){
        return TimeUtils.nanoTime() - lastBulletTime > createNextInTime;
    }
    @Override
   public void render(SpriteBatch batch){
        batch.draw(Assets.bulletImage, position.x,position.y, bounds.width, bounds.height);


        bulletTrail.draw(batch, Gdx.graphics.getDeltaTime());


    }


    @Override
    public void reset() {
        this.position.set(0,0);

        System.out.println("This bullet is reset");
    }
}

package ObjectOriented;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.SystemTray;
import java.sql.Time;
import java.util.Iterator;

import jdk.tools.jmod.Main;

public class RoadFuryGameOO extends ApplicationAdapter {


    private SpriteBatch batch;
    private OrthographicCamera camera;
    private MainCar mainCar;
    private Array<DynamicGameObject> dynamicActors;
    private Array<DynamicGameObject> bullets; //special LibGDX Array
    private final Array<DynamicGameObject> activeBullets = new Array<DynamicGameObject>();
    private BulletPool bp;
    private GameObjectScore gameObjectScore;
    private GameObjectEnd gameObjectEnd;
    float width, height;


    @Override
    public void create() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        Assets.load();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        //Gdx.app.setLogLevel(Logger.DEBUG);
        gameObjectScore = new GameObjectScore(Gdx.graphics.getWidth() - 50, height - 20, 20, 10);
        gameObjectEnd = new GameObjectEnd(width / 2f - 200, height / 2f, 20, 10);
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();

        // create a Rectangle to logically represents the rocket
        mainCar = new MainCar(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Assets.mainCarImage.getWidth(), Assets.mainCarImage.getHeight());
        bp = new BulletPool(mainCar);
        dynamicActors = new Array<DynamicGameObject>();
        bullets = new Array<DynamicGameObject>();
        //add first astronout and asteroid
        spawnObstacleCar();
        spawnAmmo();

    }

    private void spawnObstacleCar() {
        ObstacleCar obstacleCar = new ObstacleCar(0, height, Assets.obstacleCarImage.getWidth(), Assets.obstacleCarImage.getHeight());
        obstacleCar.generateRandomPosition(width, height);
        dynamicActors.add(obstacleCar);
        obstacleCar.setCreateNextInTime(TimeUtils.nanoTime());
    }

    private void spawnAmmo() {
        Ammo ammo = new Ammo(0, height, Assets.ammoImage.getWidth(), Assets.ammoImage.getHeight());
        ammo.generateRandomPosition(width, height);
        dynamicActors.add(ammo);
        ammo.setCreateNextInTimeAmmo(TimeUtils.nanoTime());
    }


    private void spawnBullet() {
        DynamicGameObject bullet = bp.obtain();
        bullet.position.set(mainCar.position.x + mainCar.bounds.width / 2f, mainCar.position.y + mainCar.bounds.height / 2f);
        // Bullet bullet = new Bullet(mainCar);
        // bullets.add(bullet);
        activeBullets.add(bullet);
        bullet.setCreateNextInTime(TimeUtils.nanoTime());
        //bullet.update(TimeUtils.nanoTime());
        // System.out.println(bullet.getFree());
        System.out.println(bullet.bounds.y + "To je bounds x \n");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // tell the camera to update its matrices.
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        if (gameObjectScore.mainCarHealth > 0) {
            for (DynamicGameObject act : dynamicActors) {
                act.update(Gdx.graphics.getDeltaTime());
                act.updateBounds(Gdx.graphics.getDeltaTime());
            }
            /*
            for (DynamicGameObject act : bullets) {
                act.update(Gdx.graphics.getDeltaTime());
                act.updateBounds(Gdx.graphics.getDeltaTime());
            }
            */
            for (DynamicGameObject bullet : activeBullets) {
                bullet.update(Gdx.graphics.getDeltaTime());
                bullet.updateBounds(Gdx.graphics.getDeltaTime());
            }
            if (ObstacleCar.isTimeToCreateNew()) spawnObstacleCar();

            if (Ammo.isTimeToCreateNew()) spawnAmmo();

            gameObjectScore.time += Gdx.graphics.getDeltaTime();
            if (gameObjectScore.time > Constants.PERIOD) {
                gameObjectScore.time -= Constants.PERIOD;
                gameObjectScore.setScore();
            }


            for (Iterator<DynamicGameObject> iter = dynamicActors.iterator(); iter.hasNext(); ) {
                DynamicGameObject act = iter.next();
                if (act.position.y + act.bounds.height < 0) iter.remove();
                mainCar.update(Gdx.graphics.getDeltaTime());

                //System.out.println(act.bounds.x);
                //System.out.println(act.position.x + " y = " + act.position.y + "\n");
                //System.out.println(mainCar.bounds.height);
                for (DynamicGameObject bullet : activeBullets) {
                    //bullet.update(Gdx.graphics.getDeltaTime());
                    if (bullet.position.y > height) {
                        bp.free(bullet);
                        activeBullets.removeValue(bullet, true);
                    }
                    if (act instanceof ObstacleCar && bullet.bounds.overlaps(act.bounds)) {
                        iter.remove();
                        bp.free(bullet);
                        activeBullets.removeValue(bullet, true);
                    }
                }
                /*
                for (Iterator<DynamicGameObject> i = bullets.iterator(); i.hasNext(); ) {
                    DynamicGameObject bullet = i.next();

                    if (act instanceof ObstacleCar && bullet.bounds.overlaps(act.bounds)) {
                        iter.remove();
                        i.remove();

                    }
                }
                */
                if (act.bounds.overlaps(mainCar.bounds)) {
                    iter.remove();

                    if (act instanceof ObstacleCar) {
                        gameObjectScore.mainCarHealth = -1;

                    }
                    if (act instanceof Ammo) {
                        gameObjectScore.AmmoPlus();

                    }

                    //act.updateScore(gameObjectScore);
                }

            }
            //if(Gdx.input.isTouched()) commandTouched(); //mouse or touch screen
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) mainCar.commandMoveLeft();
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) mainCar.commandMoveUp();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) mainCar.commandMoveRight();
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) mainCar.commandMoveDown();
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && Bullet.isTimeToCreateNew() && gameObjectScore.ammo > 0) {
                spawnBullet();
                gameObjectScore.ammo -= 1;

            }

            batch.begin();
            {
                batch.draw(Assets.mainBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                mainCar.render(batch);
                gameObjectScore.render(batch);

                for (DynamicGameObject act : dynamicActors) {
                    act.render(batch);
                }
                for (DynamicGameObject bullet : activeBullets) {
                    bullet.render(batch);
            /*
            for (DynamicGameObject act : bullets) {
                act.render(batch);
            }

             */


                    gameObjectScore.render(batch);

                    // gameObjectScore.render(batch);


                }


                batch.end();
            }
        } else {
            batch.begin();
            gameObjectEnd.render(batch);
            gameObjectScore.render(batch);
            batch.end();
        }
    }


}











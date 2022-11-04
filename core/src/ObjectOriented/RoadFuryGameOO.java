package ObjectOriented;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class RoadFuryGameOO extends ApplicationAdapter {


    private SpriteBatch batch;
    private OrthographicCamera camera;
    private MainCar mainCar;
    private Array<DynamicGameObject> dynamicActors;
    //private Array<DynamicGameObject> bullets; //special LibGDX Array
    private final Array<DynamicGameObject> activeBullets = new Array<DynamicGameObject>();
    private final Array<DynamicGameObject> activeObstacleCars = new Array<DynamicGameObject>();
    private ObstacleCarPool ocp;
    private BulletPool bp;
    private GameObjectScore gameObjectScore;
    private GameObjectEnd gameObjectEnd;
    //private PowerUp powerUp;
    private State state;
    float width, height;
    private Vector2 mainCarStartingPosition;


    @Override
    public void create() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        Assets.load();
       mainCarStartingPosition = new Vector2( width/ 2f, height / 2f);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        //Gdx.app.setLogLevel(Logger.DEBUG);

        gameObjectScore = new GameObjectScore(Gdx.graphics.getWidth() - 50, height - 20, 20, 10);
        gameObjectEnd = new GameObjectEnd(width / 2f - 200, height / 2f, 20, 10);
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();

        state = new State();
        // create a Rectangle to logically represents the rocket
        mainCar = new MainCar(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Assets.mainCarImage.getWidth(), Assets.mainCarImage.getHeight());
      //  powerUp = new PowerUp(mainCar);
        bp = new BulletPool(mainCar);


        dynamicActors = new Array<DynamicGameObject>();
        ocp = new ObstacleCarPool(height, 50);

       // bullets = new Array<DynamicGameObject>();
        //add first astronout and asteroid
        spawnPowerUp();
        spawnObstacleCar();
        spawnAmmo();

    }
    private void spawnPowerUp(){
        PowerUp powerUp = new PowerUp(mainCar);
        powerUp.generateRandomPosition(height);
        dynamicActors.add(powerUp);
        powerUp.setCreateNextInTime(TimeUtils.nanoTime());
    }
    private void spawnObstacleCar() {
        //ObstacleCar obstacleCar = new ObstacleCar(0, height, Assets.obstacleCarImage.getWidth(), Assets.obstacleCarImage.getHeight());
        //obstacleCar.generateRandomPosition(height);
        //dynamicActors.add(obstacleCar);
        //obstacleCar.setCreateNextInTime(TimeUtils.nanoTime());
        DynamicGameObject obstacleCar = ocp.obtain();
        obstacleCar.generateRandomPosition(height);
        activeObstacleCars.add(obstacleCar);
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


        switch (state.getState()){
            case 1:
                PowerUp.checkTimeInvincibility(gameObjectScore.score);
                if (gameObjectScore.mainCarHealth > 0) {
                    for (DynamicGameObject act : dynamicActors) {
                        act.update(Gdx.graphics.getDeltaTime());
                        act.updateBounds();
                    }
                    for(DynamicGameObject obstacleCar : activeObstacleCars){
                        obstacleCar.update(Gdx.graphics.getDeltaTime());
                        obstacleCar.updateBounds();
                    }


            /*
            for (DynamicGameObject act : bullets) {
                act.update(Gdx.graphics.getDeltaTime());
                act.updateBounds(Gdx.graphics.getDeltaTime());
            }
            */
                    for (DynamicGameObject bullet : activeBullets) {
                        bullet.update(Gdx.graphics.getDeltaTime());
                        bullet.updateBounds();
                    }
                    if (ObstacleCar.isTimeToCreateNew()) spawnObstacleCar();
                    if (PowerUp.isTimeToCreateNew()) spawnPowerUp();

                    if (Ammo.isTimeToCreateNew()) spawnAmmo();

                    gameObjectScore.time += Gdx.graphics.getDeltaTime();
                    if (gameObjectScore.time > Constants.PERIOD) {
                        gameObjectScore.time -= Constants.PERIOD;
                        gameObjectScore.setScore();
                    }


                    for (Iterator<DynamicGameObject> iter = activeObstacleCars.iterator(); iter.hasNext(); ) {
                        DynamicGameObject act = iter.next();
                        if (act.position.y + act.bounds.height + 20 < 0){


                            ocp.free(act);
                            activeObstacleCars.removeValue(act, true);
                            iter.remove();

                        }
                        mainCar.update(Gdx.graphics.getDeltaTime());

                        for(DynamicGameObject dynamicObjects: dynamicActors){ //tukaj se ostane ammo
                            if (dynamicObjects.bounds.overlaps(mainCar.bounds)) {
                                dynamicActors.removeValue(dynamicObjects,true);

                                if(dynamicObjects instanceof PowerUp){
                                    PowerUp.setInvicibility(gameObjectScore.score);
                                }else {
                                    gameObjectScore.AmmoPlus();
                                }
                            }
                        }
                        //System.out.println(act.bounds.x);
                        //System.out.println(act.position.x + " y = " + act.position.y + "\n");
                        //System.out.println(mainCar.bounds.height);
                        for (DynamicGameObject bullet : activeBullets) {
                            //bullet.update(Gdx.graphics.getDeltaTime());
                            if (bullet.position.y > height) {
                                bp.free(bullet);
                                activeBullets.removeValue(bullet, true);
                            }
                            if (bullet.bounds.overlaps(act.bounds)) {
                                iter.remove();
                                bp.free(bullet);
                                ocp.free(act);
                                activeObstacleCars.removeValue(act, true);
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
                            if(!mainCar.invincibility){
                                ocp.free(act);
                                activeObstacleCars.removeValue(act, true);
                                gameObjectScore.mainCarHealth = -1;
                            }
                            else{
                                ocp.free(act);
                                activeObstacleCars.removeValue(act, true);
                            }


                            //act.updateScore(gameObjectScore);
                        }

                    }
                    //if(Gdx.input.isTouched()) commandTouched(); //mouse or touch screen
                    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) mainCar.commandMoveLeft();
                    if (Gdx.input.isKeyPressed(Input.Keys.UP)) mainCar.commandMoveUp();
                    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) mainCar.commandMoveRight();
                    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) mainCar.commandMoveDown();
                    Gdx.input.setInputProcessor(new InputAdapter(){
                        @Override public boolean keyUp (int keycode) {
                            if (keycode == Input.Keys.P)
                                state.setState(2);
                            return true;
                        }
                    });
                    //if(Gdx.input.isKeyJustPressed(Input.Keys.P)) state.setState(2);
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
                            gameObjectScore.render(batch);
                        }
                        for(DynamicGameObject obstacleCar: activeObstacleCars){
                            obstacleCar.render(batch);
                        }
                        batch.end();
                    }
                } else {
                    state.setState(3);
                }
                break;
            case 2:
                batch.begin();
                state.render(batch);
                Gdx.input.setInputProcessor(new InputAdapter() {
                                                @Override
                                                public boolean keyUp(int keycode) {
                                                    if (keycode == Input.Keys.P)
                                                        state.setState(1);
                                                    return true;
                                                }
                                            });
                batch.end();
                break;
            case 3:
                batch.begin();

                gameObjectEnd.render(batch);
                gameObjectScore.render(batch);
                activeObstacleCars.clear();
                dynamicActors.clear();
                if(Gdx.input.isKeyPressed(Input.Keys.R)) {
                    spawnPowerUp();
                    spawnAmmo();
                    spawnObstacleCar();
                    gameObjectScore.score = 0;
                    mainCar.position.set(mainCarStartingPosition);
                    gameObjectScore.ammo = 0;
                    gameObjectScore.mainCarHealth = 1;

                   // Gdx.app.getApplicationListener().resume();
                    state.setState(1);

                }
                if(Gdx.input.isKeyPressed(Input.Keys.Q)){
                    dispose();
                }
                batch.end();
                break;
            default:
                break;
        }


    }
    @Override
    public void dispose () {
        Assets.dispose();
        mainCar.dispose();
        gameObjectEnd.dispose();
        gameObjectScore.dispose();
        batch.dispose();


    }


}











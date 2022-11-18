package ObjectOriented;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import util.ViewportUtils;
import util.debug.DebugCameraController;
import util.debug.MemoryInfo;

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
    private Vector2 mainCarStartingPosition;
    public static float PPM = 16;
    private State state;
    int width, height;
    //DEBUG
    private DebugCameraController debugCameraController;
    private MemoryInfo memoryInfo;
    private boolean debug = false;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private ParticleEffect particleEffectExplosion;
    private ParticleEffect particleEffectBulletTrail;
    private ParticleEffectPool bigExplosionPool;
    private ParticleEffectPool bulletTrailPool;
    Array<ParticleEffectPool.PooledEffect> effects_explosion = new Array();
    Array<ParticleEffectPool.PooledEffect> effects_bullet_trail = new Array();



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
        //v create, show ali konstruktorju

        //DEBUG
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        memoryInfo = new MemoryInfo(500);

        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        //Particle effects
        particleEffectExplosion = new ParticleEffect();
        particleEffectExplosion.load(Gdx.files.internal("Particle Park Explosion.p"), Gdx.files.internal(""));
        //pe.getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        //pe.setEmittersCleanUpBlendFunction(false);
        bigExplosionPool = new ParticleEffectPool(particleEffectExplosion, 0, 20);

        particleEffectBulletTrail = new ParticleEffect();
        particleEffectBulletTrail.load(Gdx.files.internal("Particle Park Smoke.p"), Gdx.files.internal(""));

      // bulletTrailPool = new ParticleEffectPool(particleEffectBulletTrail, 0, 20);
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
        //ParticleEffectPool.PooledEffect effect = bulletTrailPool.obtain();

        //effects_bullet_trail.add(effect);
        //bullets.add(bullet);
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
        //batch.setProjectionMatrix(camera.combined);


        switch (state.getState()){
            case 1:


                if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) debug = !debug;

                if (debug) {
                    debugCameraController.handleDebugInput(Gdx.graphics.getDeltaTime());
                    memoryInfo.update();
                }


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
                        if (act.position.y + act.bounds.height < 0){


                            ocp.free(act);
                            //activeObstacleCars.removeValue(act, true);
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
                                ParticleEffectPool.PooledEffect effect = bigExplosionPool.obtain();
                                effect.setPosition(act.position.x + Assets.obstacleCarImage.getWidth() / 2f, act.position.y + Assets.obstacleCarImage.getHeight() / 2f);
                                effects_explosion.add(effect);
                                iter.remove();

                                bp.free(bullet);
                                ocp.free(act);
                                activeObstacleCars.removeValue(act, true);
                                activeBullets.removeValue(bullet, true);
                                //particleEffectBulletTrail.allowCompletion();
                               // particleEffectBulletTrail.dispose();

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

                } else {
                    state.setState(3);
                }

                     camera.update();

                    // tell the SpriteBatch to render in the
                    // coordinate system specified by the camera
                    batch.setProjectionMatrix(camera.combined);
                    batch.begin();
                    {


                        batch.draw(Assets.mainBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                        mainCar.render(batch);
                        gameObjectScore.render(batch);

                        for (DynamicGameObject act : dynamicActors) {
                            act.render(batch);
                        }
                        for (DynamicGameObject bullet : activeBullets) {

                            /*
                            for(int j = effects_bullet_trail.size - 1; j >= 0; j--){

                                ParticleEffectPool.PooledEffect effect = effects_bullet_trail.get(j);
                                effect.setPosition(bullet.position.x, bullet.position.y);
                                effect.draw(batch, Gdx.graphics.getDeltaTime());
                                if(effect.isComplete()){
                                    effect.free();
                                    effects_bullet_trail.removeIndex(j);
                                }
                            }

                             */




                            bullet.render(batch);
                            gameObjectScore.render(batch);
                        }
                        for(DynamicGameObject obstacleCar: activeObstacleCars){
                            obstacleCar.render(batch);
                        }
                        //batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        for(int i = effects_explosion.size - 1; i>= 0; i--){
                            ParticleEffectPool.PooledEffect effect = effects_explosion.get(i);
                            effect.draw(batch, Gdx.graphics.getDeltaTime());
                            if(effect.isComplete()){
                                effect.free();
                                effects_explosion.removeIndex(i);
                            }
                        }

                        batch.end();


                    }

                    if (debug) {
                        debugCameraController.applyTo(camera);
                        batch.begin();
                        {
                            // the average number of frames per second
                            GlyphLayout layout = new GlyphLayout(Assets.font, "FPS:" + Gdx.graphics.getFramesPerSecond());
                            Assets.font.setColor(Color.YELLOW);
                            Assets.font.draw(batch, layout, Gdx.graphics.getWidth() - layout.width, Gdx.graphics.getHeight() - 50);

                            // number of rendering calls, ever; will not be reset unless set manually
                            Assets.font.setColor(Color.YELLOW);
                            Assets.font.draw(batch, "RC:" + batch.totalRenderCalls, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 20);

                            memoryInfo.render(batch, Assets.font);
                        }
                        batch.end();
                        batch.totalRenderCalls = 0;
                        ViewportUtils.drawGrid(viewport, shapeRenderer, 50);

                        // print rectangles
                        shapeRenderer.setProjectionMatrix(camera.combined);
                        // https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/glutils/ShapeRenderer.html
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                        {
                            shapeRenderer.setColor(1, 1, 0, 1);
                            for (DynamicGameObject act : dynamicActors) {
                                shapeRenderer.rect(act.position.x, act.position.y, act.bounds.getWidth(), act.bounds.getHeight());
                            }

                            for (DynamicGameObject bullet : activeBullets) {
                                shapeRenderer.rect(bullet.position.x, bullet.position.y, bullet.bounds.getWidth(), bullet.bounds.getHeight());
                            }


                            for (DynamicGameObject obstacleCar: activeObstacleCars) {
                                shapeRenderer.rect(obstacleCar.position.x, obstacleCar.position.y, obstacleCar.bounds.getWidth(), obstacleCar.bounds.getHeight());
                            }
                            shapeRenderer.rect(mainCar.position.x, mainCar.position.y, mainCar.bounds.getWidth(), mainCar.bounds.getHeight());
                        }
                        shapeRenderer.end();
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











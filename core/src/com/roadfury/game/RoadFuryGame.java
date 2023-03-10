package com.roadfury.game;

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
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;
import java.util.Iterator;

public class RoadFuryGame extends ApplicationAdapter {
    SpriteBatch batch;
    Rectangle mainCarRectangle;
    Rectangle bulletRectangle;


    //Texture img;
    Texture mainBackground;
    OrthographicCamera camera;
    Texture obstacleCarImage;

    Texture ammoImage;
    Texture mainCarImage;
    Texture gunImage;
    Texture bulletImage;
    private Array<Rectangle> obstacleCarRectangles;
    private Array<Rectangle> ammoRectangles;
    private Array<Rectangle> bulletRectangles;
    private long lastObstacleCarTime;
    private long lastAmmoTime;
    private long lastBulletTime;
    private long lastFuelTime;
    private int mainCarHealth;
    private int roadNumber = 1;
    private int score;
    private float time = 0f;
    private int ammoCount;
    private boolean gameOver = false;
    private float textWidth;
    private Sound destruction;




    public BitmapFont font;

    /*
    | 1 | 2 | 3 | 4 |
    |   |   |   |   |
    |   |   |   |   |
    |   |   |   |   |
     */
    private static final float PERIOD = 1f;
    private static final int BARRIER = 40; //40 px from sides to barrier in main background
    private static final int ROAD1 = 80;
    private static final int ROAD2 = 80 + 115 + 5;
    private static final int ROAD3 = 80 + 230 + 15;
    private static final int ROAD4 = 80 + 345 + 15;

    private static final int SPEED = 400;    // pixels per second
    private static final int BULLET_SPEED = 350;
    private static final int SPEED_MAIN_CAR = 200; // pixels per second
    private static  float SPEED_OBSTACLE_CAR = 200;    // pixels per second
    private static long CREATE_OBSTACLE_CAR_TIME = 1000000000;
    private static final long CREATE_AMMO_PACK_TIME = 1000000000;// ns
    private static final long CREATE_BULLET_TIME = 400000000;

    private static final String GAME_OVER_TEXT = "Game Over"; // Text Game Over shown when you die


    @Override
    public void create () {


        //img = new Texture("road.png");
        ammoCount = 10;
        mainCarHealth = 1;
        font = new BitmapFont();
        font.getData().setScale(5);

        score = 0;

        mainBackground = new Texture("road.png");
        mainCarImage = new Texture("mainCar.png");
        ammoImage = new Texture("ammo.png");
        gunImage = new Texture("gun.png");
        bulletImage = new Texture ("bullet.png");

        obstacleCarImage = new Texture("obstacleCar.png");
        destruction  = Gdx.audio.newSound(Gdx.files.internal("destruction.mp3"));
        batch = new SpriteBatch();
        //Gdx.graphics.setWindowedMode(img.getWidth(), img.getHeight());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch = new SpriteBatch();

        mainCarRectangle = new Rectangle();
        mainCarRectangle.x = (int) (Gdx.graphics.getWidth() / 2f - mainCarImage.getWidth() / 2f);
        mainCarRectangle.y = 50;
        mainCarRectangle.height = mainCarImage.getHeight();
        mainCarRectangle.width = mainCarImage.getWidth();

        //ARRAYS of obstacles and ammo
        obstacleCarRectangles = new Array<Rectangle>();
        ammoRectangles = new Array<Rectangle>();
        bulletRectangles = new Array<Rectangle>();

        GlyphLayout layout = new GlyphLayout(font, GAME_OVER_TEXT);
        textWidth = layout.width;
        spawnObstacleCar();

        spawnAmmo();


    }

    @Override
    public void render () {





        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if(TimeUtils.nanoTime() - lastObstacleCarTime > CREATE_OBSTACLE_CAR_TIME) spawnObstacleCar();
        if (TimeUtils.nanoTime() - lastAmmoTime > CREATE_AMMO_PACK_TIME * 8) spawnAmmo();


        //batch.draw(mainCarImage,mainCarRectangle.x,mainCarRectangle.y, mainCarRectangle.width, mainCarRectangle.height);
        if(mainCarHealth > 0) {
        time += Gdx.graphics.getDeltaTime();
        if(time > PERIOD) {
            time -= PERIOD;
            addScore();
        }
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
           if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) commandMoveLeft();
           if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) commandMoveRight();
           if (Gdx.input.isKeyPressed(Input.Keys.UP)) commandMoveUp();


            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) commandMoveDown();

                if (TimeUtils.nanoTime() - lastBulletTime > CREATE_BULLET_TIME && Gdx.input.isKeyPressed(Input.Keys.SPACE) ) {
                    spawnBullet();
                    commandFireBullet();

                }


            if(score != 0) {
                if (score % 5 == 0) {
                    SPEED_OBSTACLE_CAR += 0.3;
                    //if(CREATE_OBSTACLE_CAR_TIME > 900000000)
                    if(score % 10 == 0) {
                        CREATE_OBSTACLE_CAR_TIME -= 1000000;
                    }
                }
            }
            for (Iterator<Rectangle> it = obstacleCarRectangles.iterator(); it.hasNext(); ) {
                Rectangle obstacleCar = it.next();
                obstacleCar.y -= SPEED_OBSTACLE_CAR * Gdx.graphics.getDeltaTime();

                if (obstacleCar.y + obstacleCarImage.getHeight() < 0) it.remove();

                if (obstacleCar.overlaps(mainCarRectangle)) {
                    mainCarHealth--;
                    it.remove();
                }
                for (Iterator<Rectangle> it_bullet = bulletRectangles.iterator(); it_bullet.hasNext();) {
                    Rectangle bullet = it_bullet.next();
                    if (bullet.overlaps(obstacleCar)) {
                        destruction.play();
                        it.remove();
                        it_bullet.remove();

                    }
                }


            }
            for (Iterator<Rectangle> it = ammoRectangles.iterator(); it.hasNext();){
                Rectangle ammo = it.next();
                ammo.y -= SPEED_OBSTACLE_CAR * Gdx.graphics.getDeltaTime();
                if(ammo.y + ammoImage.getHeight() < 0){
                    it.remove();

                }
                if(ammo.overlaps(mainCarRectangle)){
                    ammoCount = 10;
                    it.remove();
                }
            }

            for (Iterator<Rectangle> it = bulletRectangles.iterator(); it.hasNext();){
                Rectangle bullet = it.next();
                bullet.y += BULLET_SPEED * Gdx.graphics.getDeltaTime();


                }





        }

        //font.draw(batch, "" + Gdx.graphics.getHeight(), Gdx.graphics.getWidth()/ 2f, Gdx.graphics.getHeight() / 2f);
        //font.draw(batch, "" + Gdx.graphics.getWidth(), Gdx.graphics.getWidth()/ 2f, Gdx.graphics.getHeight() / 2f);
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        {    //
            //batch.draw(mainBackground, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

            batch.draw(mainBackground, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

            batch.draw(mainCarImage,mainCarRectangle.x,mainCarRectangle.y, mainCarRectangle.width, mainCarRectangle.height);
            batch.draw(gunImage, mainCarRectangle.x - 15, mainCarRectangle.y + 20);




           // batch.draw(bulletImage, bulletRectangle.x, bulletRectangle.y, bulletImage.getWidth(), bulletImage.getHeight());
            for (Rectangle obstacleCar : obstacleCarRectangles) {
                batch.draw(obstacleCarImage, obstacleCar.x, obstacleCar.y);

                for(Rectangle ammo : ammoRectangles) {
                    if (!ammo.overlaps(obstacleCar)) {
                        batch.draw(ammoImage, ammo.x, ammo.y);

                    }
                }

            }


            for(Rectangle bullet : bulletRectangles){
                batch.draw(bulletImage, bullet.x, bullet.y);
            }











            /*
for (Rectangle astronaut : astronauts) {
batch.draw(bearImage, astronaut.x, astronaut.y);
}

*/
            if(mainCarHealth <= 0){
                font.setColor(Color.RED);
                font.getData().setScale(5);
                font.draw(batch, "" +GAME_OVER_TEXT, mainBackground.getWidth() / 2f  - (textWidth / 2f)  , mainBackground.getHeight() /2f );
            }

            //font.draw(batch, "Game Over" + mainCarHealth, mainBackground.getWidth() / 2f, mainBackground.getHeight() /2f );
            font.setColor(Color.YELLOW);
            font.draw(batch, "" + score, Gdx.graphics.getWidth() - 100 , Gdx.graphics.getHeight() - 20);
            font.setColor(Color.GREEN);
            font.draw(batch, "" + ammoCount, 20, Gdx.graphics.getHeight() - 20);

        }



        batch.end();

    }



    @Override
    public void dispose () {
        mainCarImage.dispose();
        obstacleCarImage.dispose();
        bulletImage.dispose();
        ammoImage.dispose();
        font.dispose();
        destruction.dispose();
        batch.dispose();
        mainBackground.dispose();

    }
    private void addScore() {
        score += 1;
    }

    //MOVEMENT
    //MOVE RIGHT




    private void commandMoveRight(){
        mainCarRectangle.x += SPEED * Gdx.graphics.getDeltaTime();
        if(mainCarRectangle.x > Gdx.graphics.getWidth() - BARRIER - mainCarImage.getWidth())
        {
            mainCarRectangle.x = Gdx.graphics.getWidth() - BARRIER - mainCarImage.getWidth();
        }
    }
    //MOVE LEFT
    private void commandMoveLeft(){
        mainCarRectangle.x -= SPEED * Gdx.graphics.getDeltaTime();
        if(mainCarRectangle.x < BARRIER){
            mainCarRectangle.x = BARRIER;
        }

    }
    //MOVE UP
    private void commandMoveUp(){
        mainCarRectangle.y += SPEED * Gdx.graphics.getDeltaTime();
        if(mainCarRectangle.y > Gdx.graphics.getHeight() - mainCarRectangle.getHeight()){
            mainCarRectangle.y = (int) (Gdx.graphics.getHeight() - mainCarRectangle.getHeight());
        }
    }

    //MOVE DOWN
    private void commandMoveDown(){
        mainCarRectangle.y -= SPEED * Gdx.graphics.getDeltaTime();
        if(mainCarRectangle.y < 0){
            mainCarRectangle.y = 0;
        }
    }


    private void commandFireBullet(){
        bulletRectangle.y += BULLET_SPEED * Gdx.graphics.getDeltaTime();

    }

    //SPAWNING
    //spawning obstacle cars
    private void spawnObstacleCar(){

        Rectangle obstacleCar = new Rectangle();

        int newRoadNumber = 1;
        while(newRoadNumber == roadNumber){
            newRoadNumber = MathUtils.random(1,4);
        }
        roadNumber = newRoadNumber;


        if(roadNumber == 1){
            obstacleCar.x = ROAD1;
        }else if(roadNumber == 2){
            obstacleCar.x = ROAD2;
        }
        else if(roadNumber == 3){
            obstacleCar.x = ROAD3;
        }
        else{
            obstacleCar.x = ROAD4;
        }




        obstacleCar.y = MathUtils.random(mainBackground.getHeight(), mainBackground.getHeight() + 100);
        obstacleCar.width = obstacleCarImage.getWidth();
        obstacleCar.height = obstacleCarImage.getHeight();
        obstacleCarRectangles.add(obstacleCar);
        lastObstacleCarTime = TimeUtils.nanoTime();

    }
    private void spawnAmmo(){
        Rectangle ammoRectangle = new Rectangle();
        ammoRectangle.x = MathUtils.random(BARRIER + ammoImage.getWidth() / 2f, Gdx.graphics.getWidth() - BARRIER - ammoImage.getWidth() /2f);
        ammoRectangle.y = Gdx.graphics.getHeight();
        ammoRectangle.width = ammoImage.getWidth();
        ammoRectangle.height = ammoImage.getHeight();
        ammoRectangles.add(ammoRectangle);
        lastAmmoTime = TimeUtils.nanoTime();

    }
    private void spawnBullet(){
        bulletRectangle = new Rectangle();
        bulletRectangle.x = mainCarRectangle.x + mainCarImage.getWidth() / 2f;
        bulletRectangle.y = mainCarRectangle.y + mainCarImage.getHeight() / 2f + 20;
        bulletRectangle.width = bulletImage.getWidth();
        bulletRectangle.height = bulletImage.getHeight();

        bulletRectangles.add(bulletRectangle);
        lastBulletTime = TimeUtils.nanoTime();
        ammoCount--;
    }
 

}




package ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import javax.crypto.Cipher;

public class  ObstacleCar extends DynamicGameObject implements Pool.Poolable{
    public static long createNextInTime = Constants.CREATE_OBSTACLE_CAR_TIME;
    public static long lastObstacleCar;
    public static int roadNumber = 1;
    public float y;


    public ObstacleCar(float x, float y, float width, float height) {


        super(x, y, Assets.obstacleCarImage.getWidth(), Assets.obstacleCarImage.getHeight());

    }
    public ObstacleCar(float y){

        super(0,y, Assets.obstacleCarImage.getWidth(),Assets.obstacleCarImage.getHeight());
        this.y = y;//generateRandomPosition();
    }
    public static boolean isTimeToCreateNew(){
        return TimeUtils.nanoTime() - lastObstacleCar  > createNextInTime;
    }
    @Override
    public void update(float deltaTime){
        position.y -= Constants.SPEED_OBSTACLE_CAR * deltaTime;
       // bounds.set(position.x, position.y, bounds.getWidth(), bounds.getHeight());

    }
    @Override
    public void setCreateNextInTime(long time){

        lastObstacleCar = time;
    }
    @Override
    public void generateRandomPosition(float height){
        position.y = height + this.bounds.height;
        //ObstacleCar obstacleCar = new ObstacleCar(0,height,Assets.obstacleCarImage.getWidth(), Assets.obstacleCarImage.getHeight());
        int newRoadNumber = 1;
        while(newRoadNumber == roadNumber){
            newRoadNumber = MathUtils.random(1,4);
        }
        roadNumber = newRoadNumber;


        if(roadNumber == 1){
            position.x = Constants.ROAD1;

        }else if(roadNumber == 2){
            position.x = Constants.ROAD2;
        }
        else if(roadNumber == 3){
            position.x = Constants.ROAD3;
        }
        else{
            position.x = Constants.ROAD4;
        }

    }
    @Override
    public void render(SpriteBatch batch){
        batch.draw(Assets.obstacleCarImage, position.x, position.y, bounds.width, bounds.height);

    }

    @Override
    public void reset() {
        generateRandomPosition(y);
        this.position.set(position.x, position.y);
        System.out.println("This car is reset");
    }
}

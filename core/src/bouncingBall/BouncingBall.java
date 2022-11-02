package bouncingBall;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import ObjectOriented.Assets;

public class  BouncingBall  extends ApplicationAdapter {

    private SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Ball ball;
    private Rectangle rectangle;
    @Override
    public void create () {
        Assets.load();
        rectangle = new Rectangle(0, 0, 5000, 200);
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();



        batch.setProjectionMatrix(camera.combined);
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            Vector3 input = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
            camera.unproject(input);
            spawnCircle(input);

        }

        if(ball != null){

            if(ball.position.y - ball.bounds.radius <= rectangle.height){
            //if(Intersector.overlaps(ball.bounds, rectangle)){
                ball.rebound();
                //ball.update(Gdx.graphics.getDeltaTime());
            }
            else{

                    ball.update(Gdx.graphics.getDeltaTime());

            }
            /*
            if(ball.position.y < 0){
                ball.position.y = 0;
            }
            /*

             /*
            if(ball.velocity.y <= 0){
                ball.position.y = 0;
            }

             */
        }


        batch.begin();
        if(ball != null){
            ball.render(batch);
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        //shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(0, 0, 5000, 200);
        shapeRenderer.end();
        batch.end();
    }



    @Override
    public void dispose () {

    }
    public void spawnCircle(Vector3 input){
        ball = new Ball(input.x, input.y, 20);

        //ball.update(TimeUtils.nanoTime());
    }
}





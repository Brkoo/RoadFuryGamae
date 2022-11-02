package bouncingBall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ObjectOriented.Assets;
import ObjectOriented.Constants;

public class Ball extends GameObject{
    ShapeRenderer shape = new ShapeRenderer();

    float gravity = 0.9f;
    float bounceReduce = 0.8f;
    float weight = 10f;
    //float maxVelocity = 50f;


    public Ball(float x, float y, float radius) {
        super(x, y, radius);
     velocity.y = 0.0f;

    }

    public void update(float deltaTime){

            velocity.y = velocity.y + gravity;
            position.y = position.y - velocity.y;

    }
    public void rebound(){
            if(velocity.y > 0) {
                velocity.y = -velocity.y * bounceReduce;
              // position.y -= velocity.y;
            }
            position.y = position.y - velocity.y;

    }
    public void render(SpriteBatch batch){
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLUE);
         //shape.setColor(0, 1, 0, 1);
        shape.circle(position.x, position.y, bounds.radius); // fills a rect
        shape.end();
        //batch.draw(Assets.ammoImage, position.x, position.y);
    }
}

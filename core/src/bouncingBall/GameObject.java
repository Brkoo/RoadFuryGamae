package bouncingBall;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {

    public final Circle bounds;
    public final Vector2 position;
    public final Vector2 velocity;
    public final Vector2 accel;

    public GameObject(float x, float y, float radius)
    {

        this.position = new Vector2(x, y);
        this.bounds = new Circle(position.x, position.y,radius);
        this.velocity = new Vector2();
        this.accel = new Vector2();

    }

}

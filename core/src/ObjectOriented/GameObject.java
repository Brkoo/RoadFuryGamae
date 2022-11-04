package ObjectOriented;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
    public final Rectangle bounds;
    public final Vector2 position;
    public int state = 1;

    public GameObject(float x, float y, float width, float height){
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle(position.x, position.y, width, height);
        //this.bounds = new Rectangle(width, y - height / 2, width, height);
    }
    public GameObject(){
       this.position = new Vector2();
       this.bounds = new Rectangle();

    }
    public void setState(int state){
        this.state = state;
    }
    public void render(SpriteBatch batch) {

    }
    public void dispose(){
        this.dispose();
    }


}

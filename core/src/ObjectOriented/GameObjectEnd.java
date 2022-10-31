package ObjectOriented;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameObjectEnd extends GameObject {
    public GameObjectEnd(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
    @Override
    public void render(SpriteBatch batch){
        Assets.font.setColor(Color.RED);

        Assets.font.draw(batch, "GAME OVER" ,bounds.x, bounds.y+bounds.height - 20);


    }
}

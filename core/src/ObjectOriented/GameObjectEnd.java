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
        //Assets.font.getData().setScale(4);
        Assets.font.setColor(Color.RED);

        Assets.font.draw(batch, "GAME OVER" ,bounds.x, bounds.y+bounds.height - 20);

        Assets.font.setColor(Color.YELLOW);
        Assets.font.draw(batch, "Press R \n to RESTART", position.x, position.y - 100);

    }
}

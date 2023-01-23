package ObjectOriented;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class State extends GameObject {


    public State(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    // state 1 = gae runing
    // state 2 = game paused
    // state 3 = game ended
    public State(){
        super(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0,0);
        state = 1;
    }
    public int getState(){
        return state;
    }
    public void setState(int state){
        this.state = state;
    }
    private void pause(SpriteBatch batch){
        state = 2;
        Assets.font.setColor(Color.RED);
        //Assets.font.getData().setScale(4);
        Assets.font.draw(batch, "PAUSE",position.x, position.y);


    }
    private void resume(SpriteBatch batch){
        state = 1;
        Gdx.app.getApplicationListener().resume();
    }
    @Override
    public void render(SpriteBatch batch){
        state = 2;
        Assets.font.setColor(Color.RED);
        //Assets.font.getData().setScale(4);
        Assets.font.draw(batch, "PAUSE",position.x, position.y);
    }

}

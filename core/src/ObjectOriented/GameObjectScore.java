package ObjectOriented;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameObjectScore extends GameObject{
    public int score;
    public float time = 0f;
    public int mainCarHealth = 1;
    public int ammo = 0;


    public GameObjectScore(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
    public int getMainCarHealth(){
        return mainCarHealth;
    }
    public int getScore(){
        return score;
    }
    public boolean isEnd(){
        return mainCarHealth <= 0;
    }
    public void setScore(){
        score += 1;
    }
    public int getAmmo(){
        return ammo;
    }
    public void AmmoPlus(){
        ammo += 5;
    }

    @Override
    public void render(SpriteBatch batch){
        Assets.font.setColor(Color.YELLOW);
        Assets.font.getData().setScale(2);
        Assets.font.draw(batch, "" + getScore(), position.x - 540, bounds.y+bounds.height - 20);
        Assets.font.setColor(Color.GREEN);
        Assets.font.draw(batch, "" + getAmmo(), bounds.x, bounds.y+bounds.height - 20);


    }
}

package ObjectOriented;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainCar extends DynamicGameObject{
    public boolean invincibility = false;

    public MainCar(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
    @Override
   public void update(float deltaTime){
        bounds.set(position.x, position.y, bounds.getWidth(), bounds.getHeight());
    }

     void commandMoveRight(){
         position.x += Constants.SPEED * Gdx.graphics.getDeltaTime();
        if(position.x > Gdx.graphics.getWidth() - Constants.BARRIER - Assets.mainCarImage.getWidth())
        {
            position.x = Gdx.graphics.getWidth() - Constants.BARRIER - Assets.mainCarImage.getWidth();
        }
    }
    void commandMoveLeft(){
        position.x -= Constants.SPEED * Gdx.graphics.getDeltaTime();
        if(position.x < Constants.BARRIER)
        {
            position.x = Constants.BARRIER;
        }
    }
    void commandMoveUp(){
        position.y += Constants.SPEED * Gdx.graphics.getDeltaTime();
        if(position.y > Gdx.graphics.getHeight() - bounds.getHeight() )
        {
            position.y = Gdx.graphics.getHeight() - bounds.getHeight();
        }
    }
    void commandMoveDown(){
        position.y -= Constants.SPEED * Gdx.graphics.getDeltaTime();
        if(position.y < 0)
        {
            position.y = 0;
        }
    }
    @Override
    public void render(SpriteBatch batch){
        if(invincibility){
            batch.draw(Assets.mainCarImagePower, position.x, position.y, bounds.width, bounds.height);
            batch.draw(Assets.gunImage,  position.x, position.y, bounds.width, bounds.height);
        }else{
        batch.draw(Assets.mainCarImage, position.x, position.y, bounds.width, bounds.height);
        batch.draw(Assets.gunImage,  position.x, position.y, bounds.width, bounds.height);
    }
    }
}

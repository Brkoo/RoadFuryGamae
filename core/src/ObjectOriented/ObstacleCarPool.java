package ObjectOriented;
import com.badlogic.gdx.utils.Pool;
public class ObstacleCarPool extends Pool<DynamicGameObject>{
    public float y;
    public ObstacleCarPool(float y, int max){
        super(50, 80);
       this.y = y;
    }
    @Override
    protected DynamicGameObject newObject() {
        return new ObstacleCar(y);
    }
}

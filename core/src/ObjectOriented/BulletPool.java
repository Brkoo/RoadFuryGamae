package ObjectOriented;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pool;
public class BulletPool  extends Pool<DynamicGameObject> {
    MainCar mainCar;

    public BulletPool(int init, int max){
        super(init, max);
    }
    public BulletPool(){
        super();
    }
    public BulletPool(MainCar mainCar){
        this.mainCar = mainCar;
    }
    @Override
    protected DynamicGameObject newObject() {
        return new Bullet(mainCar);
    }
}

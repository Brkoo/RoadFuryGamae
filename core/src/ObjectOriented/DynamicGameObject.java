package ObjectOriented;

public class DynamicGameObject extends GameObject{


    public DynamicGameObject(float x, float y, float width, float height) {
        super(x, y, width, height);


    }
    void update(float deltaTime){

    }
    void updateBounds(float deltaTime){
        bounds.set(position.x, position.y, bounds.width, bounds.height);
    }

    public void setCreateNextInTime(long nanoTime) {
    }
}

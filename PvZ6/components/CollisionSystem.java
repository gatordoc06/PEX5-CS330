package components;
import java.util.ArrayList;

public class CollisionSystem {
    ArrayList<CollisionShape> collisionShapes;

    public CollisionSystem() {
        collisionShapes = new ArrayList<>();
    }

    public void clear() {
        collisionShapes.clear();
    }

    public void addCollisionShape(CollisionShape shape) {
        if (shape != null) {
            collisionShapes.add(shape);
        }
    }

    public boolean checkCollisions(CollisionShape shape) {
        return getFirstCollision(shape) != null;
    }

    public CollisionShape getFirstCollision(CollisionShape shape) {
        if (shape == null) {
            return null;
        }
        for (CollisionShape other : collisionShapes) {
            if (shape != other && shape.collidesWith(other)) {
                return other;
            }
        }
        return null;
    }
}

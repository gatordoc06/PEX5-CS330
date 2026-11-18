package components;

//this time not lazy with just using ints


public class CollisionShape {
    public enum CollisionLayer {
        PLANT,
        ZOMBIE,
        BULLET
        }

    private float radius;
    private float posX;
    private float posY;

    // Collision layer this object belongs
    private CollisionLayer layerOn;

    // Layers this object can detect
    private CollisionLayer layerDetector;

    public CollisionShape(float radius, CollisionLayer layerOn, CollisionLayer layerDetector) {
        this.radius = radius;
        this.layerOn = layerOn;
        this.layerDetector = layerDetector;
    }

    public void setPosition(float x, float y) {
        this.posX = x;
        this.posY = y;
    }

    public boolean collidesWith(CollisionShape other) {

        // A collision is valid when either object can detect the other
        // based on the layer rules configured for that shape.
        if (this.layerDetector != other.layerOn && other.layerDetector != this.layerOn) {
            return false;
        }

        float dx = this.posX - other.posX;
        float dy = this.posY - other.posY;

        float distanceSquared = dx * dx + dy * dy;
        float radiusSum = this.radius + other.radius;

        return distanceSquared <= radiusSum * radiusSum;
    }
}
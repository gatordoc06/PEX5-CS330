package entities;
import components.CollisionShape;
import core.PvZ6;
import java.awt.image.BufferedImage;


public abstract class Entity{
    protected int xPos;
    protected int yPos;
    
    protected int size;

    protected BufferedImage image;
    
    //all zombies, plants, and projectiles need collision
    protected CollisionShape collisionShape;

    public void update(){}

    public void draw(){
        PvZ6.image(image, xPos, yPos, xPos+size, yPos-size);
    }

    public void setPosition(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

}
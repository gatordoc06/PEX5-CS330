package entities.projectiles;
// this file, and all accompanying files, are licensed
// to cadets enrolled in USAFA CS330 in the Fall 2025 term
// no commercial use of the code is licensed

import components.CollisionShape;
import core.PvZ6;

/** the things peashooters make and shoot
  */
public class Pea extends Projectile {
  public static final int DEFAULT_SIZE = 15;
  public static final int SPEED = 6;
  public static final int DAMAGE = 20;
  public static final int Y_OFFSET = 48;

  public Pea(int xPos, int yPos) {
    this(xPos, yPos, DEFAULT_SIZE);
  }

  public Pea(int xPos, int yPos, int size) {
    this.xPos = xPos;
    this.yPos = yPos;
    this.size = size;
    speed = SPEED;
    damage = DAMAGE;
    collisionShape = new CollisionShape(size / 2f,
      CollisionShape.CollisionLayer.BULLET, CollisionShape.CollisionLayer.ZOMBIE);
    collisionShape.setPosition(xPos, yPos - Y_OFFSET);
    
  }

  public void draw() {
    PvZ6.fill(0,175,0);
    PvZ6.stroke(0,100,0);
    PvZ6.circle(xPos, yPos - Y_OFFSET, size);
  }

  public void update(){
    xPos += speed;
    collisionShape.setPosition(xPos, yPos - Y_OFFSET);
  }

  public int getXPos() {
    return xPos;
  }

  public int getYPos() {
    return yPos;
  }

  public CollisionShape getCollisionShape() {
    return collisionShape;
  }
}

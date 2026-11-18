package entities.zombies;
// this file, and all accompanying files, are licensed
// to cadets enrolled in USAFA CS330 in the Fall 2025 term
// no commercial use of the code is licensed

import java.awt.image.BufferedImage;

import components.CollisionShape;
import core.PvZ6;
import entities.plants.Plant;

/** everyone knows what a zombie is
  *
  */
public class Zombie {
  //constants
  public static final int DEFAULT_SIZE = PvZ6.LAWN_SIZE;
  public static final int MOVE_INTERVAL = 5;
  public static final int HEALTH = 100;
  public static final int BITE_DAMAGE = 1;
  public static final int BITE_INTERVAL = PvZ6.GAME_SPEED;

  private int xPos;
  private int yPos;
  private int size;
  private BufferedImage image;
  private int speed;
  private CollisionShape collisionShape;
  private int health;

  //counters for movement and biting
  private int moveCounter;
  private int biteCounter;

  public Zombie(int xPos, int yPos) {
    this(xPos, yPos, DEFAULT_SIZE);
  }

  public Zombie(int xPos, int yPos, int size) {
    this.xPos = xPos;
    this.yPos = yPos;
    this.size = size;
    speed = MOVE_INTERVAL;
    moveCounter = speed;
    health = HEALTH;
    image = PvZ6.loadImage("images/ZombieHD.png");
    collisionShape = new CollisionShape(size / 2f,
      CollisionShape.CollisionLayer.ZOMBIE, CollisionShape.CollisionLayer.PLANT);
    updateCollisionPosition();
  }

  public void draw() {
    PvZ6.image(image, xPos, yPos, xPos+size, yPos-size*400/250);
  }

  public void update(){
    update(null);
  }

  public void update(Plant plantInLane) {
    updateCollisionPosition();
    if (plantInLane != null && collisionShape.collidesWith(plantInLane.getCollisionShape())) {
      biteCounter++;
      if (biteCounter >= BITE_INTERVAL) {
        plantInLane.zombieBitMe(BITE_DAMAGE);
        biteCounter = 0;
      }
      return;
    }

    biteCounter = 0;
    moveCounter--;
    if(moveCounter <= 0){
      xPos -= 1;
      moveCounter = speed;
    }
    updateCollisionPosition();
  }

  private void updateCollisionPosition() {
    collisionShape.setPosition(xPos + size / 2f, yPos - size / 2f);
  }

  public void takeDamage(int damage) {
    health = Math.max(0, health - damage);
  }

  public boolean isDead() {
    return health <= 0;
  }

  public float getCenterY() {
    return yPos - size / 2f;
  }

  public int getXPos() {
    return xPos;
  }

  public int getYPos() {
    return yPos;
  }

  public int getSize() {
    return size;
  }

  public CollisionShape getCollisionShape() {
    return collisionShape;
  }
}

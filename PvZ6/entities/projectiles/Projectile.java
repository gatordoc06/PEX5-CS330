package entities.projectiles;
// this file, and all accompanying files, are licensed
// to cadets enrolled in USAFA CS330 in the Fall 2025 term
// no commercial use of the code is licensed

import components.CollisionShape;
import core.PvZ6;
import entities.Entity;

/** the things peashooters make and shoot
  */
public abstract class Projectile extends Entity {

  protected int speed;
  protected int damage;


  public void update(){
    xPos += speed;
    collisionShape.setPosition(xPos, yPos);
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

  public int getDamage() {
    return damage;
  }
}

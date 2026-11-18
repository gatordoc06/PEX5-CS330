package entities.plants;
// this file, and all accompanying files, are licensed
// to cadets enrolled in USAFA CS330 in the Fall 2025 term
// no commercial use of the code is licensed

import entities.Entity;
import components.CollisionShape;
import core.Game;
import core.PvZ6;
import java.awt.image.BufferedImage;

/** abstract class that is parent class of
  * any plant class
  *
  */

public abstract class Plant extends Entity {
  public static final int SEED_COOLDOWN = PvZ6.GAME_SPEED * 5;

  
  protected String name;
  protected float seedCooldown;
  protected float seedStartCooldown;
  protected int health;
  protected int cost;

  //might want to put this in sub plants or call action cooldown or smth
  protected float currentCooldown;
  protected float cooldown;

  public void zombieBitMe(){
    zombieBitMe(1);
  }

  public void zombieBitMe(int damage) {
    if (health <= 0) {
      return;
    }
    health = Math.max(0, health - damage);
    if(health <= 0){
      die();
    }
  }


  public void die(){
    Game game = Game.getGame();
    if (game != null) {
      game.removePlant(this);
    }
  }

  protected void initializeCollisionShape() {
    collisionShape = new CollisionShape(size / 2f,
        CollisionShape.CollisionLayer.PLANT, CollisionShape.CollisionLayer.ZOMBIE);
    updateCollisionPosition();
  }

  @Override
  public void setPosition(int xPos, int yPos) {
    super.setPosition(xPos, yPos);
    updateCollisionPosition();
  }

  private void updateCollisionPosition() {
    if (collisionShape != null) {
      collisionShape.setPosition(xPos + size / 2f, yPos - size / 2f);
    }
  }

  public float getCenterY() {
    return yPos - size / 2f;
  }

  public int getXPos() {
    return xPos;
  }

  public CollisionShape getCollisionShape() {
    return collisionShape;
  }

  public int getCost() {
    return cost;
  }

  public float getSeedCooldown(){
    return seedCooldown;
  }

  public float getSeedStartCooldown(){
    return seedStartCooldown;
  }

  public BufferedImage getImage() {
    return image;
  }

}

package entities.plants;
// this file, and all accompanying files, are licensed
// to cadets enrolled in USAFA CS330 in the Fall 2025 term
// no commercial use of the code is licensed

import core.PvZ6;
import core.Game;
import entities.projectiles.Pea;

/** a plant that shoots peas
  */
public class Peashooter extends Plant {
  public static final int HEALTH = PvZ6.GAME_SPEED * 10;
  public static final int COST = 100;
  public static final int SHOT_INTERVAL = PvZ6.GAME_SPEED * 3;

  // the value of the peaShootCountdown is decremented
  // each time ShootPea is called
  // when the time reaches zero, a new Pea
  // object is instantiated and the
  // countdown value is reset
  private int peaShootCountdown;
  private boolean zombieInLane;

  public Peashooter() {
    this(PvZ6.LAWN_SIZE);
  }

  public Peashooter(int size) {
    health = HEALTH;
    cost = COST;
    seedCooldown = SEED_COOLDOWN;
    seedStartCooldown = 0;
    this.size = size;
    initializeCollisionShape();
    image = PvZ6.loadImage("images/plant-peashooter.png");
    peaShootCountdown = SHOT_INTERVAL;
  }

   public void update(){
     shootPea();
   }

  public void shootPea(){
    Game game = Game.getGame();
    if (game == null || !zombieInLane) {
      return;
    }

    peaShootCountdown--;
    if(peaShootCountdown <= 0){
      game.addNewProjectile(new Pea(xPos + size, yPos));
      peaShootCountdown = SHOT_INTERVAL;
    }
  }

  public void onZombieStateChanged() {
    Game game = Game.getGame();
    zombieInLane = game != null && game.hasZombieInLane(this);
  }

  /* this method isn't actually needed
   * you can use instanceof operator instead
   */
  public boolean isSunflower(){
    return false;
  }

  /* this method isn't actually needed
   * you can use instanceof operator instead
   */
  public boolean isPeashooter(){
    return true;
  }

  // do nothing
  // there is no collecting sun from a Peashooter
  public void collectSun(){}

}

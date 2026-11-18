package entities.plants;
// this file, and all accompanying files, are licensed
// to cadets enrolled in USAFA CS330 in the Fall 2025 term
// no commercial use of the code is licensed

import core.PvZ6;
import core.Game;
import world.Sun;

/**
 * this is a plant that produces sunshine
 */
public class Sunflower extends Plant {
  public static final int HEALTH = 10;
  public static final int COST = 50;
  public static final int SUN_PRODUCTION_INTERVAL = PvZ6.GAME_SPEED * 15;


  public Sunflower() {
    this(60);
  }

  public Sunflower(int size) {
    health = HEALTH;
    cost = COST;
    seedCooldown = SEED_COOLDOWN;
    seedStartCooldown = 0;
    this.size = size;
    initializeCollisionShape();
    this.cooldown = SUN_PRODUCTION_INTERVAL;
    this.currentCooldown = 0;
    image = PvZ6.loadImage("images/Sunflower2009HD.png");
  }


  public void update() {
    currentCooldown += 1;
    if (currentCooldown >= cooldown) {
      currentCooldown = 0;
      produceSun();
    }
  }

  //kinda works, the sun is behind the sunflower
  private void produceSun() {
    int sunX = xPos + (size - Sun.DEFAULT_SIZE) / 2;
    int sunY = yPos - (size + Sun.DEFAULT_SIZE) / 2;
    Game.getGame().getLawn().addSun(sunX, sunY);
  }
}

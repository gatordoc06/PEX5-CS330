package core;
// this file, and all accompanying files, are licensed
// to cadets enrolled in USAFA CS330 in the Fall 2025 term
// no commercial use of the code is licensed

import java.util.ArrayList;
import java.util.Iterator;

import components.*;
import entities.plants.*;
import entities.projectiles.Projectile;
import entities.zombies.*;
import UI.*;
import world.*;

public class Game{

  private static final int STARTING_SUN = 150;

  private static Game theGame;
  private Lawn lawn;

  private ArrayList<Zombie> zombies;  
  private ArrayList<Plant> plants;  
  private ArrayList<Projectile> projectiles;
  private ArrayList<Peashooter> peashooterObservers;

  public CollisionSystem collisionSystem;

  private GardenManager gardenManager;

  public Game(){
    initialize();
    theGame = this;
  }

  public static Game getGame() {
    return theGame;
  }

  public Lawn getLawn() {
    return lawn;
  }

  private void initialize(){
     gardenManager = GardenManager.getInstance();
     collisionSystem = new CollisionSystem();
      PvZ6.sunCollected = STARTING_SUN;

     lawn = new Lawn(PvZ6.NUM_ROWS, PvZ6.NUM_COLS,
         PvZ6.LAWN_LEFT_EDGE, 120);

     zombies = new ArrayList<>();
     zombies.add(new Zombie(PvZ6.NUM_COLS*PvZ6.LAWN_SIZE + PvZ6.LAWN_LEFT_EDGE, 180));

     plants = new ArrayList<>();
     projectiles = new ArrayList<>();
     peashooterObservers = new ArrayList<>();
  }


  public void draw(){
     update();
     PvZ6.background(0, 150, 0);   // middle green
     PvZ6.fill(50);                //dark gray
     PvZ6.rect(0, 0, PvZ6.LAWN_LEFT_EDGE, PvZ6.height); // place for plant buttons
     PvZ6.fill(255);
     PvZ6.text("sun: "+PvZ6.sunCollected, 10, PvZ6.height-30);

     gardenManager.draw();

     lawn.draw();

     for (Plant p : plants) {
        p.draw();
     }
     for (Zombie z : zombies) {
        z.draw();
     }
     for (Projectile proj : projectiles) {
        proj.draw();
     }
     //broken way to fix my ordering last min 
     lawn.drawSuns();
     

  }


  public void update(){
    gardenManager.update();
    lawn.update();


    for (Plant p : plants) {
        p.update();
    }

    for (Zombie z : zombies) {
        z.update(findPlantInLane(z));
    }

    updateProjectiles();
  }

  //this was more efficent than checking collision for every plant and zombie
  private Plant findPlantInLane(Zombie zombie) {
    int zombieLane = lawn.getRowAtY(zombie.getCenterY());
    Plant closestPlant = null;
    for (Plant plant : plants) {
      if (lawn.getRowAtY(plant.getCenterY()) == zombieLane
              && plant.getXPos() < zombie.getXPos() + zombie.getSize()
              && (closestPlant == null || plant.getXPos() > closestPlant.getXPos())) {
        closestPlant = plant;
      }
    }
    return closestPlant;
  }

  public boolean hasZombieInLane(Plant plant) {
    int plantLane = lawn.getRowAtY(plant.getCenterY());
    for (Zombie zombie : zombies) {
      if (!zombie.isDead()
              && lawn.getRowAtY(zombie.getCenterY()) == plantLane
              && zombie.getXPos() + zombie.getSize() > plant.getXPos()) {
        return true;
      }
    }
    return false;
  }

  //build off my cs330 dinogame, cs110 battleship, cs210 roguelike collision system as refrence
  private void updateProjectiles() {
    collisionSystem.clear();
    for (Zombie zombie : zombies) {
      if (!zombie.isDead()) {
        collisionSystem.addCollisionShape(zombie.getCollisionShape());
      }
    }

    Iterator<Projectile> projectileIterator = projectiles.iterator();
    while (projectileIterator.hasNext()) {
      Projectile projectile = projectileIterator.next();
      projectile.update();
      CollisionShape hitShape = collisionSystem.getFirstCollision(projectile.getCollisionShape());
      if (hitShape != null) {
        //check all zombies
        for (Zombie zombie : zombies) {
          if (zombie.getCollisionShape() == hitShape) {
            zombie.takeDamage(projectile.getDamage());
            break;
          }
        }
        projectileIterator.remove();
      } 
      //check if off lawn
      else if (projectile.getXPos() > lawn.getRightEdge()) {
        projectileIterator.remove();
      }
    }

    if (zombies.removeIf(Zombie::isDead)) {
      notifyPeashootersOfZombieChange();
    }
  }

  //this was in PvZ6 I think but also wanted it here to pass in for lawn
  //not the best way of this
  public void mouseClicked() {
    if (gardenManager.handleMouseClick()) {
      return;
    }
    lawn.mouseClick();
  }


  //Adders and removers for list
  public void addNewProjectile(Projectile projectile){
    projectiles.add(projectile);
  }

  public void addNewZombie(Zombie zombie){
    zombies.add(zombie);
    notifyPeashootersOfZombieChange();
  }

  //used observers to track changes
  public void addNewPlant(Plant plant){
    plants.add(plant);
    if (plant instanceof Peashooter) {
      Peashooter peashooter = (Peashooter) plant;
      peashooterObservers.add(peashooter);
      peashooter.onZombieStateChanged();
    }
  }

  public void removePlant(Plant plant) {
    plants.remove(plant);
    if (plant instanceof Peashooter) {
      peashooterObservers.remove(plant);
    }
    lawn.removePlant(plant);
  }


  //could moved to seperate class but its simpler since game knows all entities
  private void notifyPeashootersOfZombieChange() {
    for (Peashooter peashooter : peashooterObservers) {
      peashooter.onZombieStateChanged();
    }
  }
}

//javac -d out $(Get-ChildItem -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
//java -cp out core.PvZ6
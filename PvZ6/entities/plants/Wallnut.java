//Requirement C: Diversify plants
//Elias Lopez
package entities.plants;

//Wallnuts are simply high-health plants to delay zombies
//No special abilities beyond tanking zombie bites
//Imports for PVZ6 and Game
import core.PvZ6;
import core.Game;

public class Wallnut extends Plant{
    //2x health of a sunflower
    public static final int HEALTH = 20;
    //same cost as a sunflower
    public static final int COST = 50;

    //based off other completed plant classes
    public Wallnut() {
        this(PvZ6.LAWN_SIZE);
    }

    //based off other completed plant classes
    public Wallnut(int size) {
        health = HEALTH;
        cost = COST;
        seedCooldown = SEED_COOLDOWN;
        seedStartCooldown = 0;
        this.size = size;
        initializeCollisionShape();
        //Added new image for Wallnut class
        image = PvZ6.loadImage("images/Wallnut_PLANT_PVZ6.png");
    }
}

package world;
// this file, and all accompanying files, are licensed
// to cadets enrolled in USAFA CS330 in the Fall 2025 term
// no commercial use of the code is licensed

import UI.GardenManager;
import core.PvZ6;
import entities.plants.Plant;

/**
  * LawnSquare objects
  * 1. are drawn as green squares (of alternating hue)
  * 2. can own a plant
  */

public class LawnSquare {
  private int xPos;
  private int yPos;
  private int size;
  private Plant plant;

  public LawnSquare(int xPos, int yPos) {
    this.xPos = xPos;
    this.yPos = yPos;
    this.size = PvZ6.LAWN_SIZE;
    plant = null;
  }

  public void draw() {
    if (((xPos+yPos)/size)%2 ==1) {
      PvZ6.fill(0, 200, 0);
    } else {
      PvZ6.fill(0, 150, 0);
    }
    PvZ6.rect(xPos, yPos, size, size);
  }

  /** plant (the name of the method) is the verb - 'to plant'
    * as in "there was no plant in this lawnSquare
    * before, but there is now
    */
  public void plant(Plant plant) {
    if (isEmptySquare()) {
      this.plant = plant;
      if (plant != null) {
        plant.setPosition(xPos, yPos+PvZ6.LAWN_SIZE);
      }
    }
  }

  /** you can put a plant here
    */
  public boolean isEmptySquare() {
    return plant == null;
  }

  /** you can take the plant out of a lawnSquare
    */
  public void removePlant() {
    plant = null;
  }

  public void removePlant(Plant plantToRemove) {
    if (plant == plantToRemove) {
      plant = null;
    }
  }

  /** this is a helper method for mouseClick()
    * it should remain private
    */
  private boolean isClickInBounds() {
    return (PvZ6.mouseX >= xPos && PvZ6.mouseX <= xPos + size)
      && (PvZ6.mouseY >= yPos && PvZ6.mouseY <= yPos + size);
  }

  /** does what is needed in response to a click of the mouse
   * first checks to see if click is in bounds
   * if in bounds and no plant, then will plant a plant (if one available)
   * if in bounds and no plant, will collect sun, if available
   */
  public void mouseClick() {
    if (isClickInBounds()) {
      if (isEmptySquare()) {
        GardenManager.getInstance().plantSelected(this);
      } 
      
    }
  }

  /** can be used for debugging ... displays the state of the
   *  object on the console
   */
  public void reportState() {
    System.out.print("lawn Square at x: "+xPos);
    System.out.print(" and y: "+yPos);
    if (plant == null) {
      System.out.println(" has NO plant");
    } else {
      System.out.print(" has plant: ");
      System.out.println(plant);
    }
  }
  public int getXPos() {
    return xPos;
  }

  public int getYPos() {
    return yPos;
  } 
  
}

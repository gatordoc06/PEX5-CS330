package UI;

import core.PvZ6;
import entities.plants.*;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public class SeedPacketUI {
    private final Supplier<? extends Plant> plantFactory;
    private final int cost;
    private float currentCooldown;
    private final float maxCooldown;

    private int size;
    private int xPos;
    private int yPos;
    private boolean selected;
    private final BufferedImage image;

    //reworked from plant button- can handle any plant to be more modular
    public SeedPacketUI(Supplier<? extends Plant> plantFactory, int xPos, int yPos){
        this.plantFactory = plantFactory;
        Plant plant = plantFactory.get();
        this.size = 40;
        currentCooldown = plant.getSeedStartCooldown();
        this.maxCooldown = plant.getSeedCooldown();
        this.cost = plant.getCost();
        this.image = plant.getImage();
        this.xPos = xPos;
        this.yPos = yPos;
        this.selected = false;
    }
    

    public void update(){
        if (currentCooldown > 0){
            currentCooldown--;
        }
    }

    //copy from plant Button to replace
    public void draw(){
        PvZ6.fill(200, 200, 0);
        PvZ6.rect(xPos, yPos, size, size);
        if (image != null) {
            PvZ6.image(image, xPos, yPos, xPos + size, yPos + size);
        }
        if (maxCooldown > 0 && currentCooldown > 0) {
            int shadeHeight = (int) (size * Math.min(1, currentCooldown / maxCooldown));
            PvZ6.fill(100, 100, 100, 200);
            PvZ6.rect(xPos, yPos, size, shadeHeight);
        }
        if (selected) {
            PvZ6.noFill();
            PvZ6.strokeWeight(3);
            PvZ6.stroke(255, 255, 0);
            PvZ6.rect(xPos, yPos, size, size);
            PvZ6.fill(255);
            PvZ6.strokeWeight(1);
            PvZ6.stroke(0);
        }
        PvZ6.fill(255);
        PvZ6.text(Integer.toString(cost), xPos, yPos + size + 12);
        PvZ6.fill(0);
    }

    public boolean isReady(){
        return currentCooldown <=0;
    }

    public boolean containsMouse() {
        return (PvZ6.mouseX >= xPos && PvZ6.mouseX <= xPos + size) 
        && (PvZ6.mouseY >= yPos && PvZ6.mouseY <= yPos + size);
    }

    public int getCost() {
        return cost;
    }

    public Plant createPlant() {
        return plantFactory.get();
    }

    public void planted() {
        currentCooldown = maxCooldown;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}

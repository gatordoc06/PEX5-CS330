package UI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import core.Game;
import core.PvZ6;
import entities.plants.*;
import world.LawnSquare;

public class GardenManager {
    private List<SeedPacketUI> seedPackets;
    private static GardenManager instance;
    private SeedPacketUI selectedPacket;

    private GardenManager() {
        seedPackets = new ArrayList<>();
        addSeedPacket(Peashooter::new, 0, 0);
        addSeedPacket(Sunflower::new, 50, 0);
    }
    
    
    public static GardenManager getInstance() {
        if (instance == null) {
            instance = new GardenManager();
        }
        return instance;
    }

    public void update(){
        for (SeedPacketUI seedPacket : seedPackets) {
            seedPacket.update();
        }
    }
    
    public void draw(){
        for (SeedPacketUI seedPacket : seedPackets) {
            seedPacket.draw();
        }
    }

    /** Adds a plant packet without coupling the manager to a specific plant type. */
    public void addSeedPacket(Supplier<? extends Plant> plantFactory, int xPos, int yPos) {
        seedPackets.add(new SeedPacketUI(plantFactory, xPos, yPos));
    }

    /** Returns true when a click was on a seed packet and was handled. */
    public boolean handleMouseClick() {
        for (SeedPacketUI packet : seedPackets) {
            if (packet.containsMouse()) {
                if (selectedPacket == packet) {
                    selectedPacket.setSelected(false);
                    selectedPacket = null;
                } else if (packet.isReady() && PvZ6.sunCollected >= packet.getCost()) {
                    if (selectedPacket != null) {
                        selectedPacket.setSelected(false);
                    }
                    selectedPacket = packet;
                    selectedPacket.setSelected(true);
                }
                return true;
            }
        }
        return false;
    }

    /** Plants the selected packet on an empty square and charges sun only on success. */
    public boolean plantSelected(LawnSquare square) {
        if (selectedPacket == null || square == null || !square.isEmptySquare()
                || !selectedPacket.isReady()
                || PvZ6.sunCollected < selectedPacket.getCost()) {
            return false;
        }

        Plant plant = selectedPacket.createPlant();
        square.plant(plant);
        Game.getGame().addNewPlant(plant);
        PvZ6.sunCollected -= selectedPacket.getCost();
        selectedPacket.planted();
        selectedPacket.setSelected(false);
        selectedPacket = null;
        return true;
    }
}

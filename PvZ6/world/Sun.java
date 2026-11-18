package world;
// this file, and all accompanying files, are licensed
// to cadets enrolled in USAFA CS330 in the Fall 2025 term
// no commercial use of the code is licensed

import java.awt.image.BufferedImage;

import core.PvZ6;

/**
 * represents the sun that a sunflower produces
 * (this is not the sun collection value)
 */
public class Sun {
  public static final int DEFAULT_SIZE = 40;
  public static final int VALUE = 25;
  public static final int FALL_SPEED = 2;
  public static final int RISE_HEIGHT = 48;
  public static final int LIFETIME = PvZ6.GAME_SPEED * 20;

  //handles either falling or sunflower motion
  private enum Motion {
    FALLING_FROM_SKY,
    RISE_AND_RETURN
  }

  private int xPos;
  private int yPos;
  private int size;
  private int targetY;
  private int riseTargetY;
  private boolean rising;
  private Motion motion;
  private BufferedImage image;
  private int sunValue;

  public Sun(int xPos, int yPos) {
    this(xPos, yPos, yPos, DEFAULT_SIZE);
  }

  public Sun(int xPos, int yPos, int size) {
    this(xPos, yPos, yPos, size);
  }

  public Sun(int xPos, int yPos, int targetY, int size) {
    this(xPos, yPos, targetY, size, Motion.FALLING_FROM_SKY);
  }

  private Sun(int xPos, int yPos, int targetY, int size, Motion motion) {
    this.xPos = xPos;
    this.yPos = yPos;
    this.targetY = targetY;
    this.riseTargetY = Math.max(0, yPos - RISE_HEIGHT);
    this.motion = motion;
    this.rising = motion == Motion.RISE_AND_RETURN;
    this.size = size;
    image = PvZ6.loadImage("images/Sun_PvZ2.png");
    this.sunValue = VALUE;
  }

  public static Sun fromPlant(int xPos, int yPos) {
    return new Sun(xPos, yPos, yPos, DEFAULT_SIZE, Motion.RISE_AND_RETURN);
  }

  public void draw() {
    PvZ6.image(image, xPos, yPos, xPos + size, yPos + size);
  }

  public void update() {
    if (motion == Motion.RISE_AND_RETURN && rising) {
      yPos = Math.max(riseTargetY, yPos - FALL_SPEED);
      if (yPos == riseTargetY) {
        rising = false;
      }
    } else if (yPos < targetY) {
      yPos = Math.min(targetY, yPos + FALL_SPEED);
    }
  }


  public int getSunValue() {
    return sunValue;
  }

  public boolean isClickInBounds() {
    return (PvZ6.mouseX >= xPos && PvZ6.mouseX <= xPos + size)
      && (PvZ6.mouseY >= yPos && PvZ6.mouseY <= yPos + size);
  }
}

package world;

import java.util.ArrayList;
import java.util.Iterator;
import core.PvZ6;

public class Lawn {
  public static final int SUN_INITIAL_DELAY = PvZ6.GAME_SPEED * 5;
  public static final int SUN_SPAWN_INTERVAL = PvZ6.GAME_SPEED * 8;

  private LawnSquare[][] squares;
  private ArrayList<Sun> suns;
  private int rows;
  private int cols;
  private int startX;
  private int startY;
  private int sunSpawnCountdown;

  public Lawn(int rows, int cols, int startX, int startY) {
    this.rows = rows;
    this.cols = cols;
    this.startX = startX;
    this.startY = startY;
    this.squares = new LawnSquare[rows][cols];

    this.suns = new ArrayList<Sun>();
    this.sunSpawnCountdown = SUN_INITIAL_DELAY;

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        squares[r][c] = new LawnSquare(startX + c * PvZ6.LAWN_SIZE, startY + r * PvZ6.LAWN_SIZE);
      }
    }
  }

  //draw normal grids
  public void draw() {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        squares[r][c].draw();
      }
    }
  }

  //fix ordering 
  public void drawSuns() {
    for (Sun sun : suns) {
      sun.draw();
    }
  }

  public void update() {
    sunSpawnCountdown--;
    if (sunSpawnCountdown <= 0) {
      addSun();
      sunSpawnCountdown = SUN_SPAWN_INTERVAL;
    }

    //loop through suns and update them,
    Iterator<Sun> sunIterator = suns.iterator();
    while (sunIterator.hasNext()) {
      Sun sun = sunIterator.next();
      sun.update();
      
    }
  }

  public void mouseClick() {
    for (int i = suns.size() - 1; i >= 0; i--) {
      Sun sun = suns.get(i);
      if (sun.isClickInBounds()) {
        PvZ6.sunCollected += sun.getSunValue();
        suns.remove(i);
        return;
      }
    }

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        squares[r][c].mouseClick();
      }
    }

  }

  public LawnSquare getSquare(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return null;
    }
    return squares[row][col];
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  public int getRowAtY(float y) {
    int row = (int) Math.floor((y - startY) / (float) PvZ6.LAWN_SIZE);
    return row >= 0 && row < rows ? row : -1;
  }

  public int getRightEdge() {
    return startX + cols * PvZ6.LAWN_SIZE;
  }

  public void removePlant(entities.plants.Plant plant) {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        squares[r][c].removePlant(plant);
      }
    }
  }

  public LawnSquare getRandomSquare() {
    if (rows == 0 || cols == 0) {
      return null;
    }
    int row = (int)(Math.random() * rows);
    int col = (int)(Math.random() * cols);
    return squares[row][col];
  }


  public void addSun() {
    if (rows == 0 || cols == 0) {
      return;
    }
    int sunSize = Sun.DEFAULT_SIZE;
    int column = (int) (Math.random() * cols);
    int xPos = startX + column * PvZ6.LAWN_SIZE + (PvZ6.LAWN_SIZE - sunSize) / 2;
    int targetY = startY + (int) (Math.random() * Math.max(1,
        rows * PvZ6.LAWN_SIZE - sunSize));
    suns.add(new Sun(xPos, -sunSize, targetY, sunSize));
  }

  //for sunflower- might want to change name
  public void addSun(int xPos, int yPos) {
    suns.add(Sun.fromPlant(xPos, yPos));
  }
}

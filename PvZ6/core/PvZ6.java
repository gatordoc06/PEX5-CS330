package core;
// this file, and all accompanying files, are licensed
// to cadets enrolled in USAFA CS330 in the Fall 2025 term
// no commercial use of the code is licensed

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/** this class plays the role that the main .pde file (and the
  * Processing/PApplet runtime behind it) used to play: it owns the
  * window, the draw loop, and the small set of drawing/input
  * primitives (fill, rect, image, mouseX, mouseY, etc.) that the
  * rest of the classes use, the same way they used to call PApplet
  * methods directly. all of the game logic still lives in Game and
  * the other classes - this file only deals with graphics/input,
  * so that logic and graphics stay separated.
  */
public class PvZ6 extends JPanel {

  public static final int GAME_SPEED = 30; // expressed in frames per second (larger numbers go faster)

  public static final int NUM_ROWS = 5;
  public static final int NUM_COLS = 9;

  public static final int LAWN_SIZE = 60;
  public static final int LAWN_LEFT_EDGE = 100;

  public static Font font;

  public static Game game;

  //TODO: do something about this global variable
  public static int sunCollected = 0;

  // these used to be provided implicitly by Processing/PApplet
  public static int width;
  public static int height;
  public static int mouseX;
  public static int mouseY;
  public static char key;

  // only one image mode / text alignment is actually implemented below,
  // but these are kept so setup() can call imageMode()/textAlign() like before
  static final int CORNERS = 1;
  static final int LEFT = 1;
  static final int CENTER = 2;

  private static PvZ6 panel;
  private static Timer timer;
  private static int frameRateValue = 60;

  private static Graphics2D g2;
  private static Color fillColor = Color.WHITE;
  private static Color strokeColor = Color.BLACK;
  private static int currentStrokeWeight = 1;
  private static Font currentFont;

  public static void main(String[] args) {
    setup();

    JFrame frame = new JFrame("PvZ6");
    panel = new PvZ6();
    panel.setPreferredSize(new Dimension(width, height));
    panel.setFocusable(true);
    frame.add(panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.pack();
    frame.setLocationRelativeTo(null);

    panel.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        PvZ6.mouseClicked();
      }
    });

    panel.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        key = e.getKeyChar();
        PvZ6.keyPressed();
      }
    });

    timer = new Timer(1000 / frameRateValue, e -> panel.repaint());

    frame.setVisible(true);
    panel.requestFocusInWindow();
    timer.start();
  }

  private static void setup() {
    size(LAWN_LEFT_EDGE + NUM_COLS * LAWN_SIZE + 40,
         NUM_ROWS * LAWN_SIZE + 180);
    frameRate(GAME_SPEED);
    imageMode(CORNERS);

    font = createFont("Georgia", 24);
    textFont(font);
    textAlign(LEFT, CENTER);

    game = new Game();
  }

  private static void draw() {
    game.draw();
  }

  private static void mouseClicked() {
    game.mouseClicked();
  }

  private static void keyPressed() {
    if (key == ' ') {
      noLoop();
    } else {
      loop();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g2 = (Graphics2D) g;
    draw();
  }

  // ---- drawing/input primitives (stand-ins for what PApplet used to provide) ----

  public static void size(int w, int h) {
    width = w;
    height = h;
  }

  public static void frameRate(int fps) {
    frameRateValue = fps;
    if (timer != null) {
      timer.setDelay(1000 / frameRateValue);
    }
  }

  public static void imageMode(int mode) {
    // only CORNERS-style placement is implemented (see image()); kept so setup() matches the original
  }

  public static Font createFont(String name, int size) {
    return new Font(name, Font.PLAIN, size);
  }

  public static void textFont(Font f) {
    currentFont = f;
  }

  public static void textAlign(int horizontal, int vertical) {
    // only LEFT/CENTER alignment is implemented (see text()); kept so setup() matches the original
  }

  public static void loop() {
    if (timer != null) {
      timer.start();
    }
  }

  public static void noLoop() {
    if (timer != null) {
      timer.stop();
    }
  }

  public static void background(int r, int g, int b) {
    g2.setColor(new Color(r, g, b));
    g2.fillRect(0, 0, width, height);
  }

  public static void fill(int gray) {
    fillColor = new Color(gray, gray, gray);
  }

  public static void fill(int r, int g, int b) {
    fillColor = new Color(r, g, b);
  }

  public static void fill(int r, int g, int b, int a) {
    fillColor = new Color(r, g, b, a);
  }

  public static void noFill() {
    fillColor = null;
  }

  public static void stroke(int gray) {
    strokeColor = new Color(gray, gray, gray);
  }

  public static void stroke(int r, int g, int b) {
    strokeColor = new Color(r, g, b);
  }

  public static void strokeWeight(int w) {
    currentStrokeWeight = w;
  }

  public static void rect(int x, int y, int w, int h) {
    if (fillColor != null) {
      g2.setColor(fillColor);
      g2.fillRect(x, y, w, h);
    }
    if (strokeColor != null) {
      g2.setColor(strokeColor);
      g2.setStroke(new BasicStroke(currentStrokeWeight));
      g2.drawRect(x, y, w, h);
    }
  }

  public static void circle(int x, int y, int d) {
    int r = d / 2;
    if (fillColor != null) {
      g2.setColor(fillColor);
      g2.fillOval(x - r, y - r, d, d);
    }
    if (strokeColor != null) {
      g2.setColor(strokeColor);
      g2.setStroke(new BasicStroke(currentStrokeWeight));
      g2.drawOval(x - r, y - r, d, d);
    }
  }

  public static void image(BufferedImage img, int x1, int y1, int x2, int y2) {
    if (img == null) {
      return;
    }
    int left = Math.min(x1, x2);
    int top = Math.min(y1, y2);
    int w = Math.abs(x2 - x1);
    int h = Math.abs(y2 - y1);
    g2.drawImage(img, left, top, left + w, top + h,
      0, 0, img.getWidth(), img.getHeight(), null);
  }

  public static void text(String s, int x, int y) {
    g2.setFont(currentFont);
    g2.setColor(fillColor != null ? fillColor : Color.BLACK);
    FontMetrics fm = g2.getFontMetrics();
    int baseline = y - fm.getHeight() / 2 + fm.getAscent();
    g2.drawString(s, x, baseline);
  }

  public static BufferedImage loadImage(String filename) {
    try {
      return ImageIO.read(new File(filename));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

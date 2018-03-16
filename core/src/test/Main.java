package test;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Main
{
  public static final int MAX_SPAWN = 80;
  public static final int X = 640;
  public static final int Y = 480;
  public static final double GRAVITY = 1500;
  public static final double DRAG = 0.2;
  public static final double BOUNCE = 0.9;
  public static final String TITLE = "Mike's 2D Physics Engine";
  private static JFrame f;
  private static Canvas c;
  public static BufferStrategy b;
  private static GraphicsEnvironment ge;
  private static GraphicsDevice gd;
  private static GraphicsConfiguration gc;
  private static BufferedImage buffer;
  private static Graphics graphics;
  private static Graphics2D g2d;
  private static AffineTransform at;
  public static ArrayList<Spawn> living = new ArrayList<Spawn>();
  public static boolean isRunning = true;

  public static void main(String[] args)
  {
    // Initialize some things.
    initializeJFrame();
    // Create and start threads.
    Thread moveEngine = new MoveEngine();
    moveEngine.start();
    Thread makeBall = new MakeBall();
    makeBall.start();
    // Run the animation loop.
    runAnimation();
  }

  public static void runAnimation()
  {
    // Set up some variables.
    int fps = 0;
    int frames = 0;
    long totalTime = 0;
    long curTime = System.currentTimeMillis();
    long lastTime = curTime;
    // Start the loop.
    while (isRunning) {
      try {
        // Calculations for FPS.
        lastTime = curTime;
        curTime = System.currentTimeMillis();
        totalTime += curTime - lastTime;
        if (totalTime > 1000) {
          totalTime -= 1000;
          fps = frames;
          frames = 0;
        }
        ++frames;
        // clear back buffer...
        g2d = buffer.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, X, Y);
        // Draw entities
        for (int i = 0; i < living.size(); i++) {
          at = new AffineTransform();
          at.translate(living.get(i).getX(), living.get(i).getY());
          g2d.setColor(Color.BLACK);
          Spawn s = living.get(i);
          g2d.fill(new Ellipse2D.Double(s.getX(), s.getY(), s
                                        .getRadius() * 2, s.getRadius() * 2));
        }
        // display frames per second...
        g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
        g2d.setColor(Color.GREEN);
        g2d.drawString(String.format("FPS: %s", fps), 20, 20);
        // Blit image and flip...
        graphics = b.getDrawGraphics();
        graphics.drawImage(buffer, 0, 0, null);
        if (!b.contentsLost()) b.show();
        // Let the OS have a little time...
        Thread.sleep(15);
      } catch (InterruptedException e) {
      } finally {
        // release resources
        if (graphics != null) graphics.dispose();
        if (g2d != null) g2d.dispose();
      }
    }
  }

  public static boolean allDead()
  {
    if (living.size() < 1) return true;
    return false;
  }

  public static synchronized int giveBirth(int x, int y, double vx,
                                           double vy, int m)
  {
    if (living.size() >= MAX_SPAWN) return 1;
    living.add(new Spawn(x, y, vx, vy, m));
    return 0;
  }

  private static void initializeJFrame()
  {
    // Create the frame...
    f = new JFrame(TITLE);
    f.setIgnoreRepaint(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Create canvas for painting...
    c = new Canvas();
    c.setIgnoreRepaint(true);
    c.setSize(X, Y);
    // Add the canvas, and display.
    f.add(c);
    f.pack();
    // The following line centers the window on the screen.
    f.setLocationRelativeTo(null);
    f.setVisible(true);
    // Set up the BufferStrategy for double buffering.
    c.createBufferStrategy(2);
    b = c.getBufferStrategy();
    // Get graphics configuration...
    ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    gd = ge.getDefaultScreenDevice();
    gc = gd.getDefaultConfiguration();
    // Create off-screen drawing surface
    buffer = gc.createCompatibleImage(X, Y);
    // Objects needed for rendering...
    graphics = null;
    g2d = null;
  }
}

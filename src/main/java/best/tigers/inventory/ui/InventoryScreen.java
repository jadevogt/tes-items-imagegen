package best.tigers.inventory.ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InventoryScreen implements InventoryComponent {
  private final BufferedImage img;
  public static final int WIDTH = 1242;
  public static final int HEIGHT = 1263;

  public InventoryScreen() {
    this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
  }

  @Override
  public BufferedImage buildImage() {
    drawBackground();
    drawInner();
    return img;
  }

  public void drawBackground() {
    BufferedImage bg;
    try {
      bg = ImageIO.read(new File("./assets/background.png"));
    } catch (IOException ignored) {
      return;
    }
    Graphics2D g = img.createGraphics();
    g.drawImage(bg, 0, 0, WIDTH, HEIGHT, null);
  }

  public void drawInner() {
    var inner = new InnerInventoryScreen();
    Graphics2D g = img.createGraphics();
    g.drawImage(
        inner.buildImage(),
        InnerInventoryScreen.OFFSET_X,
        InnerInventoryScreen.OFFSET_Y,
        InnerInventoryScreen.WIDTH,
        InnerInventoryScreen.HEIGHT,
        null);
  }
}

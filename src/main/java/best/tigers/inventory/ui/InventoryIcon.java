package best.tigers.inventory.ui;

import best.tigers.inventory.items.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class InventoryIcon implements InventoryComponent {
  public static final int WIDTH = 120;
  public static final int HEIGHT = 118;
  public static final int INSET_WIDTH = 80;
  public static final int INSET_HEIGHT = 80;
  public static final int INSET_X = 15;
  public static final int INSET_Y = 20;
  private BufferedImage img;
  private final Item item;

  public InventoryIcon(Item item) {
    this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    this.item = item;
  }

  public static BufferedImage resize(BufferedImage img, int width, int height) {
    // Calculate scale factors to preserve aspect ratio
    double scalex = (double) width / img.getWidth();
    double scaley = (double) height / img.getHeight();
    double scale = Math.min(scalex, scaley);

    // Compute new dimensions
    int newWidth = (int) (img.getWidth() * scale);
    int newHeight = (int) (img.getHeight() * scale);

    // Create a new BufferedImage with the desired size
    BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

    // Create a Graphics2D object for the new image
    Graphics2D g2d = resized.createGraphics();

    // Enable high-quality rendering hints
    g2d.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Create the AffineTransform for scaling
    AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);

    // Draw the original image onto the new image with scaling
    g2d.drawImage(img, transform, null);

    // Dispose of the Graphics2D object
    g2d.dispose();

    return resized;
  }

  private static BufferedImage dropShadow(BufferedImage img) {
    // a filter which converts all colors except 0 to black
    ImageProducer prod =
        new FilteredImageSource(
            img.getSource(),
            new RGBImageFilter() {
              @Override
              public int filterRGB(int x, int y, int rgb) {
                if (rgb == 0) return 0;
                else return 0xff000000;
              }
            });
    // create whe black image
    Image shadow = Toolkit.getDefaultToolkit().createImage(prod);

    // result
    BufferedImage result =
        new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) result.getGraphics();

    // draw shadow with offset
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    g.drawImage(shadow, 2, 2, null);
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    // draw original image
    g.drawImage(img, 0, 0, null);

    return result;
  }

  public void drawIcon() {
    BufferedImage icon;
    BufferedImage stolenIndicator;
    try {
      icon = ImageIO.read(item.icon().toFile());
    } catch (IOException ignored) {
      return;
    }
    try {
      stolenIndicator = ImageIO.read(new File("./assets/icons/stolen.png"));
    } catch (IOException ignored) {
      return;
    }
    Graphics2D g = img.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    icon = resize(icon, INSET_WIDTH, INSET_HEIGHT);
    if (item.icon().toString().contains("unique")) {
      icon = dropShadow(icon);
    }
    g.drawImage(icon, INSET_X, INSET_Y, INSET_WIDTH, INSET_HEIGHT, null);
    if (item.stolen()) {
      g.drawImage(stolenIndicator, 15, 71, 32, 32, null);
    }
  }

  @Override
  public BufferedImage buildImage() {
    drawIcon();
    return img;
  }
}

package best.tigers.inventory.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class InventoryRow implements InventoryComponent {
  public static final int WIDTH = 980;
  public static final int HEIGHT = 118;
  public static final int GOLD_AREA_WIDTH = 128;
  public static final int GOLD_AREA_OFFSET_X = 576;
  public static final int TEXT_AREA_WIDTH = 450;
  public static final int TEXT_AREA_OFFSET_X = 124;
  public static final int WEIGHT_AREA_WIDTH = 94;
  public static final int WEIGHT_AREA_OFFSET_X = 704;
  public final Rectangle textArea;
  public final Rectangle goldArea;
  public final Rectangle weightArea;
  // public static final int TEXT_AREA_OFFSET_X = 124;
  private final BufferedImage img;
  private final String name;
  private final String iconName;
  private final int goldValue;
  private final double weightValue;

  public InventoryRow(String name, String icon, int goldValue, double weightValue) {
    this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    this.name = name;
    this.iconName = icon;
    this.goldValue = goldValue;
    this.weightValue = weightValue;
    this.textArea = new Rectangle(0, 0, (int) (TEXT_AREA_WIDTH * 0.8), HEIGHT);
    this.goldArea = new Rectangle(GOLD_AREA_OFFSET_X, 0, (int) (GOLD_AREA_WIDTH * 0.8), HEIGHT);
    this.weightArea =
        new Rectangle(WEIGHT_AREA_OFFSET_X, 0, (int) (WEIGHT_AREA_WIDTH * 0.8), HEIGHT);
    registerFont();
  }

  public InventoryRow(String name, String icon, int goldValue) {
    this(name, icon, goldValue, Math.floor(Math.random() * 100));
  }

  @Override
  public BufferedImage buildImage() {
    drawRow();
    return img;
  }

  public int getNumLines(Graphics2D unscaled, Font font, String text) {
    unscaled.setFont(font);
    FontMetrics metrics = unscaled.getFontMetrics(unscaled.getFont());
    var maxWidth = (int) textArea.getWidth();
    if (metrics.stringWidth(text) > maxWidth) {
      var words = text.split(" ");
      StringBuilder currentLine = new StringBuilder(words[0]);
      ArrayList<String> lines = new ArrayList<>();
      for (int i = 1; i < words.length; i++) {
        if (metrics.stringWidth(currentLine + " " + words[i]) <= maxWidth) {
          currentLine.append(" ").append(words[i]);
        } else {
          lines.add(currentLine.toString());
          currentLine = new StringBuilder();
          currentLine.append(words[i]);
        }
      }
      lines.add(currentLine.toString());
      return lines.size();
    }
    return 1;
  }

  public void registerFont() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    try {
      Font customFont;
      customFont = Font.createFont(Font.TRUETYPE_FONT, new File("./assets/fonts/oblivion.otf"));
      ge.registerFont(customFont);
    } catch (IOException | FontFormatException e) {
      e.printStackTrace();
    }
  }

  public void setOblivionFont(Graphics2D graphics) {
    Font font = new Font("OblivionCustom_v02", Font.PLAIN, 42);
    graphics.setFont(font);
  }

  public void drawWeightArea(double weight, Graphics2D g) {
    BufferedImage unscaledImg =
        new BufferedImage((int) weightArea.getWidth(), HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D unscaled = unscaledImg.createGraphics();
    setOblivionFont(unscaled);
    FontMetrics metrics = unscaled.getFontMetrics(unscaled.getFont());
    int x =
        (((int) weightArea.getWidth()
                - (int) metrics.getStringBounds(Integer.toString((int) weight), g).getWidth())
            / 2);
    int y =
        (int) weightArea.getY()
            + (((int) weightArea.getHeight() - metrics.getHeight()) / 2)
            + metrics.getAscent();
    unscaled.setColor(new Color(109, 62, 39));
    unscaled.drawString(Integer.toString((int) weight), x, y);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(unscaledImg, WEIGHT_AREA_OFFSET_X, 0, WEIGHT_AREA_WIDTH, HEIGHT, null);
  }

  public void drawGoldArea(int amount, Graphics2D g) {
    BufferedImage unscaledImg =
        new BufferedImage((int) goldArea.getWidth(), HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D unscaled = unscaledImg.createGraphics();
    setOblivionFont(unscaled);
    FontMetrics metrics = unscaled.getFontMetrics(unscaled.getFont());
    int x =
        (((int) goldArea.getWidth()
                - (int) metrics.getStringBounds(Integer.toString(amount), g).getWidth())
            / 2);
    int y =
        (int) goldArea.getY()
            + (((int) goldArea.getHeight() - metrics.getHeight()) / 2)
            + metrics.getAscent();
    unscaled.setColor(new Color(109, 62, 39));
    unscaled.drawString(Integer.toString(amount), x, y);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(unscaledImg, GOLD_AREA_OFFSET_X, 0, GOLD_AREA_WIDTH, HEIGHT, null);
  }

  public void drawTextArea(String text, Graphics2D g) {
    int textAreaWidthScaled = (int) (TEXT_AREA_WIDTH * 0.80);
    Font font = new Font("OblivionCustom_v02", Font.PLAIN, 42);
    int size = 42;
    BufferedImage unscaledImg =
        new BufferedImage(textAreaWidthScaled, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D unscaled = unscaledImg.createGraphics();
    unscaled.setFont(font);
    while (getNumLines(unscaled, font, text) > 2) {
      size--;
      font = new Font("OblivionCustom_v02", Font.PLAIN, size);
      unscaled.setFont(font);
    }
    FontMetrics metrics = unscaled.getFontMetrics(unscaled.getFont());
    var maxWidth = (int) textArea.getWidth();
    if (metrics.stringWidth(text) > maxWidth) {
      var words = text.split(" ");
      StringBuilder currentLine = new StringBuilder(words[0]);
      ArrayList<String> lines = new ArrayList<>();
      for (int i = 1; i < words.length; i++) {
        if (metrics.stringWidth(currentLine + " " + words[i]) <= maxWidth) {
          currentLine.append(" ").append(words[i]);
        } else {
          lines.add(currentLine.toString());
          currentLine = new StringBuilder();
          currentLine.append(words[i]);
        }
      }
      lines.add(currentLine.toString());
      int lineHeight = metrics.getHeight();
      int totalHeight = lineHeight * lines.size();
      int topy = (unscaledImg.getHeight() - totalHeight) / 2 + metrics.getAscent();

      for (int i = 0; i < lines.size(); i++) {
        unscaled.setColor(new Color(109, 62, 39));
        unscaled.drawString(lines.get(i), 0, topy + (lineHeight * i));
      }
    } else {
      int y =
          (int) textArea.getY()
              + (((int) textArea.getHeight() - metrics.getHeight()) / 2)
              + metrics.getAscent();
      unscaled.setColor(new Color(109, 62, 39));
      unscaled.drawString(text, 0, y);
    }
    // Determine the X coordinate for the text

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(unscaledImg, TEXT_AREA_OFFSET_X, 0, TEXT_AREA_WIDTH, HEIGHT, null);
  }

  public void drawRow() {
    InventoryIcon icon = new InventoryIcon(iconName);
    Graphics2D g = img.createGraphics();
    g.drawImage(icon.buildImage(), 0, 0, InventoryIcon.WIDTH, InventoryIcon.HEIGHT, null);
    drawTextArea(name, g);
    drawGoldArea(goldValue, g);
    drawWeightArea(weightValue, g);
  }
}

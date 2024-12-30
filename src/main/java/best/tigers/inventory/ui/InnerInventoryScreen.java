package best.tigers.inventory.ui;

import best.tigers.inventory.items.Item;
import best.tigers.inventory.util.FindIcons;
import best.tigers.inventory.util.Parser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InnerInventoryScreen implements InventoryComponent {
  public static final int WIDTH = 980;
  public static final int HEIGHT = 708;
  public static final int ROW_COUNT = 6;
  public static final int OFFSET_X = 90;
  public static final int OFFSET_Y = 154;
  public static final int HEIGHT_OFFSET = 0;
  private BufferedImage img;
  private final List<Item> items;

  public InnerInventoryScreen(List<Item> items) {
    this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    this.items = items;
  }

  public InnerInventoryScreen() {
    this(new ArrayList<>());
  }

  @Override
  public BufferedImage buildImage() {
    drawScreen();
    return img;
  }

  public void drawScreen() {
    for (int i = 0; i < items.size(); i++) {
      InventoryRow row = new InventoryRow(items.get(i));
      int yOffset = i * InventoryRow.HEIGHT;
      Graphics2D g = img.createGraphics();
      g.drawImage(row.buildImage(), 0, yOffset, InventoryRow.WIDTH, InventoryRow.HEIGHT, null);
    }
  }
}

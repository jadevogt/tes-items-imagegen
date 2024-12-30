package best.tigers.inventory.ui;

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

  public InnerInventoryScreen() {
    this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
  }

  @Override
  public BufferedImage buildImage() {
    drawScreen();
    return img;
  }

  public void drawScreen() {
    List<String> lines = new ArrayList<>();
    try (InputStream in =
        InnerInventoryScreen.class.getClassLoader().getResourceAsStream("example/random.txt")) {
      if (in == null) {
        throw new IOException("Resource not found");
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      lines.addAll(reader.lines().filter(Parser::itemFilter).toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    Collections.shuffle(lines);
    for (int i = 0; i < ROW_COUNT; i++) {
      var item = Parser.parseItem(lines.get(i));
      var armorIcon = FindIcons.locatedIcon("armor", item.name().toLowerCase());
      var weaponIcon = FindIcons.locatedIcon("weapon", item.name().toLowerCase());
      var icon = weaponIcon == null ? armorIcon : weaponIcon;
      double weightValue = Math.floor(Math.random() * 100);
      int goldValue = (int) Math.floor(Math.random() * 300);
      if (icon == null) {
        icon = FindIcons.locatedMiscIcon(item.name().toLowerCase());
        weightValue = Math.floor(Math.random() * 10);
        goldValue = (int) Math.floor(Math.random() * 10);
      }
      if (icon == null) {
        icon = new File("broken.png");
        weightValue = Math.floor(Math.random() * 10);
        goldValue = (int) Math.floor(Math.random() * 10);
      }
      if (item.name().toLowerCase().contains("tome")
          || item.name().toLowerCase().contains("book")) {
        icon = FindIcons.randomBook();
        weightValue = Math.floor(Math.random() * 10);
        goldValue = (int) Math.floor(Math.random() * 500);
      }
      if (item.name().toLowerCase().contains("potion")) {
        icon = new File("./assets/icons/potion.png");
        weightValue = Math.floor(Math.random() * 3);
        goldValue = (int) Math.floor(Math.random() * 70);
      }
      InventoryRow row = new InventoryRow(item.name(), icon.getPath(), goldValue, weightValue);
      int yOffset = i * InventoryRow.HEIGHT;
      Graphics2D g = img.createGraphics();
      g.drawImage(row.buildImage(), 0, yOffset, InventoryRow.WIDTH, InventoryRow.HEIGHT, null);
    }
  }
}

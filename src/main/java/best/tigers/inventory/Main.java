package best.tigers.inventory;

import best.tigers.inventory.ui.InventoryScreen;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    var inv = new InventoryScreen();
    for (int i = 0; i < 1; i++) {
      File outputfile = new File("out/image" + i + ".png");
      ImageIO.write(inv.buildImage(), "PNG", outputfile);
    }
  }
}

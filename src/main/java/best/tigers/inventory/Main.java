package best.tigers.inventory;

import best.tigers.inventory.items.Item;
import best.tigers.inventory.items.ItemBuilder;
import best.tigers.inventory.ui.InventoryScreen;
import best.tigers.inventory.util.BlueSky;
import best.tigers.inventory.util.Parser;
import best.tigers.inventory.util.Post;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class Main {
 
  public static void main(String[] args) {
    //List<Post> lines = BlueSky.getPosts(1440);
    List<Post> lines = BlueSky.getPosts(4000);
    ItemBuilder builder = new ItemBuilder(Path.of("./assets/icons"));
      List<Item> items =
          lines.stream()
              .map(Parser::cleanItemPost)
              .filter(i -> builder.getFactory(i.content()) != null)
              .filter(Parser::itemFilter)
              .map(i -> builder.getFactory(i).constructItem(i))
              .limit(6)
              .toList();
    var inv = new InventoryScreen(items);
    try {
    File outputfile = new File("out/image.png");
    ImageIO.write(inv.buildImage(), "PNG", outputfile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    /*
    try {
      BlueSky.createPost(inv.buildImage(), items);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
     */
  }
}

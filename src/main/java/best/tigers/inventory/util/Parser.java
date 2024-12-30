package best.tigers.inventory.util;

import best.tigers.inventory.items.ParsedItem;
import best.tigers.inventory.ui.InnerInventoryScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Parser {
  public static String stripDescription(String description, String itemText) {
    if (itemText.contains(" " + description)) {
      return itemText.split(" " + description)[0];
    }
    return itemText;
  }

  public static ParsedItem parseItem(String itemString) {
    var name = itemString;
    boolean stolen = false;
    if (itemString.contains(" [STOLEN]")) {
      name = itemString.split(" [STOLEN]")[0];
      stolen = true;
    }
    name = stripDescription("sold", name);
    name = stripDescription("repaired", name);
    name = stripDescription("removed", name);
    name = stripDescription("added", name);

    return new ParsedItem(name, stolen);
  }

  public static Map<String, String> getItemMappingsForList(String listName) {
    List<String> lines = new ArrayList<>();
    try (InputStream in =
        InnerInventoryScreen.class
            .getClassLoader()
            .getResourceAsStream("lists/" + listName + ".txt")) {
      if (in == null) {
        throw new IOException("Resource not found");
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      lines.addAll(reader.lines().toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    var mappings = new HashMap<String, String>();
    for (String line : lines) {
      var split = line.split("\\|");
      mappings.put(split[0].toLowerCase(), split[1]);
    }
    return mappings;
  }

  public static boolean itemFilter(String inputString) {
    if (inputString.contains("Word of Power")) return false;
    if (inputString.contains("Objective")) return false;
    if (inputString.split(" ").length > 6) return false;
    if (inputString.split(" ").length < 3) return false;
    if (inputString.split(" ").length == 3 && inputString.toLowerCase().contains(" elf"))
      return false;
    if (inputString.contains("Blessing")) return false;
    if (inputString.contains("Curse")) return false;
    return true;
  }
}

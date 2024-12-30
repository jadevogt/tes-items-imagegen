package best.tigers.inventory.items;

import best.tigers.inventory.ui.InnerInventoryScreen;
import best.tigers.inventory.util.Post;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WeaponFactory extends SubtypedItemFactory{
  public WeaponFactory(String basePath, List<String> subTypeList) {
    super(basePath, subTypeList);
  }
  
  public static List<String> getSubTypes() {
    List<String> lines = new ArrayList<>();
    try (InputStream in =
             InnerInventoryScreen.class.getClassLoader().getResourceAsStream("lists/weapon.txt")) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      lines.addAll(reader.lines().toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return lines;
  }
  
  public WeaponFactory(String basePath) {
    super(basePath, getSubTypes());
  }

  @Override
  public boolean checkItemType(String itemName) {
    String cleanedItemName = cleanItemName(itemName);
    if (cleanedItemName.contains("bowl")) {
      return false;
    }
    if (checkValidSubType(itemName)) {
      return attemptFindIcon(itemName).isPresent();
    }
    return false;
  }

  @Override
  public double getWeightValue() {
    return Math.round(10 + Math.random() * 20);
  }

  @Override
  public double getValueModifier() {
    return 4.3;
  }

  @Override
  public double getWeightValue(String itemName) {
    return getWeightValue();
  }
}

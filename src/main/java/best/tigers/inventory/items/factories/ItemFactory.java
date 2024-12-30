package best.tigers.inventory.items.factories;

import best.tigers.inventory.items.Item;

public interface ItemFactory {
  public Item fromString(String itemString);
}

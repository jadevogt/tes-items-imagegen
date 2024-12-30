package best.tigers.inventory.items;

import best.tigers.inventory.util.Post;

import java.nio.file.Path;

public class ItemBuilder {
  private final WeaponFactory weaponFactory;
  private final ArmorFactory armorFactory;
  private final MiscItemFactory miscItemFactory;
  private final BookFactory bookFactory;

  public ItemBuilder(Path basePath) {
    this.weaponFactory = new WeaponFactory(basePath.resolve("weapon").toString());
    this.armorFactory = new ArmorFactory(basePath.resolve("armor").toString());
    this.miscItemFactory = new MiscItemFactory(basePath.resolve("misc").toString());
    this.bookFactory = new BookFactory(basePath.resolve("book").toString());
  }
  
  public ItemFactory getFactory(String itemName) {
    if (bookFactory.checkItemType(itemName)) return bookFactory;
    if (weaponFactory.checkItemType(itemName)) return weaponFactory;
    if (armorFactory.checkItemType(itemName)) return armorFactory;
    if (miscItemFactory.checkItemType(itemName)) return miscItemFactory;
    return null;
  }

  public ItemFactory getFactory(Post post) {
    return getFactory(post.content());
  }
}

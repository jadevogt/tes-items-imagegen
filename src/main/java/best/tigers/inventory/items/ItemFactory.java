package best.tigers.inventory.items;

import best.tigers.inventory.util.Post;

import java.nio.file.Path;
import java.util.Optional;

public interface ItemFactory {

  
  boolean checkItemType(String itemName);

  default String cleanItemName(String itemName) {
    return itemName.trim().toLowerCase();
  }
  
  double getValueModifier();

  default boolean matchesPath(String itemName, Path iconPath) {
    String cleanedIconName = iconPath.getFileName().toString().split("\\.")[0];
    return itemName.contains(" " + cleanedIconName + " ") || itemName.startsWith(cleanedIconName) || itemName.endsWith(cleanedIconName);
  }
  
  default int getGoldValueFromInteractions(Post post, double modifier) {
    return (int) Math.round((post.repostCount() * 10 + post.likeCount() * 10) * modifier);
  }

  default int getGoldValueFromInteractions(Post post) {
    return getGoldValueFromInteractions(post, 1.0);
  }

  double getWeightValue(String itemName);
  
  double getWeightValue();

  default Item constructItem(Post itemPost) {
    var name = itemPost.content();
    boolean stolen = false;
    if (name.contains(" [STOLEN]")) {
      name = name.replace(" [STOLEN]", "");
      stolen = true;
    }
    return new Item(name, stolen, getGoldValueFromInteractions(itemPost, getValueModifier()), getWeightValue(itemPost.content()), attemptFindIcon(itemPost.content()).orElseThrow());
  }

  Optional<Path> attemptFindIcon(String itemName);
}

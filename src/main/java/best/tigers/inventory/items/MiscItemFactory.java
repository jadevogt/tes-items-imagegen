package best.tigers.inventory.items;

import best.tigers.inventory.util.PathTools;
import best.tigers.inventory.util.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class MiscItemFactory implements ItemFactory {
  private final Path basePath;
  private final List<Path> types;

  public MiscItemFactory(String basePath) {
    Path inputPath = Path.of(basePath);
    this.basePath = inputPath.isAbsolute() ? inputPath : inputPath.toAbsolutePath();
    try (Stream<Path> files = Files.list(this.basePath)) {
      this.types = files .filter(PathTools::isNotFiltered) .toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public boolean checkItemType(String itemName) {
    final String cleanedItemName = cleanItemName(itemName);
    return types.stream().anyMatch(i -> matchesPath(cleanedItemName, i));
  }

  public Optional<Path> attemptFindIcon(String itemName) {
    final String cleanedItemName = cleanItemName(itemName);
    return types.stream().filter(i -> matchesPath(cleanedItemName, i)).max(Comparator.comparingInt(p -> (p).getFileName().toString().length()));
  }

  @Override
  public double getWeightValue() {
    return Math.round(2 + (Math.random() * 30));
  }

  @Override
  public double getValueModifier() {
    return 0.84;
  }

  @Override
  public double getWeightValue(String itemName) {
    if (itemName.toLowerCase().contains("potion")) {
      return (0.2 + (Math.random() * 2));
    }
    return getWeightValue();
  }
}

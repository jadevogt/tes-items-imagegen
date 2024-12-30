package best.tigers.inventory.items;

import best.tigers.inventory.util.PathTools;
import best.tigers.inventory.util.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

public class BookFactory implements ItemFactory {
  private final Path basePath;
  private final ArrayList<Path> icons;
  
  public BookFactory(String basePath) {
    Path inputPath = Path.of(basePath);
    this.basePath = inputPath.isAbsolute() ? inputPath : inputPath.toAbsolutePath();
    this.icons = new ArrayList<>();
    try (Stream<Path> files = Files.list(this.basePath)) {
      this.icons.addAll(files .filter(PathTools::isNotFiltered).toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public boolean checkItemType(String itemName) {
    String cleanedItemName = cleanItemName(itemName);
    return cleanedItemName.contains("spell tome") || cleanedItemName.contains("book");
  }

  @Override
  public double getValueModifier() {
    return 0.9;
  }

  @Override
  public double getWeightValue(String itemName) {
    return getWeightValue();
  }

  @Override
  public double getWeightValue() {
    return 1.0 + Math.random();
  }

  @Override
  public Optional<Path> attemptFindIcon(String itemName) {
    Collections.shuffle(icons);
    if (icons.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(icons.get(0));
  }
}

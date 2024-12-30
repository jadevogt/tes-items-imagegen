package best.tigers.inventory.items;

import best.tigers.inventory.util.PathTools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SubtypedItemFactory implements ItemFactory {
  private final Path basePath;
  private final Map<String, Path> subtypes;
  private final Map<String, String> subtypeAliases;

  boolean checkValidSubType(String itemName) {
    final String cleanedItemName = cleanItemName(itemName);
    return subtypeAliases.keySet().stream().anyMatch(cleanedItemName::contains);
  }

  public SubtypedItemFactory(String basePath, List<String> subTypeList) {
    Path inputPath = Path.of(basePath);
    this.basePath = inputPath.isAbsolute() ? inputPath : inputPath.toAbsolutePath();
    try (Stream<Path> files = Files.list(this.basePath)) {
      this.subtypes =
          files
              .filter(Files::isDirectory)
              .collect(Collectors.toMap(k -> k.getFileName().toString(), v -> v));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.subtypeAliases = new HashMap<>();
    for (String entry : subTypeList) {
      String[] k = entry.split("\\|");
      String subtype = cleanItemName(k[0]);
      String alias = cleanItemName(k[1]);
      this.subtypeAliases.put(subtype, alias);
    }
  }

  private String getAlias(String itemName) {
    List<String> aliases = subtypeAliases.keySet().stream().filter(itemName::contains).toList();
    Optional<String> alias =
        aliases.stream().sorted(Comparator.comparingInt(String::length).reversed()).findFirst();
    if (alias.isEmpty()) {
      throw new RuntimeException(itemName + " has no aliased item type available");
    }
    return subtypeAliases.get(alias.get());
  }

  public Optional<Path> attemptFindIcon(String itemName) {
    final String cleanedItemName = cleanItemName(itemName);
    String alias = getAlias(cleanedItemName);
    Path iconsPath = subtypes.get(alias);
    if (iconsPath == null) {
      throw new RuntimeException("No subtype icons path found for " + itemName + " in subtypeditemfactory");
    }
    Optional<Path> foundIcon = Optional.empty();
    try (Stream<Path> iconFiles = Files.list(iconsPath)) {
      foundIcon = iconFiles
          .filter(PathTools::isNotFiltered)
          .filter(i -> matchesPath(cleanedItemName, i)).max(Comparator.comparingInt(p -> (p).getFileName().toString().length()));
    } catch (IOException e) {
      System.err.println("Could not find icon for " + itemName);
    }
    if (foundIcon.isEmpty()) {
      try (Stream<Path> iconFiles = Files.list(iconsPath)) {
        return
            iconFiles
                .filter(PathTools::isNotFiltered)
                .filter(i -> matchesPath("generic", i))
                .max(Comparator.comparingInt(p -> (p).getFileName().toString().length()));
      } catch (IOException e) {
        System.err.println("Could not find icon for " + itemName);
      }
    }
    return foundIcon;
  }
}

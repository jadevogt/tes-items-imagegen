package best.tigers.inventory.util;

import java.nio.file.Path;

public class PathTools {
  public static boolean isNotFiltered(Path inputFile) {
    return !inputFile.getFileName().toString().startsWith(".");
  }
}

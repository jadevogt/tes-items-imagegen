package best.tigers.inventory.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindIcons {
  public static List<String> getFilesInDirectory(String directory) {
    List<String> results = new ArrayList<>();
    File[] files = new File(directory.trim()).listFiles();
    // If this pathname does not denote a directory, then listFiles() returns null.
    for (File file : files) {
      if (file.isFile() && !file.getName().contains(".DS_Store")) {
        results.add(file.getPath());
      }
    }
    return results;
  }

  public static File randomBook() {
    List<String> results = new ArrayList<>();
    File[] files = new File("./assets/icons/book").listFiles();
    // If this pathname does not denote a directory, then listFiles() returns null.
    for (File file : files) {
      if (file.isFile() && file.getName().contains("png")) {
        results.add(file.getPath());
      }
    }
    return new File(
        results.stream().skip((int) (results.size() * Math.random())).findAny().orElseThrow());
  }

  public static boolean isMajorWord(String word) {
    String[] minorWords = {
      "of", "and", "major", "minor",
    };
    return !Arrays.stream(minorWords).toList().contains(word);
  }

  public static boolean numMatchingWords(String one, String path, int threshold) {
    var splitOne =
        Arrays.stream(one.split(" "))
            .map(String::toLowerCase)
            .filter(FindIcons::isMajorWord)
            .toList();
    String[] pieces = path.split("/");
    path = pieces[pieces.length - 1].split("\\.")[0];
    var splitTwo =
        Arrays.stream(path.split(" "))
            .map(String::toLowerCase)
            .filter(FindIcons::isMajorWord)
            .toList();
    int total = 0;
    for (String word : splitOne) {
      if (splitTwo.contains(word)) {
        total++;
      }
    }
    return total >= threshold;
  }

  public static File locatedMiscIcon(String itemName) {
    String directoryName = "assets/icons/misc";
    var matches = getFilesInDirectory(directoryName);
    var matchesVeryGood = matches.stream().filter(i -> numMatchingWords(itemName, i, 2)).toList();
    var matchesOkay = matches.stream().filter(i -> numMatchingWords(itemName, i, 1)).toList();
    for (var match : matchesVeryGood) {
      return new File(match);
    }
    for (var match : matchesOkay) {
      return new File(match);
    }
    return new File(directoryName + "/" + "generic.png");
  }

  public static File locatedIcon(String listName, String itemName) {
    String directoryName = null;
    var mappings = Parser.getItemMappingsForList(listName);
    var excludedName = "";
    for (var key : mappings.keySet()) {
      if (itemName.contains(key)) {
        directoryName = "./assets/icons/" + listName.trim() + "/" + mappings.get(key).trim();
      }
    }
    if (directoryName == null) {
      return null;
    }
    var matches = getFilesInDirectory(directoryName);
    var matchesVeryGood = matches.stream().filter(i -> numMatchingWords(itemName, i, 2)).toList();
    var matchesOkay = matches.stream().filter(i -> numMatchingWords(itemName, i, 1)).toList();
    for (var match : matchesVeryGood) {
      return new File(match);
    }
    for (var match : matchesOkay) {
      return new File(match);
    }
    return new File(directoryName + "/" + "generic.png");
  }
}

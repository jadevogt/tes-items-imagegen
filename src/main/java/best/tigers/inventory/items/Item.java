package best.tigers.inventory.items;

import java.nio.file.Path;

public record Item(String name, boolean stolen, int goldValue, double weightValue, Path icon) {}

package best.tigers.inventory.items;

public interface Item {
  public String getName();

  public Integer getGoldValue();

  public Double getWeightValue();

  public String getIconPath();

  public Boolean isStolen();

  public Boolean isEnchanted();
}

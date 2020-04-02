package models;

public final class PartInventory {

  /** The {@link Part} concerned. */
  private Part part;
  /** The {@link Locker} where the parts are stored. */
  private Locker locker;
  /** The condition of the parts. */
  private PartCondition partCondition;
  /** The amount of parts. */
  private int amount;

}

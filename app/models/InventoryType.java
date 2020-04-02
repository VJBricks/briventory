package models;

public final class InventoryType {

  /** The id of this {@link InventoryType}. */
  private Long id;
  /** The name. */
  private String name;
  /** The description. */
  private String description;
  /** The parts are sellable. */
  private boolean sellable;
  /** The parts are sellable but are currently in the stock room. */
  private boolean stock;

}

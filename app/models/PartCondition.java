package models;

/** {@code PartCondition} represents the condition of parts. */
public final class PartCondition {

  /** The id of this {@link PartCondition}. */
  private Long id;
  /** The name. */
  private String name;
  /** If {@code true}, the parts are new and never be assembled. */
  private boolean newPart;
  /** The description. */
  private String description;

}

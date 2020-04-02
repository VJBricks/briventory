package models;

/** {@code Locker} represents a <em>space</em> into a container. */
public final class Locker {

  /** The id of this {@code Locker}. */
  private Long id;
  /** The associated {@link Container}. */
  private Container container;
  /** The order of the locker into the container. */
  private short order;
  /** If {@code true}, this locker contains the parts from the same design, regardless of the color. */
  private boolean colorMixed;

}

package models;

/** {@code ContainerType} represents the type of a container. */
public class ContainerType {

  /** The id of this {@link ContainerType}. */
  private Long id;
  /** The name. */
  private String name;
  /** The minimal amount of locker this container has. */
  private short minLockers;
  /** The maximal amount of locker this container has. */
  private short maxLockers;
  /** The numbering format string. */
  private String numberingFormat;

}

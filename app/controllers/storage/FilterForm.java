package controllers.storage;

public class FilterForm {
  /** If {@code true}, shared containers should be included. */
  private boolean sharedContainers;
  /** If {@code true}, private containers should be included. */
  private boolean privateContainers;
  /** The identifier of the container type, or {@code null} to ignore the size from the filter. */
  private Long idContainerType;
  /** The identifier of the locker size, or {@code null} to ignore the size from the filter. */
  private Long idLockerSize;

  /** @return {@code true} to include the shared containers. */
  public boolean getSharedContainers() {
    return sharedContainers;
  }

  /**
   * Sets the inclusion of shared containers in the result.
   *
   * @param sharedContainers {@code true} to include shared containers.
   */
  public void setSharedContainers(final boolean sharedContainers) {
    this.sharedContainers = sharedContainers;
  }

  /** @return {@code true} to include the private containers. */
  public boolean getPrivateContainers() {
    return privateContainers;
  }

  /**
   * Sets the inclusion of private containers in the result.
   *
   * @param privateContainers {@code true} to include private containers.
   */
  public void setPrivateContainers(final boolean privateContainers) {
    this.privateContainers = privateContainers;
  }

  /** @return the identifier of the container type to filter on, or {@code null} to ignore this filter. */
  public Long getIdContainerType() {
    return idContainerType;
  }

  /**
   * Sets the identifier of the container type to filter on. A {@code null} value makes this filter to be ignored.
   *
   * @param idContainerType the identifier of the container type to filter on, or {@code null} to ignore this filter.
   */
  public void setIdContainerType(final Long idContainerType) {
    this.idContainerType = idContainerType;
  }

  /** @return the identifier of the locker size to filter on, or {@code null} to ignore this filter. */
  public Long getIdLockerSize() {
    return idLockerSize;
  }

  /**
   * Sets the identifier of the locker size to filter on. A {@code null} value makes this filter to be ignored.
   *
   * @param idLockerSize the identifier of the locker size to filter on, or {@code null} to ignore this filter.
   */
  public void setIdLockerSize(final Long idLockerSize) {
    this.idLockerSize = idLockerSize;
  }

}

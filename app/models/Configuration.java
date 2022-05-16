package models;

import orm.Model;

public final class Configuration extends Model {
  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The revision of the database. */
  private final String databaseRevision;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link Configuration} instance.
   *
   * @param databaseRevision the {@link String} representing the semantic versioning of the database.
   */
  public Configuration(final String databaseRevision) {
    this.databaseRevision = databaseRevision;
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the {@link String} representing the semantic versioning of the database. */
  public String getDatabaseRevision() { return databaseRevision; }

}

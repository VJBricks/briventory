package models;

import repositories.Repository;

/**
 * {@code Entity} is the base class for every entity handled by the ORM.
 *
 * @param <R> the precise type of {@link Repository}.
 */
public abstract class Entity<R extends Repository> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link Repository} that created this {@link Entity}. */
  private final R repository;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link Entity}.
   *
   * @param repository the {@link Repository}.
   */
  protected Entity(final R repository) {
    this.repository = repository;
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the {@link Repository}. */
  protected final R getRepository() { return repository; }

}

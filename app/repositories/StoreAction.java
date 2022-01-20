package repositories;

import models.Entity;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

/**
 * This subclass of {@link EntityAction} will store the {@link Entity} into the database.
 *
 * @param <E> the precise subtype of the {@link Entity}.
 * @param <R> the precise subtype of the {@link org.jooq.Record}.
 */
public final class StoreAction<E extends Entity<? extends Repository>, R extends UpdatableRecord<R>>
    implements EntityAction {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link StorableEntityHandler} instance, that will perform the storage process. */
  private final StorableEntityHandler<E, R> storableEntityHandler;
  /** The {@link E} to store. */
  private final E entity;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link StoreAction}.
   *
   * @param storableEntityHandler the {@link StorableEntityHandler} instance, that will perform the storage process.
   * @param entity the {@link E} to store.
   */
  public StoreAction(final StorableEntityHandler<E, R> storableEntityHandler, final E entity) {
    this.storableEntityHandler = storableEntityHandler;
    this.entity = entity;
  }

  // *******************************************************************************************************************
  // EntityAction Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public void execute(final DSLContext dslContext) { storableEntityHandler.store(dslContext, entity); }

}

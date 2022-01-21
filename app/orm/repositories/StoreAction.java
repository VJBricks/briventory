package orm.repositories;

import orm.models.Entity;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.IStorableEntity;

/**
 * This subclass of {@link EntityAction} will store the {@link Entity} into the database.
 *
 * @param <V> the type of the errors instances, produced during the validation of the entity.
 * @param <E> the precise subtype of the {@link Entity}.
 * @param <R> the precise subtype of the {@link org.jooq.Record}.
 */
public final class StoreAction<V, E extends Entity<? extends Repository> & IStorableEntity<V>,
                                  R extends UpdatableRecord<R>>
    implements EntityAction {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link StorableEntityHandler} instance, that will perform the storage process. */
  private final StorableEntityHandler<V, E, R> storableEntityHandler;
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
  public StoreAction(final StorableEntityHandler<V, E, R> storableEntityHandler, final E entity) {
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

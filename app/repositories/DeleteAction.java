package repositories;

import models.Entity;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

/**
 * This subclass of {@link EntityAction} will delete the {@link Entity} from the database.
 *
 * @param <E> the precise subtype of the {@link Entity}.
 * @param <R> the precise subtype of the {@link org.jooq.Record}.
 */
public final class DeleteAction<E extends Entity<? extends Repository>, R extends UpdatableRecord<R>>
    implements EntityAction {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link DeletableEntityHandler} instance, that will perform the deletion process. */
  private final DeletableEntityHandler<E, R> deletableEntityHandler;
  /** The {@link E} to delete. */
  private final E entity;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link StoreAction}.
   *
   * @param deletableEntityHandler the {@link DeletableEntityHandler} instance, that will perform the deletion process.
   * @param entity the {@link E} to delete.
   */
  public DeleteAction(final DeletableEntityHandler<E, R> deletableEntityHandler, final E entity) {
    this.deletableEntityHandler = deletableEntityHandler;
    this.entity = entity;
  }

  // *******************************************************************************************************************
  // EntityAction Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public void execute(final DSLContext dslContext) {
    deletableEntityHandler.delete(dslContext, entity);
  }

}

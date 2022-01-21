package orm.repositories;

import database.BriventoryDB;
import orm.models.Entity;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.IStorableEntity;

import java.util.List;
import java.util.function.Function;

public abstract class Repository {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  protected Repository(final BriventoryDB briventoryDB) {
    this.briventoryDB = briventoryDB;
  }

  // *******************************************************************************************************************
  // Queries
  // *******************************************************************************************************************

  /**
   * Executes a query.
   *
   * @param queryDSL the query through a {@link DSLContext} that has not been yet bind to the database.
   * @param <T> <T> the type of the result instance.
   *
   * @return an instance of {@link T}.
   */
  protected final <T> T query(final Function<DSLContext, T> queryDSL) {
    return briventoryDB.query(queryDSL);
  }

  // *******************************************************************************************************************
  // Storage
  // *******************************************************************************************************************

  /**
   * Stores the {@link E} into the database.
   *
   * @param storableEntityHandler the {@link StorableEntityHandler} that will handle the storage process.
   * @param entity the {@link E} to store.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <E> the precise subtype of the {@link Entity}.
   * @param <R> the precise subtype of the {@link Record}.
   */
  protected final <V, E extends Entity<? extends Repository> & IStorableEntity<V>,
                      R extends UpdatableRecord<R>> void store(
      final StorableEntityHandler<V, E, R> storableEntityHandler, final E entity) {
    briventoryDB.store(storableEntityHandler, entity);
  }

  // *******************************************************************************************************************
  // Deletion
  // *******************************************************************************************************************

  /**
   * Deletes the {@link E} from the database.
   *
   * @param deletableEntityHandler the {@link DeletableEntityHandler} that will handle the deletion process.
   * @param entity the {@link E} to delete.
   * @param <E> the precise subtype of the {@link Entity}.
   * @param <R> the precise subtype of the {@link Record}.
   */
  public <E extends Entity<? extends Repository>, R extends UpdatableRecord<R>> void delete(
      final DeletableEntityHandler<E, R> deletableEntityHandler,
      final E entity) {
    briventoryDB.delete(deletableEntityHandler, entity);
  }

  /**
   * Deletes a {@link List} of {@link E} instances from the database.
   *
   * @param deletableEntityHandler the {@link DeletableEntityHandler} that will handle the deletion process.
   * @param entities the {@link List} of {@link E} instances to delete.
   * @param <E> the precise subtype of the {@link Entity}.
   * @param <R> the precise subtype of the {@link Record}.
   */
  public <E extends Entity<? extends Repository>, R extends UpdatableRecord<R>> void delete(
      final DeletableEntityHandler<E, R> deletableEntityHandler,
      final List<E> entities) {
    briventoryDB.delete(deletableEntityHandler, entities);
  }

}

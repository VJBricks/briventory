package orm.repositories;

import database.BriventoryDBException;
import orm.models.Entity;
import org.jooq.DSLContext;
import org.jooq.RecordUnmapper;
import org.jooq.Table;
import org.jooq.UpdatableRecord;

import java.util.LinkedList;
import java.util.List;

/**
 * {@code DeletableEntityHandler} handles the deletion of an {@link Entity}. The process is composed by the following
 * steps :
 * <ol>
 *   <li>{@code shallDelete}: to perform checks, that will determine if the entity can be deleted. For example, you
 *   will check if a reference constraint will be violated, before the database will tell it to you.</li>
 *   <li>{@code executePreDeleteActions}: before deleting the entity, this allow you to execute {@link EntityAction} on
 *   related entities.</li>
 *   <li>{@code store}: performs the deletion query.</li>
 *   <li>{@code executePostDeleteActions}: after deleting the entity, this allow you to execute {@link EntityAction} on
 *   related entities.</li>
 * </ol>
 *
 * @param <E> the precise subtype of the {@link Entity}.
 * @param <R> the precise subtype of the {@link org.jooq.Record}.
 */
public abstract class DeletableEntityHandler<E extends Entity<? extends Repository>, R extends UpdatableRecord<R>> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link RecordUnmapper} instance, to convert the {@link E} into {@link R}. */
  private final RecordUnmapper<E, R> unmapper;
  /** The {@link Table} that correspond to the instance of {@link R}. */
  private final Table<R> table;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link DeletableEntityHandler} instance.
   *
   * @param unmapper the {@link RecordUnmapper} instance, to convert the {@link E} into {@link R}.
   * @param table the {@link Table} that correspond to the instance of {@link R}.
   */
  protected DeletableEntityHandler(final RecordUnmapper<E, R> unmapper, final Table<R> table) {
    this.unmapper = unmapper;
    this.table = table;
  }

  // *******************************************************************************************************************
  // Deletion process
  // *******************************************************************************************************************

  /**
   * Shall the given entity be deleted ? By default, this method returns {@code true}. The {@link DSLContext} can be
   * used to perform checks in the database.
   * <p>If the entity cannot be deleted, a {@link BriventoryDBException} will be thrown during the deletion
   * process.</p>
   *
   * @param dslContext the {@link DSLContext}.
   * @param entity the {@link E} being stored.
   *
   * @return {@code true} if the deletion can be done, otherwise {@code false}. By default, this method returns {@code
   * true}.
   */
  protected boolean shallDelete(final DSLContext dslContext, final E entity) { return true; }

  /**
   * Provides all the {@link EntityAction}s on related entities, that will be executed before the deletion of the
   * current entity. The actions are executed in the same order as they are defined in the list.
   *
   * @param entity the entity that is going to be deleted.
   *
   * @return a {@link List} of {@link EntityAction} instance. By default, this method returns an empty list.
   */
  protected List<EntityAction> getPreDeleteActions(final E entity) {
    return new LinkedList<>();
  }

  /**
   * Provides all the {@link EntityAction}s on related entities, that will be executed after the deletion of the current
   * entity. The actions are executed in the same order as they are defined in the list.
   *
   * @param entity the entity that have been deleted.
   *
   * @return a {@link List} of {@link EntityAction} instance. By default, this method returns an empty list.
   */
  protected List<EntityAction> getPostDeleteActions(final E entity) {
    return new LinkedList<>();
  }

  /**
   * Deletes the {@link E} from the database, by applying the deletion process.
   *
   * @param dslContext the {@link DSLContext}.
   * @param entity the {@link E} to delete.
   */
  public final void delete(final DSLContext dslContext, final E entity) {
    dslContext.configuration().set(unmapper);
    final R modelAsRecord = dslContext.newRecord(table, entity);

    if (!shallDelete(dslContext, entity))
      throw new BriventoryDBException(String.format("Deletion of entity %s is not allowed", modelAsRecord));

    for (EntityAction entityAction : getPreDeleteActions(entity))
      entityAction.execute(dslContext);

    modelAsRecord.delete();

    for (EntityAction entityAction : getPostDeleteActions(entity))
      entityAction.execute(dslContext);
  }

}

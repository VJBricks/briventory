package orm.repositories;

import database.BriventoryDBException;
import orm.models.Entity;
import org.jooq.DSLContext;
import org.jooq.RecordUnmapper;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import orm.models.IStorableEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * {@code StorableEntityHandler} handles the storage of an {@link Entity}. The process is composed by the following
 * steps :
 * <ol>
 *   <li>{@code shallStore}: to perform checks, that will determine if the entity can be stored. For example, you
 *   will check if all mandatory attributes are correctly set.</li>
 *   <li>{@code executePreStoreActions}: before storing the entity, this allow you to execute {@link EntityAction} on
 *   related entities.</li>
 *   <li>{@code store}: performs the storage query, an {@code insert} or an {@code update}, depending on the entity's
 *   state.</li>
 *   <li>{@code executePostStoreActions}: after storing the entity, this allow you to execute {@link EntityAction} on
 *   related entities.</li>
 * </ol>
 *
 * @param <V> the type of the errors instances, produced during the validation of the entity.
 * @param <E> the precise subtype of the {@link Entity}.
 * @param <R> the precise subtype of the {@link org.jooq.Record}.
 */
public abstract class StorableEntityHandler<V, E extends Entity<? extends Repository> & IStorableEntity<V>,
                                               R extends UpdatableRecord<R>> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link RecordUnmapper} instance, to convert the {@link E} into {@link R}. */
  private final RecordUnmapper<E, R> unmapper;
  /** The {@link EntityReloader} instance, to reload the {@link E}, by getting data from an instance of {@link R}. */
  private final EntityReloader<R, E> reloader;
  /** The {@link Table} that correspond to the instance of {@link R}. */
  private final Table<R> table;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link StorableEntityHandler} instance.
   *
   * @param unmapper the {@link RecordUnmapper} instance, to convert the {@link E} into {@link R}.
   * @param reloader the {@link EntityReloader} instance, to reload the {@link E}, by getting data from an instance of
   * {@link R}.
   * @param table the {@link Table} that correspond to the instance of {@link R}.
   */
  protected StorableEntityHandler(final RecordUnmapper<E, R> unmapper, final EntityReloader<R, E> reloader,
                                  final Table<R> table) {
    this.unmapper = unmapper;
    this.reloader = reloader;
    this.table = table;
  }

  // *******************************************************************************************************************
  // Storage process
  // *******************************************************************************************************************

  /**
   * Shall the given entity be stored ? By default, this method returns {@code true}. The {@link DSLContext} can be used
   * to perform checks in the database.
   * <p>If the entity cannot be stored, a {@link BriventoryDBException} will be thrown during the storage
   * process.</p>
   *
   * @param dslContext the {@link DSLContext}.
   * @param entity the {@link E} being stored.
   *
   * @return a {@link List} of {@link V} instances, or an empty list if there is no error. By default, this method
   * returns the result of {@link IStorableEntity#isValid()}.
   */
  protected List<V> shallStore(final DSLContext dslContext, final E entity) { return entity.isValid(); }

  /**
   * Provides all the {@link EntityAction}s on related entities, that will be executed before the storage of the current
   * entity. The actions are executed in the same order as they are defined in the list.
   *
   * @param entity the entity that is going to be stored.
   *
   * @return a {@link List} of {@link EntityAction} instance. By default, this method returns an empty list.
   */
  protected List<EntityAction> getPreStoreActions(final E entity) {
    return new LinkedList<>();
  }

  /**
   * Provides all the {@link EntityAction}s on related entities, that will be executed after the storage of the current
   * entity. The actions are executed in the same order as they are defined in the list.
   *
   * @param entity the entity that have been stored.
   *
   * @return a {@link List} of {@link EntityAction} instance. By default, this method returns an empty list.
   */
  protected List<EntityAction> getPostStoreActions(final E entity) {
    return new LinkedList<>();
  }

  /**
   * Stores the {@link E} into the database, by applying the storage process.
   *
   * @param dslContext the {@link DSLContext}.
   * @param entity the {@link E} to store.
   */
  public final void store(final DSLContext dslContext, final E entity) {
    dslContext.configuration().set(unmapper);
    final R modelAsRecord = dslContext.newRecord(table, entity);

    List<V> shallStoreResult = shallStore(dslContext, entity);
    if (!shallStoreResult.isEmpty()) {
      StringBuilder stringBuilder = new StringBuilder();
      for (V entry : shallStoreResult)
        stringBuilder.append(entry)
                     .append(System.lineSeparator());
      throw new BriventoryDBException(
          String.format("Persistence of entity %s is not allowed for the following reasons:%s%s",
                        entity, System.lineSeparator(), stringBuilder));
    }

    for (EntityAction entityAction : getPreStoreActions(entity))
      entityAction.execute(dslContext);

    if (modelAsRecord.changed()) {
      modelAsRecord.merge();

      reloader.reload(modelAsRecord, entity);
    }

    for (EntityAction entityAction : getPostStoreActions(entity))
      entityAction.execute(dslContext);
  }

}

package orm;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.Select;
import org.jooq.UpdatableRecord;
import org.jooq.lambda.tuple.Tuple2;
import orm.models.PersistableModel1;
import orm.models.PersistableModel2;
import orm.models.PersistableModel3;
import orm.models.PersistableModel4;
import orm.models.PersistableModel5;
import orm.models.ValidatableModel;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * The {@code Repository} class is the base class for all repositories. In the GORM architecture, a repository is the
 * entry point for all operations related to the database.
 * <p>A repository is constraint to create only one {@link Model}. This behaviour has been introduced to organize the
 * code, to minimize the confusion where it is possible to retrieve instances of a particular model. This do not means
 * that a repository will not been able to export other models. It only fixes the creation of a particular model.</p>
 * <p>The {@code Repository} class provides various helper to perform queries and data manipulation. Those helpers use
 * a {@link PersistenceContext} to perform the operations.</p>
 *
 * @param <M> the specific type of {@link Model} that the implementation of {@code Repository} with create.
 *
 * @see Mapper
 * @see PersistenceContext
 */
public abstract class Repository<M extends Model> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link PersistenceContext} instance. */
  private final PersistenceContext persistenceContext;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link Repository}.
   *
   * @param persistenceContext the {@link PersistenceContext}.
   */
  protected Repository(final PersistenceContext persistenceContext) {
    this.persistenceContext = persistenceContext;
  }

  // *******************************************************************************************************************
  // Queries Helper Methods
  // *******************************************************************************************************************

  /**
   * Fetches data from the database and returns the result as a {@link List} of instances corresponding to the type
   * {@link M}.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param query the query that will be executed into the database.
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return a {@link List} of {@link M} instances.
   */
  protected final <R extends Record, F extends Mapper<R, M>> List<M> fetch(
      final F factory, final Function<DSLContext, ResultQuery<R>> query) {
    return persistenceContext.fetch(factory, query);
  }

  /**
   * Fetches data from the database and returns the result as a {@link List} of instances corresponding to the type
   * {@link M}.
   *
   * @param query the query that will be executed into the database.
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return a {@link List} of {@link M} instances.
   */
  protected final <R extends Record, F extends Mapper<R, M>> List<M> fetch(
      final Function<DSLContext, Tuple2<F, ResultQuery<R>>> query) {
    return persistenceContext.fetch(query);
  }

  /**
   * Fetches all the queries provided and returns a {@link List}, containing all instances.
   *
   * @param queries the queries that will be executed into the database.
   *
   * @return a {@link List} of {@link M} instances.
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  protected final List<M> unionAll(
      final Function<DSLContext,
                        Tuple2<? extends Mapper<? extends Record, ? extends M>,
                                  ? extends ResultQuery<? extends Record>>>... queries) {
    return persistenceContext.unionAll(queries);
  }

  /**
   * Fetches the only one record of the query.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param query the query that will be executed into the database.
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return the instance of type {@link M}, or {@code null} if the query has no row.
   *
   * @throws org.jooq.exception.DataAccessException if something went wrong executing the query.
   * @throws org.jooq.exception.TooManyRowsException if the query returned more than one record.
   * @see ResultQuery#fetchOne()
   */
  protected <R extends Record, F extends Mapper<R, M>> M fetchOne(
      final F factory, final Function<DSLContext, ResultQuery<R>> query) {
    return persistenceContext.fetchOne(factory, query);
  }

  /**
   * Fetches the only one record of the query and returns it as an {@link Optional}.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param query the query that will be executed into the database.
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return the instance of type {@link M}, or {@code null} if the query has no row.
   *
   * @throws org.jooq.exception.DataAccessException if something went wrong executing the query.
   * @throws org.jooq.exception.TooManyRowsException if the query returned more than one record.
   * @see ResultQuery#fetchOptional()
   */
  protected <R extends Record, F extends Mapper<R, M>> Optional<M> fetchOptional(
      final F factory, final Function<DSLContext, ResultQuery<R>> query) {
    return persistenceContext.fetchOptional(factory, query);
  }

  /**
   * Fetches the first record of the query. If the query does not return any record, a
   * {@link org.jooq.exception.NoDataFoundException} will be thrown.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param query the query that will be executed into the database.
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return the instance of type {@link M}.
   *
   * @throws org.jooq.exception.DataAccessException if something went wrong executing the query.
   * @throws org.jooq.exception.TooManyRowsException if the query returned more than one record.
   * @throws org.jooq.exception.NoDataFoundException if the query returned no records.
   * @see ResultQuery#fetchSingle()
   */
  protected <R extends Record, F extends Mapper<R, M>> M fetchSingle(
      final F factory, final Function<DSLContext, ResultQuery<R>> query) {
    return persistenceContext.fetchSingle(factory, query);
  }

  /**
   * Fetches the first record of the query. If the query does not return any record, a
   * {@link org.jooq.exception.NoDataFoundException} will be thrown.
   *
   * @param clazz corresponding to {@link T}.
   * @param query the query that will be executed into the database.
   * @param <T> the return type.
   * @param <R> the specific implementation, extending {@link Record}.
   *
   * @return the instance of type {@link T}.
   *
   * @throws org.jooq.exception.DataAccessException if something went wrong executing the query.
   * @throws org.jooq.exception.TooManyRowsException if the query returned more than one record.
   * @throws org.jooq.exception.NoDataFoundException if the query returned no records.
   * @see ResultQuery#fetchSingleInto(Class)
   */
  protected <T, R extends Record> T fetchSingleInto(final Class<T> clazz,
                                                    final Function<DSLContext, ResultQuery<R>> query) {
    return persistenceContext.fetchSingleInto(clazz, query);
  }

  /**
   * Executes a {@code select exists} query, using the query provided as the sub-query.
   *
   * @param query the query that will be used as the sub-query.
   *
   * @return {@code true} if the sub-query exports a least one record, otherwise {@code false}.
   *
   * @see DSLContext#fetchExists(Select)
   */
  protected boolean exists(final Function<DSLContext, Select<?>> query) {
    return persistenceContext.exists(query);
  }

  // *******************************************************************************************************************
  // Persistence
  // *******************************************************************************************************************

  protected final <V, R extends UpdatableRecord<R>,
                      P extends PersistableModel1<R> & ValidatableModel<V>> void persist(final P persistableModel) {
    persistenceContext.persist(persistableModel);
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      P extends PersistableModel2<R1, R2> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persistenceContext.persist(persistableModel);
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      R3 extends UpdatableRecord<R3>,
                      P extends PersistableModel3<R1, R2, R3> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persistenceContext.persist(persistableModel);
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      R3 extends UpdatableRecord<R3>,
                      R4 extends UpdatableRecord<R4>,
                      P extends PersistableModel4<R1, R2, R3, R4> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persistenceContext.persist(persistableModel);
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      R3 extends UpdatableRecord<R3>,
                      R4 extends UpdatableRecord<R4>,
                      R5 extends UpdatableRecord<R5>,
                      P extends PersistableModel5<R1, R2, R3, R4, R5> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persistenceContext.persist(persistableModel);
  }

  // *******************************************************************************************************************
  // Deletion
  // *******************************************************************************************************************

  /**
   * Deletes the {@link E} from the database.
   *
   * @param deletableEntityHandler the {@link DeletableEntityHandler} that will handle the deletion process.
   * @param entity the {@link E} to delete.
   * @param <E> the precise subtype of the {@link Model}.
   * @param <R> the precise subtype of the {@link Record}.
   */
  /*public <E extends Model, R extends UpdatableRecord<R>> void delete(
      final DeletableEntityHandler<E, R> deletableEntityHandler,
      final E entity) {
    persistenceContext.delete(deletableEntityHandler, entity);
  }*/

  /**
   * Deletes a {@link List} of {@link E} instances from the database.
   *
   * @param deletableEntityHandler the {@link DeletableEntityHandler} that will handle the deletion process.
   * @param entities the {@link List} of {@link E} instances to delete.
   * @param <E> the precise subtype of the {@link Model}.
   * @param <R> the precise subtype of the {@link Record}.
   */
  /*public <E extends Model, R extends UpdatableRecord<R>> void delete(
      final DeletableEntityHandler<E, R> deletableEntityHandler,
      final List<E> entities) {
    persistenceContext.delete(deletableEntityHandler, entities);
  }*/

}

package orm;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.UpdatableRecord;
import org.jooq.lambda.tuple.Tuple2;
import orm.models.IValidatableModel;
import orm.repositories.DeletableEntityHandler;
import orm.repositories.StorableEntityHandler;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class PersistenceContext {

  // *******************************************************************************************************************
  // Abstract Methods for the DSL Context configuration
  // *******************************************************************************************************************

  /** @return the {@link SQLDialect}. */
  protected abstract SQLDialect getDialect();

  // *******************************************************************************************************************
  // Abstract Methods for General Data
  // *******************************************************************************************************************

  /** @return the name of the database. */
  protected abstract String getDatabaseName();

  /** @return the URL of the database. */
  protected abstract String getDatabaseURL();

  // *******************************************************************************************************************
  // Abstract Methods, relative to queries
  // *******************************************************************************************************************

  /**
   * Fetches data from the database and returns the result as a {@link List} of instances corresponding to the type
   * {@link M}.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param query the query that will be executed into the database.
   * @param <M> the specific implementation, extending {@link Model}.
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return a {@link List} of {@link M} instances.
   */
  protected abstract <M extends Model, R extends Record, F extends Mapper<R, M>> List<M> fetch(
      F factory, Function<DSLContext, ResultQuery<R>> query);

  /**
   * Fetches data from the database and returns the result as a {@link List} of instances corresponding to the type
   * {@link M}.
   *
   * @param query the query that will be executed into the database.
   * @param <M> the specific implementation, extending {@link Model}.
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return a {@link List} of {@link M} instances.
   */
  protected abstract <R extends Record, M extends Model, F extends Mapper<R, M>> List<M> fetch(
      Function<DSLContext, Tuple2<F, ResultQuery<R>>> query);

  /**
   * Fetches all the queries provided and returns a {@link List}, containing all instances.
   *
   * @param queries the queries that will be executed into the database.
   * @param <M> the specific implementation, extending {@link Model}.
   *
   * @return a {@link List} of {@link M} instances.
   */
  protected abstract <M extends Model> List<M> unionAll(
      Function<DSLContext,
                  Tuple2<? extends Mapper<? extends Record, ? extends M>,
                            ? extends ResultQuery<? extends Record>>>[] queries);

  /**
   * Fetches the only one record of the query.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param query the query that will be executed into the database.
   * @param <M> the specific implementation, extending {@link Model}.
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return the instance of type {@link M}, or {@code null} if the query has no row.
   *
   * @throws org.jooq.exception.DataAccessException if something went wrong executing the query.
   * @throws org.jooq.exception.TooManyRowsException if the query returned more than one record.
   * @see ResultQuery#fetchOne()
   */
  protected abstract <M extends Model, R extends Record, F extends Mapper<R, M>> M fetchOne(
      F factory, Function<DSLContext, ResultQuery<R>> query);

  /**
   * Fetches the only one record of the query and returns it as an {@link Optional}.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param query the query that will be executed into the database.
   * @param <M> the specific implementation, extending {@link Model}.
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return the instance of type {@link M}, or {@code null} if the query has no row.
   *
   * @throws org.jooq.exception.DataAccessException if something went wrong executing the query.
   * @throws org.jooq.exception.TooManyRowsException if the query returned more than one record.
   * @see ResultQuery#fetchOptional()
   */
  protected abstract <M extends Model, R extends Record, F extends Mapper<R, M>> Optional<M> fetchOptional(
      F factory, Function<DSLContext, ResultQuery<R>> query);

  /**
   * Fetches the first record of the query. If the query does not return any record, a
   * {@link org.jooq.exception.NoDataFoundException} will be thrown.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param query the query that will be executed into the database.
   * @param <M> the specific implementation, extending {@link Model}.
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
  protected abstract <M extends Model, R extends Record, F extends Mapper<R, M>> M fetchSingle(
      F factory, Function<DSLContext, ResultQuery<R>> query);

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
  protected abstract <T, R extends Record> T fetchSingleInto(Class<T> clazz,
                                                             Function<DSLContext, ResultQuery<R>> query);

  /**
   * Executes a {@code select exists} query, using the query provided as the sub-query.
   *
   * @param query the query that will be used as the sub-query.
   *
   * @return {@code true} if the sub-query exports a least one record, otherwise {@code false}.
   *
   * @see DSLContext#fetchExists(Select)
   */
  protected abstract boolean exists(Function<DSLContext, Select<?>> query);

  // *******************************************************************************************************************
  // Abstract Methods, relative to storage (insert or update)
  // *******************************************************************************************************************

  /**
   * Stores the {@link E} into the database.
   *
   * @param storableEntityHandler the {@link StorableEntityHandler} that will handle the storage process.
   * @param entity the {@link E} to store.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <E> the precise subtype of the {@link Model}.
   * @param <R> the precise subtype of the {@link org.jooq.Record}.
   */
  protected abstract <V, E extends Model & IValidatableModel<V>, R extends UpdatableRecord<R>> void store(
      StorableEntityHandler<V, E, R> storableEntityHandler, E entity);

  // *******************************************************************************************************************
  // Abstract Methods, relative to deletion
  // *******************************************************************************************************************

  /**
   * Deletes the {@link E} from the database.
   *
   * @param deletableEntityHandler the {@link DeletableEntityHandler} that will handle the deletion process.
   * @param entity the {@link E} to delete.
   * @param <E> the precise subtype of the {@link Model}.
   * @param <R> the precise subtype of the {@link org.jooq.Record}.
   */
  protected abstract <E extends Model, R extends UpdatableRecord<R>> void delete(
      DeletableEntityHandler<E, R> deletableEntityHandler,
      E entity);

  protected abstract <E extends Model, R extends UpdatableRecord<R>> void delete(
      DeletableEntityHandler<E, R> deletableEntityHandler,
      List<E> entities);

}

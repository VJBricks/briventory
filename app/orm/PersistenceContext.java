package orm;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.UpdatableRecord;
import org.jooq.lambda.tuple.Tuple2;
import orm.models.PersistableModel;
import orm.models.PersistableModel1;
import orm.models.PersistableModel2;
import orm.models.PersistableModel3;
import orm.models.PersistableModel4;
import orm.models.PersistableModel5;
import orm.models.ValidatableModel;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
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
   * <p><strong>Note</strong>: the implementation of this method can implies to suppress uncheck cast warnings.</p>
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
  // Methods relative to persistence (insert or update)
  // *******************************************************************************************************************

  /**
   * Stores the {@link P} into the database.
   *
   * @param dslContext the {@link DSLContext}.
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R> the precise subtype of the {@link UpdatableRecord}.
   */
  protected final <V, R extends UpdatableRecord<R>,
                      P extends PersistableModel1<R> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist(dslContext, ModelPersistorFactory.of(persistableModel));
  }

  /**
   * Stores the {@link P} into the database.
   *
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R> the precise subtype of the {@link UpdatableRecord}.
   */
  protected final <V, R extends UpdatableRecord<R>,
                      P extends PersistableModel1<R> & ValidatableModel<V>> void persist(final P persistableModel) {
    persist(ModelPersistorFactory.of(persistableModel));
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      P extends PersistableModel2<R1, R2> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist(dslContext, ModelPersistorFactory.of(persistableModel));
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      P extends PersistableModel2<R1, R2> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persist(ModelPersistorFactory.of(persistableModel));
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      R3 extends UpdatableRecord<R3>,
                      P extends PersistableModel3<R1, R2, R3> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist(dslContext, ModelPersistorFactory.of(persistableModel));
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      R3 extends UpdatableRecord<R3>,
                      P extends PersistableModel3<R1, R2, R3> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persist(ModelPersistorFactory.of(persistableModel));
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      R3 extends UpdatableRecord<R3>,
                      R4 extends UpdatableRecord<R4>,
                      P extends PersistableModel4<R1, R2, R3, R4> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist(dslContext, ModelPersistorFactory.of(persistableModel));
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      R3 extends UpdatableRecord<R3>,
                      R4 extends UpdatableRecord<R4>,
                      P extends PersistableModel4<R1, R2, R3, R4> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persist(ModelPersistorFactory.of(persistableModel));
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      R3 extends UpdatableRecord<R3>,
                      R4 extends UpdatableRecord<R4>,
                      R5 extends UpdatableRecord<R5>,
                      P extends PersistableModel5<R1, R2, R3, R4, R5> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist(dslContext, ModelPersistorFactory.of(persistableModel));
  }

  protected final <V, R1 extends UpdatableRecord<R1>,
                      R2 extends UpdatableRecord<R2>,
                      R3 extends UpdatableRecord<R3>,
                      R4 extends UpdatableRecord<R4>,
                      R5 extends UpdatableRecord<R5>,
                      P extends PersistableModel5<R1, R2, R3, R4, R5> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persist(ModelPersistorFactory.of(persistableModel));
  }

  protected abstract void executeInTransaction(Consumer<DSLContext> usingTransaction);

  private <V, P extends PersistableModel & ValidatableModel<V>,
              Q extends ModelPersistor<V, P>> void persist(final Q modelPersistor) {
    executeInTransaction(dslContext -> persist(dslContext, modelPersistor));
  }

  private <V, M extends Model,
              P extends PersistableModel & ValidatableModel<V>,
              Q extends ModelPersistor<V, P>> void persist(
      final DSLContext dslContext, final Q modelPersistor) {

    final List<V> errors = modelPersistor.getPersistableModel().errors(dslContext);
    if (!errors.isEmpty()) {
      throw new PersistenceException(modelPersistor.getPersistableModel().getClass());
    }

    for (ModelAction modelAction : modelPersistor.getPersistableModel().getPrePersistenceActions(dslContext))
      modelAction.perform(this, dslContext);

    modelPersistor.persist(this, dslContext);

    for (ModelAction modelAction : modelPersistor.getPersistableModel().getPostPersistenceActions(dslContext))
      modelAction.perform(this, dslContext);

  }

  // *******************************************************************************************************************
  // Abstract Methods, relative to deletion
  // *******************************************************************************************************************
}

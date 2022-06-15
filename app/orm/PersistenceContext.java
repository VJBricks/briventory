package orm;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.UpdatableRecord;
import org.jooq.lambda.function.Consumer0;
import org.jooq.lambda.tuple.Tuple2;
import orm.models.DeletableModel;
import orm.models.PersistableModel;
import orm.models.PersistableModel1;
import orm.models.PersistableModel2;
import orm.models.PersistableModel3;
import orm.models.PersistableModel4;
import orm.models.PersistableModel5;
import orm.models.ValidatableModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@code PersistenceContext} provides the helper methods to query, persist, delete and migrate data. The connections,
 * transactions and pools are handled by the implementation.
 */
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
  // Abstract methods, relative to connections and transactions
  // *******************************************************************************************************************

  /**
   * Provides a {@link Consumer} to execute queries in a transaction.
   *
   * @param usingTransaction the {@link Consumer} that will provide a {@link DSLContext}.
   */
  protected abstract void consumeInTransaction(Consumer<DSLContext> usingTransaction);

  /**
   * Provides a {@link Function} to execute queries in a transaction. Those queries return a result of type {@link T}.
   *
   * @param usingTransaction the {@link Function} that will provide a {@link DSLContext}.
   * @param <T> the return type of the {@link Function}.
   *
   * @return an instance of {@link T}.
   */
  protected abstract <T> T produceInTransaction(Function<DSLContext, T> usingTransaction);

  /**
   * Provides a {@link Function} to execute queries in a connection. Those queries return a result of type {@link T}.
   *
   * @param usingConnection the {@link Function} that will provide a {@link DSLContext}.
   * @param <T> the return type of the {@link Function}.
   *
   * @return an instance of {@link T}.
   */
  protected abstract <T> T produceInConnection(Function<DSLContext, T> usingConnection);

  // *******************************************************************************************************************
  // Methods relative to queries
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
  final <M extends Model, R extends Record, F extends Mapper<R, M>> List<M> fetch(
      final F factory, final Function<DSLContext, ResultQuery<R>> query) {
    return produceInConnection(dslContext -> {
      ResultQuery<R> resultQuery = query.apply(dslContext);
      return resultQuery.fetch(factory::map);
    });
  }

  /**
   * Fetches data from the database and returns the result as a {@link List} of instances corresponding to the type
   * {@link M}.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param dslContext the {@link DSLContext}.
   * @param query the query that will be executed into the database.
   * @param <M> the specific implementation, extending {@link Model}.
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return a {@link List} of {@link M} instances.
   */
  final <M extends Model, R extends Record, F extends Mapper<R, M>> List<M> fetch(
      final F factory,
      final DSLContext dslContext,
      final Function<DSLContext, ResultQuery<R>> query) {
    ResultQuery<R> resultQuery = query.apply(dslContext);
    return resultQuery.fetch(factory::map);
  }

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
  final <R extends Record, M extends Model, F extends Mapper<R, M>> List<M> fetch(
      final Function<DSLContext, Tuple2<F, ResultQuery<R>>> query) {
    return produceInConnection(dslContext -> {
      final Tuple2<F, ResultQuery<R>> result = query.apply(dslContext);
      return result.v2.fetch(result.v1::map);
    });
  }

  /**
   * Fetches all the queries provided and returns a {@link List}, containing all instances.
   * <p><strong>Note</strong>: the implementation of this method can implies to suppress uncheck cast warnings.</p>
   *
   * @param queries the queries that will be executed into the database.
   * @param <M> the specific implementation, extending {@link Model}.
   *
   * @return a {@link List} of {@link M} instances.
   */
  @SuppressWarnings("unchecked")
  final <M extends Model> List<M> unionAll(
      final Function<DSLContext,
          Tuple2<? extends Mapper<? extends Record, ? extends M>,
              ? extends ResultQuery<? extends Record>>>[] queries) {
    return produceInTransaction(dslContext -> {
      final List<M> models = new LinkedList<>();

      for (var query : queries) {
        final Tuple2<Mapper<Record, M>, ResultQuery<Record>> result =
            (Tuple2<Mapper<Record, M>, ResultQuery<Record>>) query.apply(dslContext);
        models.addAll(result.v2.fetch(result.v1::map));
      }

      return models;
    });
  }

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
  final <M extends Model, R extends Record, F extends Mapper<R, M>> M fetchOne(
      final F factory, final Function<DSLContext, ResultQuery<R>> query) {
    return produceInConnection(dslContext -> query.apply(dslContext).fetchOne(factory::map));
  }

  /**
   * Fetches the only one record of the query.
   *
   * @param dslContext the {@link DSLContext}.
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
  final <M extends Model, R extends Record, F extends Mapper<R, M>> M fetchOne(
      final F factory,
      final DSLContext dslContext,
      final Function<DSLContext, ResultQuery<R>> query) {
    return query.apply(dslContext).fetchOne(factory::map);
  }

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
  final <M extends Model, R extends Record, F extends Mapper<R, M>> Optional<M> fetchOptional(
      final F factory,
      final Function<DSLContext, ResultQuery<R>> query) {
    return produceInConnection(dslContext -> query.apply(dslContext).fetchOptional(factory::map));
  }

  /**
   * Fetches the only one record of the query and returns it as an {@link Optional}.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param dslContext the {@link DSLContext}.
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
  final <M extends Model, R extends Record, F extends Mapper<R, M>> Optional<M> fetchOptional(
      final F factory,
      final DSLContext dslContext,
      final Function<DSLContext, ResultQuery<R>> query) {
    return query.apply(dslContext).fetchOptional(factory::map);
  }

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
  final <M extends Model, R extends Record, F extends Mapper<R, M>> M fetchSingle(
      final F factory, final Function<DSLContext, ResultQuery<R>> query) {
    return produceInConnection(dslContext -> query.apply(dslContext).fetchSingle(factory::map));
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
  final <T, R extends Record> T fetchSingleInto(final Class<T> clazz,
                                                final Function<DSLContext, ResultQuery<R>> query) {
    return produceInConnection(dslContext -> query.apply(dslContext).fetchSingleInto(clazz));
  }

  /**
   * Executes a {@code select exists} query, using the query provided as the sub-query.
   *
   * @param query the query that will be used as the sub-query.
   *
   * @return {@code true} if the sub-query exports at least one record, otherwise {@code false}.
   *
   * @see DSLContext#fetchExists(Select)
   */
  final boolean exists(final Function<DSLContext, Select<?>> query) {
    return produceInConnection(dslContext -> dslContext.fetchExists(query.apply(dslContext)));
  }

  /**
   * Executes a {@code select exists} query, using the query provided as the sub-query.
   *
   * @param dslContext the {@link DSLContext}.
   * @param query the query that will be used as the sub-query.
   *
   * @return {@code true} if the sub-query exports at least one record, otherwise {@code false}.
   *
   * @see DSLContext#fetchExists(Select)
   */
  final boolean exists(final DSLContext dslContext, final Select<?> query) {
    return dslContext.fetchExists(query);
  }

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
  final <V, R extends UpdatableRecord<R>,
      P extends PersistableModel1<R> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    final R r = persistableModel.createRecord1(dslContext);
    r.merge();
    persistableModel.refresh1(r);
  }

  /**
   * Stores the {@link P} into the database.
   *
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R> the precise subtype of the {@link UpdatableRecord}.
   */
  final <V, R extends UpdatableRecord<R>,
      P extends PersistableModel1<R> & ValidatableModel<V>> void persist(final P persistableModel) {
    consumeInTransaction(dslContext -> executePersistenceProcess(dslContext, persistableModel,
                                                                 () -> persist(dslContext, persistableModel)));
  }

  /**
   * Stores the {@link P} into the database.
   *
   * @param dslContext the {@link DSLContext}.
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   */
  final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      P extends PersistableModel2<R1, R2> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist((PersistableModel1<R1> & ValidatableModel<V>) persistableModel);
    final R2 r2 = persistableModel.createRecord2(dslContext);
    r2.merge();
    persistableModel.refresh2(r2);
  }

  /**
   * Stores the {@link P} into the database.
   *
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   */
  final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      P extends PersistableModel2<R1, R2> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    consumeInTransaction(dslContext -> executePersistenceProcess(dslContext, persistableModel,
                                                                 () -> persist(dslContext, persistableModel)));
  }

  /**
   * Stores the {@link P} into the database.
   *
   * @param dslContext the {@link DSLContext}.
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   * @param <R3> the precise subtype of the third {@link UpdatableRecord}.
   */
  final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      R3 extends UpdatableRecord<R3>,
      P extends PersistableModel3<R1, R2, R3> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist((PersistableModel2<R1, R2> & ValidatableModel<V>) persistableModel);
    final R3 r3 = persistableModel.createRecord3(dslContext);
    r3.merge();
    persistableModel.refresh3(r3);
  }

  /**
   * Stores the {@link P} into the database.
   *
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   * @param <R3> the precise subtype of the third {@link UpdatableRecord}.
   */
  final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      R3 extends UpdatableRecord<R3>,
      P extends PersistableModel3<R1, R2, R3> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    consumeInTransaction(dslContext -> executePersistenceProcess(dslContext, persistableModel,
                                                                 () -> persist(dslContext, persistableModel)));
  }

  /**
   * Stores the {@link P} into the database.
   *
   * @param dslContext the {@link DSLContext}.
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   * @param <R3> the precise subtype of the third {@link UpdatableRecord}.
   * @param <R4> the precise subtype of the fourth {@link UpdatableRecord}.
   */
  final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      R3 extends UpdatableRecord<R3>,
      R4 extends UpdatableRecord<R4>,
      P extends PersistableModel4<R1, R2, R3, R4> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist((PersistableModel3<R1, R2, R3> & ValidatableModel<V>) persistableModel);
    final R4 r4 = persistableModel.createRecord4(dslContext);
    r4.merge();
    persistableModel.refresh4(r4);
  }

  /**
   * Stores the {@link P} into the database.
   *
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   * @param <R3> the precise subtype of the third {@link UpdatableRecord}.
   * @param <R4> the precise subtype of the fourth {@link UpdatableRecord}.
   */
  final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      R3 extends UpdatableRecord<R3>,
      R4 extends UpdatableRecord<R4>,
      P extends PersistableModel4<R1, R2, R3, R4> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    consumeInTransaction(dslContext -> executePersistenceProcess(dslContext, persistableModel,
                                                                 () -> persist(dslContext, persistableModel)));
  }

  /**
   * Stores the {@link P} into the database.
   *
   * @param dslContext the {@link DSLContext}.
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   * @param <R3> the precise subtype of the third {@link UpdatableRecord}.
   * @param <R4> the precise subtype of the fourth {@link UpdatableRecord}.
   * @param <R5> the precise subtype of the fifth {@link UpdatableRecord}.
   */
  final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      R3 extends UpdatableRecord<R3>,
      R4 extends UpdatableRecord<R4>,
      R5 extends UpdatableRecord<R5>,
      P extends PersistableModel5<R1, R2, R3, R4, R5> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist((PersistableModel4<R1, R2, R3, R4> & ValidatableModel<V>) persistableModel);
    final R5 r5 = persistableModel.createRecord5(dslContext);
    r5.merge();
    persistableModel.refresh5(r5);
  }

  /**
   * Stores the {@link P} into the database.
   *
   * @param persistableModel the {@link orm.models.PersistableModel} going to be persisted.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <P> the precise subtype of {@link orm.models.PersistableModel}.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   * @param <R3> the precise subtype of the third {@link UpdatableRecord}.
   * @param <R4> the precise subtype of the fourth {@link UpdatableRecord}.
   * @param <R5> the precise subtype of the fifth {@link UpdatableRecord}.
   */
  final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      R3 extends UpdatableRecord<R3>,
      R4 extends UpdatableRecord<R4>,
      R5 extends UpdatableRecord<R5>,
      P extends PersistableModel5<R1, R2, R3, R4, R5> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    consumeInTransaction(dslContext -> executePersistenceProcess(dslContext, persistableModel,
                                                                 () -> persist(dslContext, persistableModel)));
  }

  private <V, P extends PersistableModel & ValidatableModel<V>> void executePersistenceProcess(
      final DSLContext dslContext, final P persistableModel, final Consumer0 persistenceConsumer) {

    final List<V> errors = persistableModel.validate(dslContext);
    if (!errors.isEmpty()) {
      throw new PersistenceException(persistableModel.getClass());
    }

    for (ModelAction modelAction : persistableModel.getPrePersistenceActions(dslContext))
      modelAction.perform(this, dslContext);

    persistenceConsumer.accept();

    for (ModelAction modelAction : persistableModel.getPostPersistenceActions(dslContext))
      modelAction.perform(this, dslContext);
  }

  // *******************************************************************************************************************
  // Abstract Methods, relative to deletion
  // *******************************************************************************************************************

  /**
   * Deletes the provided {@link D}, using a transaction.
   *
   * @param deletableModel the {@link D} to delete.
   * @param <V> the type of the errors instances, produced during the validation of the model.
   * @param <R> the precise subtype of {@link UpdatableRecord}.
   * @param <D> the precise subtype of {@link DeletableModel}.
   */
  final <V, R extends UpdatableRecord<R>, D extends DeletableModel<V, R>> void deleteInTransaction(
      final D deletableModel) {
    consumeInTransaction(dslContext -> delete(dslContext, deletableModel));
  }

  /**
   * Deletes the provided {@link List} of {@link D} instances, using a transaction.
   *
   * @param deletableModels the {@link List} of {@link D} instances to delete.
   * @param <V> the type of the errors instances, produced during the validation of the model.
   * @param <R> the precise subtype of {@link UpdatableRecord}.
   * @param <D> the precise subtype of {@link DeletableModel}.
   */
  final <V, R extends UpdatableRecord<R>, D extends DeletableModel<V, R>> void deleteAllInTransaction(
      final List<D> deletableModels) {
    consumeInTransaction(dslContext -> deletableModels.forEach(deletableModel -> delete(dslContext, deletableModel)));
  }

  /**
   * Deletes the provided {@link List} of {@link D} instances, using a transaction.
   *
   * @param dslContext the {@link DSLContext}.
   * @param deletableModel the {@link D} to delete.
   * @param <V> the type of the errors instances, produced during the validation of the model.
   * @param <R> the precise subtype of {@link UpdatableRecord}.
   * @param <D> the precise subtype of {@link DeletableModel}.
   */
  final <V, R extends UpdatableRecord<R>, D extends DeletableModel<V, R>> void delete(final DSLContext dslContext,
                                                                                      final D deletableModel) {
    final List<V> deletionErrors = deletableModel.validateForDeletion(dslContext);
    if (!deletionErrors.isEmpty()) {
      throw new DeletionException(deletableModel.getClass());
    }

    for (ModelAction modelAction : deletableModel.getPreDeletionActions(dslContext))
      modelAction.perform(this, dslContext);

    deletableModel.createDeletionRecord(dslContext).delete();

    for (ModelAction modelAction : deletableModel.getPostDeletionActions(dslContext))
      modelAction.perform(this, dslContext);
  }

  // *******************************************************************************************************************
  // Migration
  // *******************************************************************************************************************

  @SuppressWarnings("java:S119")
  protected final <V,
      RF2 extends UpdatableRecord<RF2>,
      RT1 extends UpdatableRecord<RT1>,
      RT2 extends UpdatableRecord<RT2>,
      T extends PersistableModel2<RT1, RT2> & ValidatableModel<V>> T migrate(
      final Function<DSLContext, RF2> recordToDelete, final T resultingModel) {
    return produceInTransaction(dslContext -> {
      final RF2 toDelete = recordToDelete.apply(dslContext);
      toDelete.delete();
      persist(dslContext, resultingModel);
      return resultingModel;
    });
  }

  // *******************************************************************************************************************
  // Abstract Methods, relative to validation
  // *******************************************************************************************************************

  /**
   * Validates the {@link V} instance.
   *
   * @param validatableModel the {@link V} to validate.
   * @param <T> the type of the result.
   * @param <V> the precise subtype of {@link ValidatableModel}.
   *
   * @return the result of the validation as a {@link T}.
   */
  final <T, V extends ValidatableModel<T>> List<T> validate(final V validatableModel) {
    return produceInTransaction(validatableModel::validate);
  }

}

package orm;

import org.jooq.Record;
import org.jooq.*;
import org.jooq.lambda.function.Consumer0;
import org.jooq.lambda.tuple.Tuple2;
import orm.models.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
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
    return persistenceContext.produceInConnection(dslContext -> {
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
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return a {@link List} of {@link M} instances.
   */
  protected final <R extends Record, F extends Mapper<R, M>> List<M> fetch(
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
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return a {@link List} of {@link M} instances.
   */
  protected final <R extends Record, F extends Mapper<R, M>> List<M> fetch(
      final Function<DSLContext, Tuple2<F, ResultQuery<R>>> query) {
    return persistenceContext.produceInConnection(dslContext -> {
      final Tuple2<F, ResultQuery<R>> result = query.apply(dslContext);
      return result.v2.fetch(result.v1::map);
    });
  }

  /**
   * Fetches all the queries provided and returns a {@link List}, containing all instances.
   *
   * @param queries the queries that will be executed into the database.
   *
   * @return a {@link List} of {@link M} instances.
   */
  @SafeVarargs
  @SuppressWarnings({"varargs", "unchecked"})
  protected final List<M> unionAll(
      final Function<DSLContext,
          Tuple2<? extends Mapper<? extends Record, ? extends M>,
              ? extends ResultQuery<? extends Record>>>... queries) {
    return persistenceContext.produceInTransaction(dslContext -> {
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
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return the instance of type {@link M}, or {@code null} if the query has no row.
   *
   * @throws org.jooq.exception.DataAccessException if something went wrong executing the query.
   * @throws org.jooq.exception.TooManyRowsException if the query returned more than one record.
   * @see ResultQuery#fetchOne()
   */
  protected final <R extends Record, F extends Mapper<R, M>> M fetchOne(
      final F factory,
      final Function<DSLContext, ResultQuery<R>> query) {
    return persistenceContext.produceInConnection(dslContext -> query.apply(dslContext).fetchOne(factory::map));
  }

  /**
   * Fetches the only one record of the query.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param dslContext the {@link DSLContext}.
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
  protected final <R extends Record, F extends Mapper<R, M>> M fetchOne(
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
   * @param <R> the specific implementation, extending {@link Record}.
   * @param <F> the specific implementation, extending {@link Mapper}.
   *
   * @return the instance of type {@link M}, or {@code null} if the query has no row.
   *
   * @throws org.jooq.exception.DataAccessException if something went wrong executing the query.
   * @throws org.jooq.exception.TooManyRowsException if the query returned more than one record.
   * @see ResultQuery#fetchOptional()
   */
  protected final <R extends Record, F extends Mapper<R, M>> Optional<M> fetchOptional(
      final F factory,
      final Function<DSLContext, ResultQuery<R>> query) {
    return persistenceContext.produceInConnection(dslContext -> query.apply(dslContext).fetchOptional(factory::map));
  }

  /**
   * Fetches the only one record of the query and returns it as an {@link Optional}.
   *
   * @param factory the {@link Mapper} that will create instances of type {@link M}.
   * @param dslContext the {@link DSLContext}.
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
  protected final <R extends Record, F extends Mapper<R, M>> Optional<M> fetchOptional(
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
  protected final <R extends Record, F extends Mapper<R, M>> M fetchSingle(
      final F factory, final Function<DSLContext, ResultQuery<R>> query) {
    return persistenceContext.produceInConnection(dslContext -> query.apply(dslContext).fetchSingle(factory::map));
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
  protected final <T, R extends Record> T fetchSingleInto(final Class<T> clazz,
                                                          final Function<DSLContext, ResultQuery<R>> query) {
    return persistenceContext.produceInConnection(dslContext -> query.apply(dslContext).fetchSingleInto(clazz));
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
  protected final boolean exists(final Function<DSLContext, Select<?>> query) {
    return persistenceContext.produceInConnection(dslContext -> dslContext.fetchExists(query.apply(dslContext)));
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
  protected final boolean exists(final DSLContext dslContext, final Select<?> query) {
    return dslContext.fetchExists(query);
  }

  // *******************************************************************************************************************
  // Persistence
  // *******************************************************************************************************************

  /**
   * Executes the persistence process on the model provided.
   *
   * @param dslContext the {@link DSLContext}.
   * @param persistableModel the {@link PersistableModel} to persist.
   * @param persistenceConsumer the consumer that will execute the persistence process.
   * @param <V> the concrete type of the validation messages.
   * @param <P> the concrete type of the {@link PersistableModel}.
   */
  private <V, P extends PersistableModel<M> & ValidatableModel<V>> void executePersistenceProcess(
      final DSLContext dslContext, final P persistableModel, final Consumer0 persistenceConsumer) {

    final List<V> errors = persistableModel.validate(dslContext);
    if (!errors.isEmpty()) {
      throw new PersistenceException(persistableModel.getClass());
    }

    for (Action action : persistableModel.getPrePersistenceActions(dslContext))
      action.perform(dslContext);

    persistenceConsumer.accept();

    for (Action action : persistableModel.getPostPersistenceActions(dslContext))
      action.perform(dslContext);
  }

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
      P extends PersistableModel1<M, R> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    final R r = persistableModel.createRecord1(dslContext);
    r.merge();
    persistableModel.refresh1(r);
  }

  /**
   * Persists the {@link PersistableModel1} provided.
   *
   * @param persistableModel the {@link PersistableModel1} to persist.
   * @param <V> the type of the validation errors.
   * @param <R> the precise subtype of {@link UpdatableRecord}.
   * @param <P> the precise subtype that is a {@link orm.models.PersistableModel} and, also, a
   * {@link ValidatableModel}.
   */
  protected final <V, R extends UpdatableRecord<R>,
      P extends PersistableModel1<M, R> & ValidatableModel<V>> void persist(final P persistableModel) {
    persistenceContext.consumeInTransaction(dslContext ->
                                                executePersistenceProcess(dslContext, persistableModel,
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
      P extends PersistableModel2<M, R1, R2> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {

    persist((PersistableModel1<M, R1> & ValidatableModel<V>) persistableModel);

    final R2 r2 = persistableModel.createRecord2(dslContext);
    r2.merge();
    persistableModel.refresh2(r2);
  }

  /**
   * Persists the {@link PersistableModel1} provided.
   *
   * @param persistableModel the {@link PersistableModel1} to persist.
   * @param <V> the type of the validation errors.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   * @param <P> the precise subtype that is a {@link orm.models.PersistableModel} and, also, a
   * {@link ValidatableModel}.
   */
  protected final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      P extends PersistableModel2<M, R1, R2> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persistenceContext.consumeInTransaction(dslContext ->
                                                executePersistenceProcess(dslContext, persistableModel,
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
      P extends PersistableModel3<M, R1, R2, R3> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist((PersistableModel2<M, R1, R2> & ValidatableModel<V>) persistableModel);
    final R3 r3 = persistableModel.createRecord3(dslContext);
    r3.merge();
    persistableModel.refresh3(r3);
  }

  /**
   * Persists the {@link PersistableModel1} provided.
   *
   * @param persistableModel the {@link PersistableModel1} to persist.
   * @param <V> the type of the validation errors.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   * @param <R3> the precise subtype of the third {@link UpdatableRecord}.
   * @param <P> the precise subtype that is a {@link orm.models.PersistableModel} and, also, a
   * {@link ValidatableModel}.
   */
  protected final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      R3 extends UpdatableRecord<R3>,
      P extends PersistableModel3<M, R1, R2, R3> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persistenceContext.consumeInTransaction(dslContext ->
                                                executePersistenceProcess(dslContext, persistableModel,
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
      P extends PersistableModel4<M, R1, R2, R3, R4> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist((PersistableModel3<M, R1, R2, R3> & ValidatableModel<V>) persistableModel);
    final R4 r4 = persistableModel.createRecord4(dslContext);
    r4.merge();
    persistableModel.refresh4(r4);
  }

  /**
   * Persists the {@link PersistableModel1} provided.
   *
   * @param persistableModel the {@link PersistableModel1} to persist.
   * @param <V> the type of the validation errors.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   * @param <R3> the precise subtype of the third {@link UpdatableRecord}.
   * @param <R4> the precise subtype of the fourth {@link UpdatableRecord}.
   * @param <P> the precise subtype that is a {@link orm.models.PersistableModel} and, also, a
   * {@link ValidatableModel}.
   */
  protected final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      R3 extends UpdatableRecord<R3>,
      R4 extends UpdatableRecord<R4>,
      P extends PersistableModel4<M, R1, R2, R3, R4> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persistenceContext.consumeInTransaction(dslContext ->
                                                executePersistenceProcess(dslContext, persistableModel,
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
      P extends PersistableModel5<M, R1, R2, R3, R4, R5> & ValidatableModel<V>> void persist(
      final DSLContext dslContext, final P persistableModel) {
    persist((PersistableModel4<M, R1, R2, R3, R4> & ValidatableModel<V>) persistableModel);
    final R5 r5 = persistableModel.createRecord5(dslContext);
    r5.merge();
    persistableModel.refresh5(r5);
  }

  /**
   * Persists the {@link PersistableModel1} provided.
   *
   * @param persistableModel the {@link PersistableModel1} to persist.
   * @param <V> the type of the validation errors.
   * @param <R1> the precise subtype of the first {@link UpdatableRecord}.
   * @param <R2> the precise subtype of the second {@link UpdatableRecord}.
   * @param <R3> the precise subtype of the third {@link UpdatableRecord}.
   * @param <R4> the precise subtype of the fourth {@link UpdatableRecord}.
   * @param <R5> the precise subtype of the fifth {@link UpdatableRecord}.
   * @param <P> the precise subtype that is a {@link orm.models.PersistableModel} and, also, a
   * {@link ValidatableModel}.
   */
  protected final <V, R1 extends UpdatableRecord<R1>,
      R2 extends UpdatableRecord<R2>,
      R3 extends UpdatableRecord<R3>,
      R4 extends UpdatableRecord<R4>,
      R5 extends UpdatableRecord<R5>,
      P extends PersistableModel5<M, R1, R2, R3, R4, R5> & ValidatableModel<V>> void persist(
      final P persistableModel) {
    persistenceContext.consumeInTransaction(dslContext ->
                                                executePersistenceProcess(dslContext, persistableModel,
                                                                          () -> persist(dslContext, persistableModel)));
  }

  // *******************************************************************************************************************
  // Deletion
  // *******************************************************************************************************************

  protected final <V, R extends UpdatableRecord<R>, D extends DeletableModel<M, V, R>> void deleteInTransaction(
      final D deletableModel) {
    persistenceContext.consumeInTransaction(dslContext -> delete(dslContext, deletableModel));
  }

  protected final <V, R extends UpdatableRecord<R>, D extends DeletableModel<M, V, R>> void deleteAllInTransaction(
      final List<D> deletableModels) {
    persistenceContext.consumeInTransaction(
        dslContext -> deletableModels.forEach(deletableModel ->
                                                  delete(dslContext, deletableModel)));
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
  private <V, R extends UpdatableRecord<R>, D extends DeletableModel<M, V, R>> void delete(final DSLContext dslContext,
                                                                                           final D deletableModel) {
    final List<V> deletionErrors = deletableModel.validateForDeletion(dslContext);
    if (!deletionErrors.isEmpty()) {
      throw new DeletionException(deletableModel.getClass());
    }

    for (ModelAction<M> modelAction : deletableModel.getPreDeletionActions(dslContext))
      modelAction.perform(dslContext);

    deletableModel.createDeletionRecord(dslContext).delete();

    for (ModelAction<M> modelAction : deletableModel.getPostDeletionActions(dslContext))
      modelAction.perform(dslContext);
  }
  // *******************************************************************************************************************
  // Migration
  // *******************************************************************************************************************

  @SuppressWarnings("java:S119")
  protected final <V,
      RF2 extends UpdatableRecord<RF2>,
      RT1 extends UpdatableRecord<RT1>,
      RT2 extends UpdatableRecord<RT2>,
      T extends PersistableModel2<M, RT1, RT2> & ValidatableModel<V>> T migrate(
      final Function<DSLContext, RF2> recordToDelete, final T resultingModel) {
    return persistenceContext.produceInTransaction(dslContext -> {
      final RF2 toDelete = recordToDelete.apply(dslContext);
      toDelete.delete();
      persist(dslContext, resultingModel);
      return resultingModel;
    });
  }

  // *******************************************************************************************************************
  // Validation
  // *******************************************************************************************************************

  /**
   * Validates the {@link V} provided.
   *
   * @param validatableModel the {@link V} to validate.
   * @param <R> The type of the validation results.
   * @param <V> the precise subtype of {@link ValidatableModel}.
   *
   * @return a {@link List} containing all {@link R} instances.
   */
  protected final <R, V extends ValidatableModel<R>> List<R> validate(final V validatableModel) {
    return persistenceContext.produceInTransaction(validatableModel::validate);
  }

  // *******************************************************************************************************************
  // Lazy Loader Helpers
  // *******************************************************************************************************************

  /**
   * Constructs an instance of {@link ManyModelsLoader}.
   *
   * @param key the key.
   * @param fetcher the {@link BiFunction} that will be used to fetch.
   * @param actionsCreator the {@link Function3} that will return a {@link List} of {@link Action} instances, that will
   * be executed during the persistence process.
   * @param <K> the type of the key.
   *
   * @return an instance of {@link ManyModelsLoader}.
   */
  protected final <K> ManyModelsLoader<K, M> createManyModelsLoader(
      final K key,
      final BiFunction<DSLContext, K, List<M>> fetcher,
      final Function3<DSLContext, K, List<M>, List<Action>> actionsCreator) {
    return new ManyModelsLoader<>(persistenceContext, key, fetcher, actionsCreator);
  }

  /**
   * Constructs an instance of {@link ModelLoader}.
   *
   * @param fetcher the {@link BiFunction} that will be used to fetch.
   * @param actionsCreator the {@link Function3} that will return a {@link List} of {@link Action} instances, that will
   * be executed during the persistence process.
   * @param <K> the type of the key.
   *
   * @return an instance of {@link ModelLoader}.
   */
  protected final <K> ModelLoader<K, M> createModelLoader(
      final BiFunction<DSLContext, K, M> fetcher,
      final Function3<DSLContext, K, M, List<Action>> actionsCreator) {
    return new ModelLoader<>(persistenceContext, fetcher, actionsCreator);
  }

  /**
   * Constructs an instance of {@link ModelLoader}.
   *
   * @param key the key.
   * @param fetcher the {@link BiFunction} that will be used to fetch.
   * @param actionsCreator the {@link Function3} that will return a {@link List} of {@link Action} instances, that will
   * be executed during the persistence process.
   * @param <K> the type of the key.
   *
   * @return an instance of {@link ModelLoader}.
   */
  protected final <K> ModelLoader<K, M> createModelLoader(
      final K key,
      final BiFunction<DSLContext, K, M> fetcher,
      final Function3<DSLContext, K, M, List<Action>> actionsCreator) {
    return new ModelLoader<>(persistenceContext, key, fetcher, actionsCreator);
  }

  /**
   * Constructs an instance of {@link OptionalModelLoader}.
   *
   * @param fetcher the {@link BiFunction} that will be used to fetch.
   * @param <K> the type of the key.
   * @param actionsCreator the {@link Function3} that will return a {@link List} of {@link Action} instances, that will
   * be executed during the persistence process.
   *
   * @return an instance of {@link OptionalModelLoader}.
   */
  protected final <K> OptionalModelLoader<K, M> createOptionalModelLoader(
      final BiFunction<DSLContext, K, Optional<M>> fetcher,
      final Function3<DSLContext, K, Optional<M>, List<Action>> actionsCreator) {
    return new OptionalModelLoader<>(persistenceContext, fetcher, actionsCreator);
  }

  /**
   * Constructs an instance of {@link OptionalModelLoader}.
   *
   * @param key the key.
   * @param fetcher the {@link BiFunction} that will be used to fetch.
   * @param <K> the type of the key.
   * @param actionsCreator the {@link Function3} that will return a {@link List} of {@link Action} instances, that will
   * be executed during the persistence process.
   *
   * @return an instance of {@link OptionalModelLoader}.
   */
  protected final <K> OptionalModelLoader<K, M> createOptionalModelLoader(
      final K key,
      final BiFunction<DSLContext, K, Optional<M>> fetcher,
      final Function3<DSLContext, K, Optional<M>, List<Action>> actionsCreator) {
    return new OptionalModelLoader<>(persistenceContext, key, fetcher, actionsCreator);
  }

  /**
   * Constructs an instance of {@link RecordLoader}.
   *
   * @param key the key.
   * @param fetcher the {@link BiFunction} that will be used to fetch.
   * @param actionsCreator the {@link Function3} that will be used to create the corresponding instances of
   * {@link Action}.
   * @param <K> the type of the key.
   * @param <V> the type of the value.
   *
   * @return an instance of {@link RecordLoader}.
   */
  protected final <K extends Model, V> RecordLoader<K, V> createRecordLoader(
      final K key,
      final BiFunction<DSLContext, K, V> fetcher,
      final Function3<DSLContext, K, V, List<Action>> actionsCreator) {
    return new RecordLoader<>(persistenceContext, key, fetcher, actionsCreator);
  }

}

package database;

import globalhandlers.ExceptionManager;
import orm.models.Entity;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;
import orm.models.IStorableEntity;
import play.db.Database;
import play.libs.concurrent.HttpExecution;
import orm.repositories.DeletableEntityHandler;
import orm.repositories.Repository;
import orm.repositories.StorableEntityHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * {@code BriventoryDB} is the entry point to perform database manipulations. This class is intends to be used in a
 * dependency injected environment.
 */
@Singleton
public final class BriventoryDB {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The injected {@link Database} instance. */
  private final Database database;
  /** The {@link Executor} retrieved from the injected {@link BriventoryDBContext} instance. */
  private final Executor executor;
  /** The injected {@link ExceptionManager} to handle exceptions. */
  private final ExceptionManager exceptionManager;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link BriventoryDB} instance using the injected parameters.
   *
   * @param database the {@link Database} instance.
   * @param context the {@link BriventoryDBContext} instance.
   * @param exceptionManager the {@link ExceptionManager} instance.
   */
  @Inject
  public BriventoryDB(final Database database, final BriventoryDBContext context,
                      final ExceptionManager exceptionManager) {
    this.database = database;
    executor = HttpExecution.fromThread((Executor) context);
    this.exceptionManager = exceptionManager;
  }

  /**
   * Executes a query in the Briventory database, using its thread execution context.
   *
   * @param function the {@link Function} that will execute the query.
   * @param <R> the precise subtype of the {@link Record}.
   *
   * @return an instance of {@link R}.
   */
  public <R> R query(final Function<DSLContext, R> function) {
    try {
      return supplyAsync(() -> database.withConnection(connection -> {
        final var dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        return function.apply(dslContext);
      }), executor).join();
    } catch (Exception e) {
      final BriventoryDBException briventoryDBException = new BriventoryDBException("Query failure", e);
      exceptionManager.handleDatabaseException(briventoryDBException);
      throw briventoryDBException;
    }
  }

  /**
   * Creates a new and empty {@link Record}, according the {@link Table} provided.
   *
   * @param table the {@link Table} to define the {@link Record} type.
   * @param <R> the precise subtype of the {@link Record}.
   *
   * @return a new and empty {@link Record}.
   */
  public <R extends Record> R createRecord(final Table<R> table) {
    try {
      return database.withTransaction(connection -> {
        final var dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        return dslContext.newRecord(table);
      });
    } catch (Exception e) {
      final BriventoryDBException briventoryDBException = new BriventoryDBException("Query failure", e);
      exceptionManager.handleDatabaseException(briventoryDBException);
      throw briventoryDBException;
    }
  }

  /**
   * Stores the {@link E} into the database.
   *
   * @param storableEntityHandler the {@link StorableEntityHandler} that will handle the storage process.
   * @param entity the {@link E} to store.
   * @param <V> the type of the errors instances, produced during the validation of the entity.
   * @param <E> the precise subtype of the {@link Entity}.
   * @param <R> the precise subtype of the {@link Record}.
   */
  public <V, E extends Entity<? extends Repository> & IStorableEntity<V>, R extends UpdatableRecord<R>> void store(
      final StorableEntityHandler<V, E, R> storableEntityHandler,
      final E entity) {
    try {
      supplyAsync(() -> database.withTransaction(connection -> {
        final var dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        storableEntityHandler.store(dslContext, entity);
        return null;
      }), executor).join();
    } catch (Exception e) {
      BriventoryDBException briventoryDBException;
      if (e instanceof BriventoryDBException alreadyWellTyped) {
        briventoryDBException = alreadyWellTyped;
      } else if (e instanceof CompletionException completionException &&
                 completionException.getCause() instanceof BriventoryDBException innerException) {
        briventoryDBException = innerException;
      } else {
        briventoryDBException = new BriventoryDBException(
            String.format("The persistence of entity %s has failed", entity), e);
      }
      exceptionManager.handleDatabaseException(briventoryDBException);
      throw briventoryDBException;
    }
  }

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
    try {
      supplyAsync(() -> database.withTransaction(connection -> {
        final var dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        deletableEntityHandler.delete(dslContext, entity);
        return null;
      }), executor).join();
    } catch (Exception e) {
      final BriventoryDBException briventoryDBException =
          new BriventoryDBException(String.format("The deletion of entity %s has failed", entity), e);
      exceptionManager.handleDatabaseException(briventoryDBException);
      throw briventoryDBException;
    }
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
    try {
      supplyAsync(() -> database.withTransaction(connection -> {
        final var dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        for (E entity : entities)
          deletableEntityHandler.delete(dslContext, entity);
        return null;
      }), executor).join();
    } catch (Exception e) {
      final BriventoryDBException briventoryDBException =
          new BriventoryDBException(
              String.format("The deletion of the list of the following entities has failed: %s", entities),
              e);
      exceptionManager.handleDatabaseException(briventoryDBException);
      throw briventoryDBException;
    }
  }

}

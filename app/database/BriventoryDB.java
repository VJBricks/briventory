package database;

import org.hibernate.Session;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public final class BriventoryDB {

  /** The global cache region. */
  public static final String CACHE_REGION = "briventoryCache";
  /** The name of the persistence unit. */
  private static final String PERSISTENCE_UNIT_NAME = "default";

  /** The injected {@link JPAApi} instance. */
  private final JPAApi jpaApi;
  /** The {@link Executor} retrieved from the injected {@link BriventoryDBContext} instance. */
  private final Executor executor;

  /**
   * Creates a new instance of {@link BriventoryDB} instance using the injected parameters.
   *
   * @param jpaApi the {@link JPAApi} instance.
   * @param context the {@link BriventoryDBContext} instance.
   */
  @Inject
  public BriventoryDB(final JPAApi jpaApi, final BriventoryDBContext context) {
    this.jpaApi = jpaApi;
    executor = HttpExecution.fromThread((Executor) context);
  }

  /**
   * Executes a query in the Briventory database, using it's thread execution context.
   *
   * @param <R> the return type of the query.
   * @param function the {@link Function} that will execute the query.
   *
   * @return the {@link CompletableFuture} created to use the Briventory database thread execution context.
   */
  public <R> CompletableFuture<R> query(final Function<Session, R> function) {
    return supplyAsync(() -> jpaApi.withTransaction(PERSISTENCE_UNIT_NAME, true, entityManager -> {
      return function.apply(entityManager.unwrap(Session.class));
    }), executor);
  }

  /**
   * Persists the objects into the database.
   *
   * @param function the {@link Function} that will persist the objects.
   * @param <R> the return type.
   *
   * @return the {@link CompletableFuture} created to use the Briventory database thread execution context.
   */
  public <R> CompletableFuture<R> persist(final Function<Session, R> function) {
    return supplyAsync(() -> jpaApi.withTransaction(PERSISTENCE_UNIT_NAME, false, entityManager -> {
      var session = entityManager.unwrap(Session.class);
      var result = function.apply(session);
      session.flush();
      return result;
    }), executor);
  }

  public CompletableFuture<Void> remove(final Function<Session, Void> function) {
    return runAsync(() -> jpaApi.withTransaction(PERSISTENCE_UNIT_NAME, false, entityManager -> {
      var session = entityManager.unwrap(Session.class);
      function.apply(session);
      session.flush();
    }));
  }

}

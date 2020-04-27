package database;

import ch.varani.briventory.BriventoryBuildInfo;
import models.Revision;
import org.hibernate.Session;
import org.semver.Version;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

@Singleton
public class BriventoryDB {

  /** The app version. */
  private static final Version APP_VERSION = Version.parse(BriventoryBuildInfo.version());

  /** The {@link JPAApi} to persist objects. */
  private final JPAApi jpaApi;
  /** The {@link Executor} retrieved from the injected {@link BriventoryDBContext} instance. */
  private final Executor executor;

  /**
   * Creates a new instance of {@link BriventoryDB} instance using the injected parameters.
   *
   * @param jpaApi the {@link JPAApi} to persist objects.
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
  public final <R> CompletableFuture<R> query(final Function<Session, R> function) {
    return CompletableFuture.supplyAsync(() -> jpaApi.withTransaction(entityManager -> {
      final Session session = entityManager.unwrap(Session.class);
      return function.apply(session);
    }));
  }

  /**
   * Perists the objects into the database.
   *
   * @param function the {@link Function} that will persist the objects.
   * @param <R> the return type.
   *
   * @return the {@link CompletableFuture} created to use the Briventory database thread execution context.
   */
  public <R> CompletableFuture<R> persist(final Function<EntityManager, R> function) {
    return CompletableFuture.supplyAsync(() -> jpaApi.withTransaction(function), executor);
  }

  /**
   * Does the database has been initialized ?
   *
   * @param session the {@link Session} instance to execute the query.
   *
   * @return {@code true} if the database is initialized and the version correspond to the App major version, otherwise
   * {@code false}.
   */
  public boolean isDatabaseInitialized(final Session session) {
    try {
      final Revision revision = session.createQuery("select r from Revision r", Revision.class)
                                       .setCacheable(true)
                                       .getSingleResult();
      Version dbVersion = new Version(revision.getDatabase(), 0, 0);
      return dbVersion.isCompatible(APP_VERSION);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Does the database contains at least one non-locked administrator ?
   *
   * @param session the {@link Session} instance to execute the query.
   *
   * @return {@code true} if the database contains at least one non-locked administrator, otherwise {@code false}.
   */
  public boolean hasActiveAdministrator(final Session session) {
    final long count = session.createQuery(
        "select count(a) " +
        "from Admin a " +
        "left join LockedUser lu on a.id = lu.id " +
        "where lu.id is null",
        Long.class)
                              .setCacheable(true)
                              .getSingleResult();
    return count > 0;
  }

  /**
   * Does the App is in maintenance mode ?
   *
   * @param session the {@link Session} instance to execute the query.
   *
   * @return {@code true} if the App should be considered as <em>in maintenance</em>, otherwise {@code false}. The
   * maintenance mode is enabled if:
   * <ul>
   *   <li>the database has not been initialized;</li>
   *   <li>the database version does not correspond to the App major version;</li>
   *   <li>there is not non-locked administrator.</li>
   * </ul>
   */
  public boolean isInMaintenance(final Session session) {
    return !isDatabaseInitialized(session) ||
           !hasActiveAdministrator(session);
  }

}

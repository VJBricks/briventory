package database;

import ch.varani.briventory.BriventoryBuildInfo;
import ch.varani.briventory.jooq.tables.records.RevisionRecord;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.impl.DSL;
import org.semver.Version;
import play.api.db.Database;
import play.db.jpa.JPAApi;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import static ch.varani.briventory.jooq.tables.Admin.ADMIN;
import static ch.varani.briventory.jooq.tables.Lockeduser.LOCKEDUSER;
import static ch.varani.briventory.jooq.tables.Revision.REVISION;
import static org.jooq.impl.DSL.count;

@Singleton
public class BriventoryDB {

  /** The app version. */
  private static final Version APP_VERSION = Version.parse(BriventoryBuildInfo.version());

  /** The {@link JPAApi} to persist objects. */
  private final JPAApi jpaApi;
  /** The {@link Executor} retrieved from the injected {@link BriventoryDBContext} instance. */
  private final Executor executor;
  /** The injected {@link Database} instance. */
  private final Database database;

  /**
   * Creates a new instance of {@link BriventoryDB} instance using the injected parameters.
   *
   * @param jpaApi the {@link JPAApi} to persist objects.
   * @param context the {@link BriventoryDBContext} instance.
   * @param database the {@link Database} instance.
   */
  @Inject
  public BriventoryDB(final JPAApi jpaApi, final BriventoryDBContext context, final Database database) {
    this.jpaApi = jpaApi;
    executor = HttpExecution.fromThread((Executor) context);
    this.database = database;
  }

  /**
   * Executes a query in the Briventory database, using it's thread execution context.
   *
   * @param <T> the return type of the query.
   * @param function the {@link Function} that will execute the query.
   *
   * @return the {@link CompletableFuture} created to use the Briventory database thread execution context.
   */
  public <T> CompletableFuture<T> query(final Function<DSLContext, T> function) {

    return CompletableFuture.supplyAsync(() -> database.withConnection(connection -> {
      DSLContext dialect = DSL.using(connection, SQLDialect.POSTGRES);
      dialect.settings().setRenderNameCase(RenderNameCase.LOWER);
      return function.apply(dialect);
    }), executor);
  }

  /**
   * Perists the objects into the database.
   *
   * @param function the {@link Function} that will persist the objects.
   * @param <T> the return type.
   *
   * @return the {@link CompletableFuture} created to use the Briventory database thread execution context.
   */
  public <T> CompletableFuture<T> persist(final Function<EntityManager, T> function) {
    return CompletableFuture.supplyAsync(() -> jpaApi.withTransaction(function::apply), executor);
  }

  /**
   * @return {@code true} if the database is initialized and the version correspond to the App major version, otherwise
   * {@code false}.
   */
  public boolean isDatabaseInitialized() {
    return query(context -> {
      RevisionRecord revision = context.selectFrom(REVISION).fetchAny();
      Version dbVersion = new Version(revision.getDatabase(), 0, 0);
      return dbVersion.isCompatible(APP_VERSION);
    }).join();
  }

  /** @return {@code true} if the database contains at lease one non-locked administrator, otherwise {@code false}. */
  public boolean hasActiveAdministrator() {
    return query(context -> {
      Result<Record1<Integer>> result =
          context.select(count())
                 .from(ADMIN)
                 .leftJoin(LOCKEDUSER).on(ADMIN.IDUSER.eq(LOCKEDUSER.IDUSER))
                 .where(LOCKEDUSER.IDUSER.isNull())
                 .fetch();
      return result.get(0).component1() > 0;
    }).join();
  }

  /**
   * @return {@code true} if the App should be considered as <em>in maintenance</em>, otherwise {@code false}. The
   * maintenance mode is enabled if:
   * <ul>
   *   <li>the database has not been initialized;</li>
   *   <li>the database version does not correspond to the App major version;</li>
   *   <li>there is not non-locked administrator.</li>
   * </ul>
   */
  public boolean isInMaintenance() {
    return !isDatabaseInitialized() ||
           !hasActiveAdministrator();
  }

}

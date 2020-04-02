package database;

import ch.varani.briventory.BriventoryBuildInfo;
import ch.varani.briventory.tables.records.RevisionRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.semver.Version;
import play.Logger;
import play.api.db.Database;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static ch.varani.briventory.tables.Admin.ADMIN;
import static ch.varani.briventory.tables.Lockeduser.LOCKEDUSER;
import static ch.varani.briventory.tables.Revision.REVISION;
import static org.jooq.impl.DSL.count;


@Singleton
public class BriventoryDB {

  /** The app version. */
  private static final Version APP_VERSION = Version.parse(BriventoryBuildInfo.version());

  /** The {@link Executor} retrieved from the injected {@link BriventoryDBContext} instance. */
  private final Executor executor;
  /** The injected {@link Database} instance. */
  private final Database database;

  /**
   * Creates a new instance of {@link BriventoryDB} instance using the injected parameters.
   *
   * @param context the {@link BriventoryDBContext} instance.
   * @param database the {@link Database} instance.
   */
  @Inject
  public BriventoryDB(final BriventoryDBContext context, final Database database) {
    executor = HttpExecution.fromThread((Executor) context);
    this.database = database;
  }

  /**
   * Runs the {@link SQL} query.
   *
   * @param sql the {@link SQL} query.
   *
   * @return the {@link CompletableFuture} containing the {@link Result} of {@link Record}.
   */
  public CompletableFuture<Result<Record>> query(final SQL sql) {

    return CompletableFuture.supplyAsync(() ->
                                             database.withConnection(connection -> {
                                               DSLContext dialect = DSL.using(connection, SQLDialect.POSTGRES);
                                               return dialect.fetch(sql);
                                             }), executor);
  }

  /**
   * Runs the {@link SQL} query within a transaction.
   *
   * @param sql the {@link SQL} query.
   *
   * @return the {@link CompletableFuture} containing the {@link Result} of {@link Record}.
   */
  public CompletableFuture<Result<Record>> withTransaction(final SQL sql) {

    return CompletableFuture.supplyAsync(() ->
                                             database.withTransaction(connection -> {
                                               DSLContext dialect = DSL.using(connection, SQLDialect.POSTGRES);
                                               return dialect.fetch(sql);
                                             }), executor);
  }

  /**
   * @return {@code true} if the database is initialized and the version correspond to the App major version, otherwise
   * {@code false}.
   */
  public boolean isDatabaseInitialized() {
    try {
      return CompletableFuture.supplyAsync(() ->
                                               database.withConnection(connection -> {
                                                 DSLContext sql = DSL.using(connection, SQLDialect.POSTGRES);
                                                 RevisionRecord revision = sql.selectFrom(REVISION).fetchAny();

                                                 Version dbVersion = new Version(revision.getDatabase(), 0, 0);
                                                 return dbVersion.isCompatible(APP_VERSION);
                                               }),
                                           executor).get();
    } catch (ExecutionException e) {
      return true;
    } catch (InterruptedException e) {
      Logger.of(BriventoryDB.class).error("Database call interrupted", e);
      Thread.currentThread().interrupt();
    }
    return true;
  }

  /** @return {@code true} if the database contains at lease one non-locked administrator, otherwise {@code false}. */
  public boolean hasActiveAdministrator() {
    try {
      return CompletableFuture.supplyAsync(() ->
                                               database.withConnection(connection -> {
                                                 DSLContext sql = DSL.using(connection, SQLDialect.POSTGRES);
                                                 Result<Record1<Integer>> result = sql.select(count())
                                                                                      .from(ADMIN)
                                                                                      .leftJoin(LOCKEDUSER)
                                                                                      .on(ADMIN.IDUSER
                                                                                              .eq(LOCKEDUSER.IDUSER))
                                                                                      .where(LOCKEDUSER.IDUSER.isNull())
                                                                                      .fetch();
                                                 return result.get(0).component1() > 0;
                                               }), executor).get();
    } catch (ExecutionException e) {
      return false;
    } catch (InterruptedException e) {
      Logger.of(BriventoryDB.class).error("Database call interrupted", e);
      Thread.currentThread().interrupt();
    }
    return false;
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
    return isDatabaseInitialized() ||
           !hasActiveAdministrator();
  }

}

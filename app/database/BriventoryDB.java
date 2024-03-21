package database;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import orm.PersistenceContext;
import play.db.ConnectionCallable;
import play.db.ConnectionRunnable;
import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@code BriventoryDB} is the entry point to perform database manipulations. This class is intends to be used in a
 * dependency injected environment.
 */
@Singleton
public final class BriventoryDB extends PersistenceContext {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The injected {@link Database} instance. */
  private final Database database;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link BriventoryDB} instance using the injected parameters.
   *
   * @param database the {@link Database} instance.
   */
  @Inject
  public BriventoryDB(final Database database) {
    this.database = database;
  }

  // *******************************************************************************************************************
  // PersistenceContext Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  protected SQLDialect getDialect() { return SQLDialect.POSTGRES; }

  /** {@inheritDoc} */
  @Override
  protected String getDatabaseName() {
    return database.getName();
  }

  /** {@inheritDoc} */
  @Override
  protected String getDatabaseURL() {
    return database.getUrl();
  }

  /** {@inheritDoc} */
  @Override
  protected void consumeInTransaction(final Consumer<DSLContext> usingTransaction) {
    database.withTransaction((ConnectionRunnable) c -> usingTransaction.accept(DSL.using(c, getDialect())));
  }

  /** {@inheritDoc} */
  @Override
  protected <T> T produceInConnection(final Function<DSLContext, T> usingConnection) {
    return database.withConnection(
        (ConnectionCallable<T>) c -> usingConnection.apply(DSL.using(c, getDialect())));
  }

  /** {@inheritDoc} */
  @Override
  protected <T> T produceInTransaction(final Function<DSLContext, T> usingTransaction) {
    return database.withTransaction(
        (ConnectionCallable<T>) c -> usingTransaction.apply(DSL.using(c, getDialect())));
  }

}

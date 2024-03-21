package orm;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;

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

}

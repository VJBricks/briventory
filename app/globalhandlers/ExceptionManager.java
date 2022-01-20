package globalhandlers;

import database.BriventoryDBException;
import play.Logger;
import play.Logger.ALogger;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

/**
 * {@code ExceptionManager} aims to manage the various exceptions that occurs during the runtime. It allows registering
 * Exception Handlers to inform that a problem has occurred. For example, those handlers can send the exceptions through
 * mails or in a logger.
 * <p>In a dependencies injected environment, {@code ExceptionManager} will be instanced only once.</p>
 */
@Singleton
public final class ExceptionManager {

  /* TODO allow to register handlers, instead of defining them in the class.*/
  /* TODO Categorize the exceptions. */

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link LinkedList}, acting as a queue, that contains all {@link Throwable} to handle. */
  private final LinkedList<Throwable> throwables;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  public ExceptionManager() { throwables = new LinkedList<>(); }

  // *******************************************************************************************************************
  // Subscription and handling
  // *******************************************************************************************************************

  /**
   * Adds a {@link SQLException} to the queue.
   *
   * @param sqlException the {@link SQLException} instance to add.
   */
  public void addDatabaseException(final SQLException sqlException) {
    throwables.addLast(sqlException);
    handleQueue();
  }

  /**
   * Adds a {@link BriventoryDBException} to the queue.
   *
   * @param briventoryDBException the {@link BriventoryDBException} instance to add.
   */
  public void handleDatabaseException(final BriventoryDBException briventoryDBException) {
    throwables.addLast(briventoryDBException);
    handleQueue();
  }

  private void log(final Throwable throwable) {
    final ALogger alogger = Logger.of(ExceptionManager.class);
    if (alogger.isErrorEnabled())
      Logger.of(ExceptionManager.class).error(throwable.getMessage());
  }

  private void mail(final Throwable throwable) {
    final ALogger alogger = Logger.of(ExceptionManager.class);
    if (alogger.isErrorEnabled())
      // TODO send by e-mail
      Logger.of(ExceptionManager.class).error("TODO E-Mail : " + throwable.getMessage());
  }

  private void handleQueue() {
    synchronized (throwables) {
      CompletableFuture.supplyAsync(() -> {
        throwables.removeIf(throwable -> {
          log(throwable);
          mail(throwable);
          return true;
        });
        return null;
      });
    }
  }

}

package database;

import java.io.Serial;

/** A {@code BriventoryDBException} will be thrown in case of error while using a {@link BriventoryDB} instance. */
public final class BriventoryDBException extends RuntimeException {
  // *******************************************************************************************************************
  // Constants
  // *******************************************************************************************************************
  /** The serial version UID of this class. */
  @Serial private static final long serialVersionUID = -7711958890226601689L;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link BriventoryDBException}.
   *
   * @param message the error message.
   */
  public BriventoryDBException(final String message) {
    super(message);
  }

  /**
   * Creates a new {@link BriventoryDBException}.
   *
   * @param cause the {@link Throwable} at the ground of the problem.
   */
  public BriventoryDBException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new {@link BriventoryDBException}.
   *
   * @param message the error message.
   * @param cause the {@link Throwable} at the ground of the problem.
   */
  public BriventoryDBException(final String message, final Throwable cause) {
    super(message, cause);
  }

}

package database;

public final class BriventoryDBException extends RuntimeException {
  private static final long serialVersionUID = -7711958890226601689L;

  public BriventoryDBException(final String message) {
    super(message);
  }

  public BriventoryDBException(final Throwable cause) {
    super(cause);
  }

  public BriventoryDBException(final String message, final Throwable cause) {
    super(message, cause);
  }

}

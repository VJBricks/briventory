package orm;

import java.io.Serial;

public class RepositoryException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1912339798092539638L;

  public RepositoryException(final String message) { super(message); }

  public RepositoryException(final String message, final Throwable cause) {
    super(message, cause);
  }

}

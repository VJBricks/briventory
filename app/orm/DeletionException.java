package orm;

import org.jooq.UpdatableRecord;

import java.io.Serial;

public final class DeletionException extends RuntimeException {

  @Serial private static final long serialVersionUID = -7691601787934250620L;

  private final Class<?> concerned;

  public <V, R extends UpdatableRecord<R>> DeletionException(final Class<?> aClass) {
    concerned = aClass;
  }

}

package orm;

import java.io.Serial;

public class PersistenceException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 7118997857840804872L;

  private final Class<?> persistableModel;

  public PersistenceException(final Class<?> persisableModel) {
    this.persistableModel = persisableModel;
  }

  public Class<?> getPersistableModel() { return persistableModel; }

}

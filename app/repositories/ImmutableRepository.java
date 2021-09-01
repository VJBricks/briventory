package repositories;

import database.BriventoryDB;

/**
 * Base class for all immutable entities.
 *
 * @param <E> the type of the immutable entity handled.
 */
public abstract class ImmutableRepository<E> {

  /** The {@link BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;

  protected ImmutableRepository(final BriventoryDB briventoryDB) {
    this.briventoryDB = briventoryDB;
  }

  protected final BriventoryDB getBriventoryDB() {
    return briventoryDB;
  }

}

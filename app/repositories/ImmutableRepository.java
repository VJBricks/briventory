package repositories;

import database.BriventoryDB;

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

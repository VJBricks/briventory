package repositories;

import database.BriventoryDB;
import models.LockedUser;

import javax.inject.Inject;

public class LockedUserRepository extends MutableRepository<LockedUser> {

  @Inject
  protected LockedUserRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

}

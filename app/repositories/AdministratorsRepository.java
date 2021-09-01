package repositories;

import database.BriventoryDB;
import models.Administrator;
import org.hibernate.Session;

import javax.inject.Inject;
import javax.inject.Singleton;

import static database.BriventoryDB.CACHE_REGION;

@Singleton
public final class AdministratorsRepository extends MutableRepository<Administrator> {

  @Inject
  public AdministratorsRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  @Override
  protected boolean shallDelete(final Session session, final Administrator admin) {
    return canDelete(session, admin);
  }

  private static boolean canDelete(final Session session, final Administrator adminToRemove) {
    final String query =
        "select count(a) " +
        "from Administrator a " +
        "left join LockedUser lu on a.id = lu.idUser " +
        "where " +
        "lu.idUser is null and " +
        "a.id != :id";
    final long count = session.createQuery(query, Long.class)
                              .setParameter("id", adminToRemove.getId())
                              .setCacheable(true)
                              .setCacheRegion(CACHE_REGION)
                              .getSingleResult();
    return count > 0;
  }

  /** @return {@code true} if the database contains at least one non-locked administrator, otherwise {@code false}. */
  public boolean hasActiveAdministrator() {
    return getBriventoryDB().query(session -> {
      final String query =
          "select count(a) " +
          "from Administrator a " +
          "left join LockedUser lu on a.id = lu.idUser " +
          "where lu.idUser is null";
      final long count = session.createQuery(query, Long.class)
                                .setCacheable(true)
                                .setCacheRegion(CACHE_REGION)
                                .getSingleResult();
      return count > 0;
    }).join();
  }
}

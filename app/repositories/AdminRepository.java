package repositories;

import database.BriventoryDB;
import models.Administrator;
import org.hibernate.CacheMode;
import org.hibernate.Session;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

import static database.BriventoryDB.CACHE_REGION;

@Singleton
public final class AdminRepository extends MutableRepository<Administrator> {

  @Inject
  public AdminRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  @Override
  protected boolean shallPersist(final Session session, final Administrator admin) { return true; }

  @Override
  protected boolean shallDelete(final Session session, final Administrator admin) {
    return canDelete(session, admin);
  }

  private static boolean canDelete(final Session session, final Administrator adminToRemove) {
    final String query =
        "select count(a) " +
        "from Administrator a " +
        "left join LockedUser lu on a.id = lu.id " +
        "where " +
        "lu.id is null and " +
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
          "left join LockedUser lu on a.id = lu.id " +
          "where lu.id is null";
      final long count = session.createQuery(query, Long.class)
                                .setCacheable(true)
                                .setCacheRegion(CACHE_REGION)
                                .getSingleResult();
      return count > 0;
    }).join();
  }

  public List<Administrator> getAll() {
    return getBriventoryDB().query(session -> session.createQuery("select a from Administrator a", Administrator.class)
                                                     .setCacheMode(CacheMode.NORMAL)
                                                     .setCacheRegion(CACHE_REGION)
                                                     .setCacheable(true)
                                                     .getResultList()).join();
  }

  /**
   * Retrieves all {@link Administrator}s using the e-mail address given. Normally, e-mail addresses are unique, so a
   * singleton list or an empty one should be returned by this method.
   *
   * @param email the e-mail address.
   *
   * @return a list of {@link Administrator} instance.
   */
  public Optional<Administrator> findByEmail(final String email) {
    return getBriventoryDB()
        .query(
            session -> session.createQuery("select a from Administrator a where a.email = :email", Administrator.class)
                              .setParameter("email", email)
                              .setCacheMode(CacheMode.NORMAL)
                              .setCacheRegion(CACHE_REGION)
                              .setCacheable(true)
                              .uniqueResultOptional()).join();
  }

}

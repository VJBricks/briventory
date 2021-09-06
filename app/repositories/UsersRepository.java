package repositories;

import database.BriventoryDB;
import models.User;
import org.hibernate.CacheMode;
import org.hibernate.Session;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

import static database.BriventoryDB.CACHE_REGION;

@Singleton
public final class UsersRepository extends MutableRepository<User> {

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  @Inject
  private UsersRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // MutableRepository Overrides
  // *******************************************************************************************************************

  /**
   * Shall the given entity be persisted ? By default, this method returns {@code true}. The {@link Session} can be used
   * to perform checks in the database.
   * <p>If the entity cannot be persisted, a {@link database.BriventoryDBException} will be thrown during the
   * persistence process.</p>
   *
   * @param session the {@link Session}.
   * @param user the {@link User} to persist (insert or update).
   *
   * @return {@code true} if the deletion succeeded, otherwise {@code false}.
   */
  @Override
  protected boolean shallPersist(final Session session, final User user) {
    return hasActiveAdministrator(session, user);
  }

  /**
   * Shall the given entity be deleted ? By default, this method returns {@code true}. The {@link Session} can be used
   * to perform checks in the database.
   * <p>If the entity cannot be deleted, a {@link database.BriventoryDBException} will be thrown during the deletion
   * process.</p>
   *
   * @param session the {@link Session}.
   * @param user the {@link User} to delete.
   *
   * @return {@code true} if the deletion succeeded, otherwise {@code false}.
   */
  @Override
  protected boolean shallDelete(final Session session, final User user) {
    return hasActiveAdministrator(session, user);
  }

  // *******************************************************************************************************************
  // Locked Users Matter
  // *******************************************************************************************************************
  public void lock(final User user) {
    user.lock();
    merge(user);
  }

  public void unlock(final User user) {
    if (user.isLocked()) {
      user.unlock();
      merge(user);
    }
  }

  public List<User> getLockedUsers() {
    return getBriventoryDB().query(session -> session.createQuery("select u " +
                                                                  "from User u " +
                                                                  "   inner join LockedUser lu on u.id = lu.id",
                                                                  User.class)
                                                     .setCacheMode(CacheMode.NORMAL)
                                                     .setCacheRegion(CACHE_REGION)
                                                     .setCacheable(true)
                                                     .getResultList()
    ).join();
  }

  // *******************************************************************************************************************
  // Administrator Matter
  // *******************************************************************************************************************

  private static boolean hasActiveAdministrator(final Session session, final User concernedUser) {
    if (concernedUser.getId() == null) return true;
    final String query =
        "select count(a) " +
        "from User u" +
        "   inner join Administrator a on u.id = a.id " +
        "   left join LockedUser lu on u.id = lu.id " +
        "where " +
        "   u.id != :id and " +
        "   lu.idUser is null";
    final long count = session.createQuery(query, Long.class)
                              .setParameter("id", concernedUser.getId())
                              .setCacheable(false)
                              .getSingleResult();
    return count > 0;
  }

  public void setAdministrator(final User user, final boolean isAdministrator) {
    user.setAdministrator(isAdministrator);
    merge(user);
  }

  public List<User> getAdministrators() {
    return getBriventoryDB().query(session -> session.createQuery("select u " +
                                                                  "from User u " +
                                                                  "   inner join Administrator a on u.id = a.id",
                                                                  User.class)
                                                     .setCacheMode(CacheMode.NORMAL)
                                                     .setCacheRegion(CACHE_REGION)
                                                     .setCacheable(true)
                                                     .getResultList()
    ).join();
  }

  /** @return {@code true} if the database contains at least one non-locked administrator, otherwise {@code false}. */
  public boolean hasActiveAdministrator() {
    return getBriventoryDB().query(session -> {
      final String query =
          "select count(a) " +
          "from Administrator a " +
          "   left join LockedUser lu on a.id = lu.id " +
          "where lu.idUser is null";
      final long count = session.createQuery(query, Long.class)
                                .setCacheable(true)
                                .setCacheRegion(CACHE_REGION)
                                .getSingleResult();
      return count > 0;
    }).join();
  }

  // *******************************************************************************************************************
  // Data Retrieval
  // *******************************************************************************************************************

  /** @return all {@link User}s in the database. */
  public List<User> getAll() {
    return getBriventoryDB().query(session -> session.createQuery("select u from User u", User.class)
                                                     .setCacheMode(CacheMode.NORMAL)
                                                     .setCacheRegion(CACHE_REGION)
                                                     .setCacheable(true)
                                                     .getResultList())
                            .join();
  }

  /**
   * Retrieves the {@link User} corresponding to the id provided.
   *
   * @param id the id of the {@link User} to search for.
   *
   * @return an {@link Optional} containing the user.
   */
  public Optional<User> findById(final long id) {
    return getBriventoryDB()
        .query(
            session -> session.createQuery("select u from User u where u.id = :id", User.class)
                              .setParameter("id", id)
                              .setCacheMode(CacheMode.NORMAL)
                              .setCacheRegion(CACHE_REGION)
                              .setCacheable(true)
                              .uniqueResultOptional()).join();
  }

  /**
   * Retrieves all {@link User}s using the e-mail address given. Normally, e-mail addresses are unique, so a singleton
   * list or an empty one should be returned by this method.
   *
   * @param email the e-mail address.
   *
   * @return a list of {@link User} instance.
   */
  public Optional<User> findByEmail(final String email) {
    return getBriventoryDB().query(session ->
                                       session.createQuery("select u from User u where u.email = :email", User.class)
                                              .setParameter("email", email)
                                              .setCacheMode(CacheMode.NORMAL)
                                              .setCacheRegion(CACHE_REGION)
                                              .setCacheable(true)
                                              .uniqueResultOptional())
                            .join();
  }

}

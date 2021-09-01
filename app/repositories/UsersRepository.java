package repositories;

import database.BriventoryDB;
import models.User;
import org.hibernate.CacheMode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

import static database.BriventoryDB.CACHE_REGION;

@Singleton
public final class UsersRepository extends MutableRepository<User> {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  /** The injected {@link LockedUserRepository} instance. */
  private final LockedUserRepository lockedUserRepository;
  /** The injected {@link AdministratorsRepository} instance. */
  private final AdministratorsRepository administratorsRepository;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  @Inject
  private UsersRepository(final BriventoryDB briventoryDB, final LockedUserRepository lockedUserRepository,
                          final AdministratorsRepository administratorsRepository) {
    super(briventoryDB);
    this.lockedUserRepository = lockedUserRepository;
    this.administratorsRepository = administratorsRepository;
  }

  // *******************************************************************************************************************
  // Locked Users Matter
  // *******************************************************************************************************************
  public void lock(final User user) {
    user.lock();
    persist(user);
  }

  public void unlock(final User user) {
    if (user.isLocked()) {
      lockedUserRepository.delete(user.getLockedUser());
      user.unlock();
    }
  }

  public List<User> getLockedUsers() {
    return getBriventoryDB().query(session -> session.createQuery("select u " +
                                                                  "from User u " +
                                                                  "inner join LockedUser lu on u.id = lu.idUser",
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

  public void setAdministrator(final User user, final boolean isAdministrator) {
    if (isAdministrator) {
      user.setAdministrator(isAdministrator);
      persist(user);
    } else {
      administratorsRepository.delete(user.getAdministrator());
      user.setAdministrator(isAdministrator);
    }
  }

  public List<User> getAdministrators() {
    return getBriventoryDB().query(session -> session.createQuery("select u " +
                                                                  "from User u " +
                                                                  "inner join Administrator a on u.id = a.id",
                                                                  User.class)
                                                     .setCacheMode(CacheMode.NORMAL)
                                                     .setCacheRegion(CACHE_REGION)
                                                     .setCacheable(true)
                                                     .getResultList()
    ).join();
  }

  /** @return all {@link User}s in the database. */
  public List<User> getAll() {
    return getBriventoryDB().query(session -> session.createQuery("select u from User u", User.class)
                                                     .setCacheMode(CacheMode.NORMAL)
                                                     .setCacheRegion(CACHE_REGION)
                                                     .setCacheable(true)
                                                     .getResultList()).join();
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
    return getBriventoryDB()
        .query(
            session -> session.createQuery("select u from User u where u.email = :email", User.class)
                              .setParameter("email", email)
                              .setCacheMode(CacheMode.NORMAL)
                              .setCacheRegion(CACHE_REGION)
                              .setCacheable(true)
                              .uniqueResultOptional()).join();
  }

}

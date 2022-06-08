package models;

import junit5.J5WithApplication;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import orm.PersistenceException;
import repositories.AccountsRepository;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest extends J5WithApplication {

  // *******************************************************************************************************************
  // Test data
  // *******************************************************************************************************************

  private static final String BRUCE_EMAIL = "bruce.wayne@night.gotham.com";
  private static final String BRUCE_FIRSTNAME = "Bruce";
  private static final String BRUCE_LASTNAME = "Wayne";
  private static final String BRUCE_PASS = "I am Batman !!";

  private static final String JOKER_EMAIL = "joker@vilain.gotham.com";
  private static final String JOKER_FIRSTNAME = "The";
  private static final String JOKER_LASTNAME = "Joker";
  private static final String JOKER_PASS = "joker493";

  private static final String KATHY_EMAIL = "kathy.kane@city.gotham.com";
  private static final String KATHY_FIRSTNAME = "Kathy";
  private static final String KATHY_LASTNAME = "Kane";
  private static final String KATHY_PASS = "kathy233";

  private static final String BARBARA_EMAIL = "barbara.gordon@city.gotham.com";
  private static final String BARBARA_FIRSTNAME = "Barbara";
  private static final String BARBARA_LASTNAME = "Gordon";
  private static final String BARBARA_PASS = "barbara4";

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  /** The injected {@link AccountsRepository} instance. */
  private AccountsRepository accountsRepository;

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  private final LinkedList<Long> ids = new LinkedList<>();

  // *******************************************************************************************************************
  // Global test methods.
  // *******************************************************************************************************************
  @BeforeEach
  public void setUp() {
    assertDoesNotThrow(() -> {
      if (accountsRepository == null)
        accountsRepository = instanceOf(AccountsRepository.class);
      assertNotNull(accountsRepository);
      ids.clear();

      if (!accountsRepository.hasActiveAdministrator()) {
        Account account = new Account().setFirstname(BRUCE_FIRSTNAME)
                                       .setLastname(BRUCE_LASTNAME)
                                       .setEmail(BRUCE_EMAIL)
                                       .setClearPassword(BRUCE_PASS)
                                       .setAdministrator(true)
                                       .setLocked(false);
        accountsRepository.persist(account);
      }
    });
  }

  @AfterEach
  public void tearDown() {
    assertDoesNotThrow(() -> {
      assertNotNull(accountsRepository);
      ids.forEach(id -> accountsRepository.findById(id).ifPresent(accountsRepository::delete));
      accountsRepository.findByEmail(KATHY_EMAIL).ifPresent(accountsRepository::delete);
      accountsRepository.findByEmail(BARBARA_EMAIL).ifPresent(accountsRepository::delete);
      accountsRepository.findByEmail(JOKER_EMAIL).ifPresent(accountsRepository::delete);
    });
  }

  // *******************************************************************************************************************
  // Test methods.
  // *******************************************************************************************************************
  @Test
  void adminInsertRemove() {
    assertNotNull(accountsRepository);
    assertDoesNotThrow(() -> {
      Account account = new Account().setFirstname(KATHY_FIRSTNAME)
                                     .setLastname(KATHY_LASTNAME)
                                     .setEmail(KATHY_EMAIL)
                                     .setClearPassword(KATHY_PASS)
                                     .setAdministrator(true);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
    });

    final var kathy = accountsRepository.findByEmail(KATHY_EMAIL);
    assertTrue(kathy.isPresent());
    assertEquals(KATHY_EMAIL, kathy.get().getEmail());

    assertDoesNotThrow(() -> accountsRepository.delete(kathy.get()));
  }

  @Test
  void adminInsertMissingFirstname() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setLastname(BARBARA_LASTNAME)
                                     .setEmail(BARBARA_EMAIL)
                                     .setClearPassword(BARBARA_PASS)
                                     .setAdministrator(true);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void adminInsertMissingLastname() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname(BARBARA_FIRSTNAME)
                                     .setEmail(BARBARA_EMAIL)
                                     .setClearPassword(BARBARA_PASS)
                                     .setAdministrator(true);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void adminInsertEmptyName() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setEmail(BARBARA_EMAIL)
                                     .setClearPassword(BARBARA_PASS)
                                     .setAdministrator(true);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void adminInsertMissingEmail() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname(BARBARA_FIRSTNAME)
                                     .setLastname(BARBARA_LASTNAME)
                                     .setClearPassword(BARBARA_PASS)
                                     .setAdministrator(true);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void adminInsertEmptyEmail() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname(BARBARA_FIRSTNAME)
                                     .setLastname(BARBARA_LASTNAME)
                                     .setEmail("")
                                     .setClearPassword(BARBARA_PASS)
                                     .setAdministrator(true);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void adminInsertBadEmail() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname(BARBARA_FIRSTNAME)
                                     .setLastname(BARBARA_LASTNAME)
                                     .setEmail("barbara.gordon-city.gc")
                                     .setClearPassword(BARBARA_PASS)
                                     .setAdministrator(true);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void adminInsertMissingPassword() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname(BARBARA_FIRSTNAME)
                                     .setLastname(BARBARA_LASTNAME)
                                     .setEmail(BARBARA_EMAIL)
                                     .setAdministrator(true);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void adminInsertEmptyPassword() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname(BARBARA_FIRSTNAME)
                                     .setLastname(BARBARA_LASTNAME)
                                     .setEmail(BARBARA_EMAIL)
                                     .setAdministrator(true);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void adminRemoveAll() {
    assertNotNull(accountsRepository);
    assertThrows(DataAccessException.class, () -> { // NOSONAR
      accountsRepository.delete(accountsRepository.getAdministrators());
    });
  }

  @Test
  void adminLockUnlock() {
    assertNotNull(accountsRepository);

    assertDoesNotThrow(() -> {
      Account account = new Account().setFirstname(JOKER_FIRSTNAME)
                                     .setLastname(JOKER_LASTNAME)
                                     .setEmail(JOKER_EMAIL)
                                     .setClearPassword(JOKER_PASS)
                                     .setAdministrator(true);

      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());

      assertFalse(accountsRepository.isLocked(account));

      account.setLocked(true);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
      assertTrue(accountsRepository.isLocked(account));

      account.setLocked(false);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
      assertFalse(accountsRepository.isLocked(account));
    });

  }

}

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

class AccountTest extends J5WithApplication {

  // *******************************************************************************************************************
  // Test data
  // *******************************************************************************************************************
  private static final String BRUCE_EMAIL = "bruce.wayne@night.gotham.com";
  private static final String BRUCE_FIRSTNAME = "Bruce";
  private static final String BRUCE_LASTNAME = "Wayne";
  private static final String BRUCE_PASS = "I am Batman !!";

  private static final String HARLEY_EMAIL = "harley.quinn@vilain.gotham.com";
  private static final String HARLEY_FIRSTNAME = "Harley";
  private static final String HARLEY_LASTNAME = "Quinn";
  private static final String HARLEY_PASS = "hquinn685";

  private static final String CANARY_EMAIL = "black.canary@city.gotham.com";
  private static final String CANARY_FIRSTNAME = "Black";
  private static final String CANARY_LASTNAME = "Canary";
  private static final String CANARY_PASS = "canary23Black";

  private static final String AQUAMAN_EMAIL = "aquaman@city.gotham.com";
  private static final String AQUAMAN_FIRSTNAME = "Aquaman";
  private static final String AQUAMAN_LASTNAME = "Of The Ocean";
  private static final String AQUAMAN_PASS = "aquaman33";

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
      accountsRepository.findByEmail(HARLEY_EMAIL).ifPresent(accountsRepository::delete);
      accountsRepository.findByEmail(CANARY_EMAIL).ifPresent(accountsRepository::delete);
      accountsRepository.findByEmail(AQUAMAN_EMAIL).ifPresent(accountsRepository::delete);
    });
  }

  // *******************************************************************************************************************
  // Test methods.
  // *******************************************************************************************************************

  @Test
  void accountInsertRemove() {
    assertNotNull(accountsRepository);
    assertDoesNotThrow(() -> {
      Account account = new Account().setFirstname(HARLEY_FIRSTNAME)
                                     .setLastname(HARLEY_LASTNAME)
                                     .setEmail(HARLEY_EMAIL)
                                     .setClearPassword(HARLEY_PASS);
      accountsRepository.persist(account);
      if (!ids.contains(account.getId())) ids.add(account.getId());
    });

    final var harley = accountsRepository.findByEmail(HARLEY_EMAIL);
    assertTrue(harley.isPresent());
    assertEquals(HARLEY_EMAIL, harley.get().getEmail());

    assertDoesNotThrow(() -> accountsRepository.delete(harley.get()));
  }

  @Test
  void accountInsertMissingName() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setEmail(CANARY_EMAIL)
                                     .setClearPassword(CANARY_PASS);
      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void accountInsertEmptyName() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname("")
                                     .setLastname("")
                                     .setEmail(CANARY_EMAIL)
                                     .setClearPassword(CANARY_PASS);
      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void accountInsertMissingEmail() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname(CANARY_FIRSTNAME)
                                     .setLastname(CANARY_LASTNAME)
                                     .setClearPassword(CANARY_PASS);
      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void accountInsertEmptyEmail() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname(CANARY_FIRSTNAME)
                                     .setLastname(CANARY_LASTNAME)
                                     .setEmail("")
                                     .setClearPassword(CANARY_PASS);
      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void accountInsertBadEmail() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname(CANARY_FIRSTNAME)
                                     .setLastname(CANARY_LASTNAME)
                                     .setEmail("black.canary-city.gc")
                                     .setClearPassword(CANARY_PASS);
      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void accountInsertMissingPassword() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account user = new Account().setFirstname(CANARY_FIRSTNAME)
                                  .setLastname(CANARY_LASTNAME)
                                  .setEmail(CANARY_EMAIL);

      accountsRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void accountInsertEmptyPassword() {
    assertNotNull(accountsRepository);
    assertThrows(PersistenceException.class, () -> { // NOSONAR
      Account account = new Account().setFirstname(CANARY_FIRSTNAME)
                                     .setLastname(CANARY_LASTNAME)
                                     .setEmail(CANARY_EMAIL)
                                     .setClearPassword("");
      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());
    });
  }

  @Test
  void accountRemoveAll() {
    assertNotNull(accountsRepository);
    final var accounts = accountsRepository.getAll();
    assertThrows(DataAccessException.class, () -> accountsRepository.delete(accounts));
  }

  @Test
  void accountLockUnlock() {
    assertNotNull(accountsRepository);

    assertDoesNotThrow(() -> {
      Account account = new Account().setFirstname(AQUAMAN_FIRSTNAME)
                                     .setLastname(AQUAMAN_LASTNAME)
                                     .setEmail(AQUAMAN_EMAIL)
                                     .setClearPassword(AQUAMAN_PASS);

      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());
      assertFalse(account.isLocked());
      assertFalse(accountsRepository.getLockedAccounts().contains(account));

      account.setLocked(true);
      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());
      assertTrue(account.isLocked());
      assertTrue(accountsRepository.getLockedAccounts().contains(account));

      account.setLocked(false);
      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());
      assertFalse(account.isLocked());
      assertFalse(accountsRepository.getLockedAccounts().contains(account));
    });

  }

}

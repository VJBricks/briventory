package models;

import junit5.J5WithApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.AccountsRepository;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class AccountMigrationTest extends J5WithApplication {

  private static final String BRUCE_EMAIL = "bruce.wayne@night.gotham.com";
  private static final String BRUCE_FIRSTNAME = "Bruce";
  private static final String BRUCE_LASTNAME = "Wayne";
  private static final String BRUCE_PASS = "I am Batman !!";

  private static final String JOKER_EMAIL = "jocker@vilain.gotham.com";
  private static final String JOKER_FIRSTNAME = "The";
  private static final String JOKER_LASTNAME = "Joker";
  private static final String JOKER_PASS = "joker493";

  private static final String HARLEY_EMAIL = "harley.quinn@vilain.gotham.com";
  private static final String HARLEY_FIRSTNAME = "Harley";
  private static final String HARLEY_LASTNAME = "Quinn";
  private static final String HARLEY_PASS = "h_quinn685";

  private AccountsRepository accountsRepository;

  private final LinkedList<Long> ids = new LinkedList<>();

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
      accountsRepository.findByEmail(JOKER_EMAIL).ifPresent(accountsRepository::delete);
    });
  }

  @Test
  void migrateAccountToAdmin() {
    assertNotNull(accountsRepository);

    Account account = new Account().setFirstname(HARLEY_FIRSTNAME)
                                   .setLastname(HARLEY_LASTNAME)
                                   .setEmail(HARLEY_EMAIL)
                                   .setClearPassword(HARLEY_PASS);

    assertDoesNotThrow(() -> {
      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());

      account.setAdministrator(true);
      accountsRepository.persist(account);
      assertTrue(accountsRepository.getAdministrators().contains(account));
    });

  }

  @Test
  void migrateAdminToAccount() {
    assertNotNull(accountsRepository);

    Account account = new Account().setFirstname(JOKER_FIRSTNAME)
                                   .setLastname(JOKER_LASTNAME)
                                   .setEmail(JOKER_EMAIL)
                                   .setClearPassword(JOKER_PASS)
                                   .setAdministrator(true);

    assertDoesNotThrow(() -> {
      accountsRepository.persist(account);
      if (account.getId() != null && !ids.contains(account.getId())) ids.add(account.getId());

      assertTrue(accountsRepository.getAll().contains(account));
      assertTrue(accountsRepository.getAdministrators().contains(account));

      account.setAdministrator(false);
      accountsRepository.persist(account);
      assertTrue(accountsRepository.getAll().contains(account));
      assertFalse(accountsRepository.getAdministrators().contains(account));

      accountsRepository.findByEmail(JOKER_EMAIL).ifPresent(u -> assertFalse(u.isAdministrator()));
    });

  }

}

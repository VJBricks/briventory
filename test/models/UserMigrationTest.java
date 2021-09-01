package models;

import junit5.J5WithApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.AdministratorsRepository;
import repositories.UsersRepository;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class UserMigrationTest extends J5WithApplication {

  private static final String JOKER_EMAIL = "jocker@vilain.gc";
  private static final String JOKER_NAME = "The Jocker";
  private static final String JOKER_PASS = "jocker493";

  private static final String HARLEY_EMAIL = "harley.quinn@vilain.gc";
  private static final String HARLEY_NAME = "Harley Quinn";
  private static final String HARLEY_PASS = "hquinn685";

  private AdministratorsRepository administratorsRepository;
  private UsersRepository usersRepository;

  private final LinkedList<Long> ids = new LinkedList<>();

  @BeforeEach
  public void setUp() {
    assertDoesNotThrow(() -> {
      administratorsRepository = getApp().injector().instanceOf(AdministratorsRepository.class);
      assertNotNull(administratorsRepository);
      usersRepository = getApp().injector().instanceOf(UsersRepository.class);
      assertNotNull(usersRepository);
      ids.clear();
    });
  }

  @AfterEach
  public void tearDown() {
    assertDoesNotThrow(() -> {
      assertNotNull(usersRepository);
      ids.forEach(id -> usersRepository.findById(id).ifPresent(usersRepository::delete));
      usersRepository.findByEmail(HARLEY_EMAIL).ifPresent(usersRepository::delete);
      usersRepository.findByEmail(JOKER_EMAIL).ifPresent(usersRepository::delete);
    });
  }

  @Test
  void migrateUserToAdmin() {
    assertNotNull(usersRepository);
    assertNotNull(administratorsRepository);

    User user = new User();
    user.setName(HARLEY_NAME);
    user.setEmail(HARLEY_EMAIL);
    user.setClearPassword(HARLEY_PASS);

    assertDoesNotThrow(() -> {
      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());

      usersRepository.setAdministrator(user, true);
      assertTrue(usersRepository.getAdministrators().contains(user));
    });

  }

  @Test
  void migrateAdminToUser() {
    assertNotNull(administratorsRepository);
    assertNotNull(usersRepository);

    User user = new User();
    user.setName(JOKER_NAME);
    user.setEmail(JOKER_EMAIL);
    user.setClearPassword(JOKER_PASS);
    user.setAdministrator(true);

    assertDoesNotThrow(() -> {
      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());

      System.out.println(usersRepository.getAll());
      assertTrue(usersRepository.getAll().contains(user));
      System.out.println(usersRepository.getAdministrators());
      assertTrue(usersRepository.getAdministrators().contains(user));

      usersRepository.setAdministrator(user, false);
      System.out.println(usersRepository.getAll());
      assertTrue(usersRepository.getAll().contains(user));
      System.out.println(usersRepository.getAdministrators());
      assertFalse(usersRepository.getAdministrators().contains(user));

      usersRepository.findByEmail(JOKER_EMAIL).ifPresent(u -> assertFalse(u.isAdministrator()));
    });

  }

}

package models;

import database.BriventoryDBException;
import junit5.J5WithApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.AdministratorsRepository;
import repositories.UsersRepository;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest extends J5WithApplication {

  private static final String JOKER_EMAIL = "joker@vilain.gc";
  private static final String JOKER_NAME = "The Joker";
  private static final String JOKER_PASS = "joker493";

  private static final String KATHY_EMAIL = "kathy.kane@city.gc";
  private static final String KATHY_NAME = "Kathy Kane";
  private static final String KATHY_PASS = "kathy233";

  private static final String BARBARA_EMAIL = "barbara.gordon@city.gc";
  private static final String BARBARA_NAME = "Barbara Gordon";
  private static final String BARBARA_PASS = "barbara4";

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
      usersRepository.findByEmail(KATHY_EMAIL).ifPresent(usersRepository::delete);
      usersRepository.findByEmail(BARBARA_EMAIL).ifPresent(usersRepository::delete);
      usersRepository.findByEmail(JOKER_EMAIL).ifPresent(usersRepository::delete);
    });
  }

  @Test
  void adminInsertRemove() {
    assertNotNull(usersRepository);
    assertDoesNotThrow(() -> {
      User user = new User();
      user.setName(KATHY_NAME);
      user.setEmail(KATHY_EMAIL);
      user.setClearPassword(KATHY_PASS);
      user.setAdministrator(true);
      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });

    final var kathy = usersRepository.findByEmail(KATHY_EMAIL);
    assertTrue(kathy.isPresent());
    assertEquals(KATHY_EMAIL, kathy.get().getEmail());

    assertDoesNotThrow(() -> usersRepository.delete(kathy.get()));
  }

  @Test
  void adminInsertMissingName() {
    assertNotNull(usersRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setEmail(BARBARA_EMAIL);
      user.setClearPassword(BARBARA_PASS);
      user.setAdministrator(true);
      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void adminInsertEmptyName() {
    assertNotNull(usersRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setEmail(BARBARA_EMAIL);
      user.setClearPassword(BARBARA_PASS);
      user.setAdministrator(true);
      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void adminInsertMissingEmail() {
    assertNotNull(usersRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName(BARBARA_NAME);
      user.setClearPassword(BARBARA_PASS);
      user.setAdministrator(true);
      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void adminInsertEmptyEmail() {
    assertNotNull(usersRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName("Barbara Gordon");
      user.setEmail("");
      user.setClearPassword(BARBARA_PASS);
      user.setAdministrator(true);
      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void adminInsertBadEmail() {
    assertNotNull(usersRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName(BARBARA_NAME);
      user.setEmail("barbara.gordon-city.gc");
      user.setClearPassword(BARBARA_PASS);
      user.setAdministrator(true);
      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void adminInsertMissingPassword() {
    assertNotNull(usersRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName(BARBARA_NAME);
      user.setEmail(BARBARA_EMAIL);
      user.setAdministrator(true);
      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void adminInsertEmptyPassword() {
    assertNotNull(administratorsRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName(BARBARA_NAME);
      user.setEmail(BARBARA_EMAIL);
      user.setAdministrator(true);
      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void adminRemoveAll() {
    assertNotNull(usersRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      usersRepository.delete(usersRepository.getAdministrators());
    });
  }

  @Test
  void adminLockUnlock() {
    assertNotNull(administratorsRepository);

    assertDoesNotThrow(() -> {
      User user = new User();
      user.setName(JOKER_NAME);
      user.setEmail(JOKER_EMAIL);
      user.setClearPassword(JOKER_PASS);
      user.setAdministrator(true);

      usersRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
      assertFalse(user.isLocked());
      List<User> locked = usersRepository.getLockedUsers();
      assertFalse(locked.contains(user));

      usersRepository.lock(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
      assertTrue(user.isLocked());
      locked = usersRepository.getLockedUsers();
      assertTrue(locked.contains(user));

      usersRepository.unlock(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
      assertFalse(user.isLocked());
      locked = usersRepository.getLockedUsers();
      assertFalse(locked.contains(user));
    });

  }

}

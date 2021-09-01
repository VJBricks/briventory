package models;

import database.BriventoryDBException;
import junit5.J5WithApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.UsersRepository;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest extends J5WithApplication {

  private static final String HARLEY_EMAIL = "harley.quinn@vilain.gc";
  private static final String HARLEY_NAME = "Harley Quinn";
  private static final String HARLEY_PASS = "hquinn685";

  private static final String CANARY_EMAIL = "black.canary@city.gc";
  private static final String CANARY_NAME = "Black Canary";
  private static final String CANARY_PASS = "canary23Black";

  private static final String AQUAMAN_EMAIL = "aquaman@city.gc";
  private static final String AQUAMAN_NAME = "Aquaman";
  private static final String AQUAMAN_PASS = "aquaman33";

  private UsersRepository userRepository;

  private final LinkedList<Long> ids = new LinkedList<>();

  @BeforeEach
  public void setUp() {
    assertDoesNotThrow(() -> {
      userRepository = getApp().injector().instanceOf(UsersRepository.class);
      assertNotNull(userRepository);
      ids.clear();
    });
  }

  @AfterEach
  public void tearDown() {
    assertDoesNotThrow(() -> {
      assertNotNull(userRepository);
      ids.forEach(id -> userRepository.findById(id).ifPresent(userRepository::delete));
      userRepository.findByEmail(HARLEY_EMAIL).ifPresent(userRepository::delete);
      userRepository.findByEmail(CANARY_EMAIL).ifPresent(userRepository::delete);
      userRepository.findByEmail(AQUAMAN_EMAIL).ifPresent(userRepository::delete);
    });
  }

  @Test
  void userInsertRemove() {
    assertNotNull(userRepository);
    assertDoesNotThrow(() -> {
      User user = new User();
      user.setName(HARLEY_NAME);
      user.setEmail(HARLEY_EMAIL);
      user.setClearPassword(HARLEY_PASS);
      userRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });

    final var harley = userRepository.findByEmail(HARLEY_EMAIL);
    assertTrue(harley.isPresent());
    assertEquals(HARLEY_EMAIL, harley.get().getEmail());

    assertDoesNotThrow(() -> userRepository.delete(harley.get()));
  }

  @Test
  void userInsertMissingName() {
    assertNotNull(userRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setEmail(CANARY_EMAIL);
      user.setClearPassword(CANARY_PASS);
      userRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void userInsertEmptyName() {
    assertNotNull(userRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName("");
      user.setEmail(CANARY_EMAIL);
      user.setClearPassword(CANARY_PASS);
      userRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void userInsertMissingEmail() {
    assertNotNull(userRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName(CANARY_NAME);
      user.setClearPassword(CANARY_PASS);
      userRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void userInsertEmptyEmail() {
    assertNotNull(userRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName(CANARY_NAME);
      user.setEmail("");
      user.setClearPassword(CANARY_PASS);
      userRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void userInsertBadEmail() {
    assertNotNull(userRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName(CANARY_NAME);
      user.setEmail("black.canary-city.gc");
      user.setClearPassword(CANARY_PASS);
      userRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void userInsertMissingPassword() {
    assertNotNull(userRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName(CANARY_NAME);
      user.setEmail(CANARY_EMAIL);
      userRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void userInsertEmptyPassword() {
    assertNotNull(userRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      User user = new User();
      user.setName(CANARY_NAME);
      user.setEmail(CANARY_EMAIL);
      userRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
    });
  }

  @Test
  void UserRemoveAll() {
    assertNotNull(userRepository);
    assertThrows(BriventoryDBException.class, () -> { // NOSONAR
      userRepository.delete(userRepository.getAll());
    });
  }

  @Test
  void userLockUnlock() {
    assertNotNull(userRepository);

    assertDoesNotThrow(() -> {
      User user = new User();
      user.setName(AQUAMAN_NAME);
      user.setEmail(AQUAMAN_EMAIL);
      user.setClearPassword(AQUAMAN_PASS);

      userRepository.persist(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
      assertFalse(user.isLocked());
      List<User> locked = userRepository.getLockedUsers();
      assertFalse(locked.contains(user));

      userRepository.lock(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
      assertTrue(user.isLocked());
      locked = userRepository.getLockedUsers();
      assertTrue(locked.contains(user));

      userRepository.unlock(user);
      if (user.getId() != null && !ids.contains(user.getId())) ids.add(user.getId());
      assertFalse(user.isLocked());
      locked = userRepository.getLockedUsers();
      assertFalse(locked.contains(user));
    });

  }

}

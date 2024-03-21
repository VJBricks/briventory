package models;

import junit5.J5WithApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest extends J5WithApplication {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  /** The injected {@link ContainersRepository} instance. */
  private ContainersRepository containersRepository;
  /** The injected {@link SharedContainersRepository} instance. */
  private SharedContainersRepository sharedContainersRepository;
  /** The injected {@link PrivateContainersRepository} instance. */
  private PrivateContainersRepository privateContainersRepository;
  /** The injected {@link ContainerTypesRepository} instance. */
  private ContainerTypesRepository containerTypesRepository;
  /** The injected {@link AccountsRepository} instance. */
  private AccountsRepository accountsRepository;

  // *******************************************************************************************************************
  // Global test methods.
  // *******************************************************************************************************************

  private ContainerType containerType;

  @BeforeEach
  public void setUp() {
    assertDoesNotThrow(() -> {
      if (containersRepository == null)
        containersRepository = instanceOf(ContainersRepository.class);
      assertNotNull(containersRepository);

      if (sharedContainersRepository == null)
        sharedContainersRepository = instanceOf(SharedContainersRepository.class);
      assertNotNull(sharedContainersRepository);

      if (privateContainersRepository == null)
        privateContainersRepository = instanceOf(PrivateContainersRepository.class);
      assertNotNull(privateContainersRepository);

      if (containerTypesRepository == null)
        containerTypesRepository = instanceOf(ContainerTypesRepository.class);
      assertNotNull(containerTypesRepository);

      if (accountsRepository == null)
        accountsRepository = instanceOf(AccountsRepository.class);
      assertNotNull(accountsRepository);

      if (containerType == null) {
        containerType = new ContainerType("For Container tests",
                                          (short) 1,
                                          (short) 1,
                                          "%d");
        assertNotNull(containerType);
        containerTypesRepository.persist(containerType);
      }
    });

  }

  @AfterEach
  public void tearDown() {
    containersRepository.getAll().forEach(container -> {
      containersRepository.delete(container);
    });
    containerTypesRepository.delete(containerType);
  }

  @Test
  void sharedContainerInsert() {
    assertEquals(0, sharedContainersRepository.getSharedContainers().size());
    final SharedContainer sharedContainer = new SharedContainer(containerType);
    sharedContainersRepository.persist(sharedContainer);
    assertEquals(1, sharedContainersRepository.getSharedContainers().size());
    assertNotNull(sharedContainer.getId());
    containersRepository.delete(sharedContainer);
    assertEquals(0, sharedContainersRepository.getSharedContainers().size());
  }

  @Test
  void containersGetAll() {
    List<Container> containers = containersRepository.getAll();
    for (Container container : containers)
      System.out.println(container);
    assertTrue(containers.isEmpty());
  }

  @Test
  void sharedToPrivateMigration() {
    final Account account = accountsRepository.getAdministrators().get(0);
    final SharedContainer sharedContainer = new SharedContainer(containerType);
    sharedContainersRepository.persist(sharedContainer);
    final long idSharedContainer = sharedContainer.getId();
    final PrivateContainer privateContainer = privateContainersRepository.migrate(sharedContainer, account);
    assertNotNull(privateContainer);
    assertEquals(idSharedContainer, privateContainer.getId());
    assertEquals(0, sharedContainersRepository.getSharedContainers().size());
    assertEquals(1, privateContainersRepository.getPrivateContainers().size());
  }

  @Test
  void privateToSharedMigration() {
    final Account account = accountsRepository.getAdministrators().get(0);
    final PrivateContainer privateContainer = new PrivateContainer(containerType, account);
    privateContainersRepository.persist(privateContainer);
    final long idPrivateContainer = privateContainer.getId();
    final SharedContainer sharedContainer = sharedContainersRepository.migrate(privateContainer);
    assertNotNull(sharedContainer);
    assertEquals(idPrivateContainer, sharedContainer.getId());
    assertEquals(1, sharedContainersRepository.getSharedContainers().size());
    assertEquals(0, privateContainersRepository.getPrivateContainers().size());
  }

}

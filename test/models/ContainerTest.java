package models;

import database.BriventoryDB;
import junit5.J5WithApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.ContainerTypesRepository;
import repositories.ContainersRepository;
import repositories.SharedContainersRepository;

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
  /** The injected {@link ContainerTypesRepository} instance. */
  private ContainerTypesRepository containerTypesRepository;
  /** The injected {@link BriventoryDB} instance. */
  private BriventoryDB briventoryDB;

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

      if (containerTypesRepository == null)
        containerTypesRepository = instanceOf(ContainerTypesRepository.class);
      assertNotNull(containerTypesRepository);

      if (briventoryDB == null)
        briventoryDB = instanceOf(BriventoryDB.class);
      assertNotNull(briventoryDB);

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
    containerTypesRepository.delete(containerType);
  }

  @Test
  void sharedContainerInsert() {
    assertEquals(0, sharedContainersRepository.getSharedContainers().size());
    final SharedContainer sharedContainer = new SharedContainer(containerType);
    sharedContainersRepository.persist(sharedContainer);
    assertEquals(1, sharedContainersRepository.getSharedContainers().size());
    assertNotNull(sharedContainer.getId());
    sharedContainersRepository.delete(sharedContainer);
    assertEquals(0, sharedContainersRepository.getSharedContainers().size());
  }

  @Test
  void containersGetAll() {
    List<Container> containers = containersRepository.getContainers();
    for (Container container : containers)
      System.out.println(container);
    assertTrue(containers.isEmpty());
  }

}

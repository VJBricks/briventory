package models;

import database.BriventoryDB;
import junit5.J5WithApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.ContainerTypesRepository;
import repositories.ContainersRepository;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTypeTest extends J5WithApplication {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  /** The injected {@link ContainersRepository} instance. */
  private ContainersRepository containersRepository;
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

      if (containerTypesRepository == null)
        containerTypesRepository = instanceOf(ContainerTypesRepository.class);
      assertNotNull(containerTypesRepository);

      if (briventoryDB == null)
        briventoryDB = instanceOf(BriventoryDB.class);
      assertNotNull(briventoryDB);
    });

    containerType = new ContainerType("Foo",
                                      (short) 3,
                                      (short) 9,
                                      "%d");
    assertNotNull(containerType);
    assertTrue(containerTypesRepository.validate(containerType).isEmpty());
  }

  @Test
  void containerTypesAdditionAndRemoval() {
    assertDoesNotThrow(() -> {
      assertEquals(0, containerTypesRepository.getContainerTypes().size());

      final ContainerType stanleySmall = new ContainerType("Stanley small lockers",
                                                           (short) 1,
                                                           (short) 3,
                                                           "%04d");
      containerTypesRepository.persist(stanleySmall);

      assertEquals(1, containerTypesRepository.getContainerTypes().size());

      final ContainerType stanleyBig = new ContainerType("Stanley big lockers",
                                                         (short) 1,
                                                         (short) 4,
                                                         "%04d");
      containerTypesRepository.persist(stanleyBig);

      assertEquals(2, containerTypesRepository.getContainerTypes().size());

      final ContainerType rothoClearBox = new ContainerType("Rotho Clear Box",
                                                            (short) 3,
                                                            (short) 9,
                                                            "Box%03d");
      containerTypesRepository.persist(rothoClearBox);

      assertEquals(3, containerTypesRepository.getContainerTypes().size());

      for (ContainerType containerType : containerTypesRepository.getContainerTypes())
        containerTypesRepository.delete(containerType);

      assertEquals(0, containerTypesRepository.getContainerTypes().size());
    });
  }

  @Test
  void containerTypeNameValidity() {
    containerType.setName(null);
    var errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.name.blank", errors.get(0).message());

    containerType.setName("");
    errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.name.blank", errors.get(0).message());

    containerType.setName("             ");
    errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.name.blank", errors.get(0).message());

    containerType.setName("Foo");
    errors = containerTypesRepository.validate(containerType);
    assertEquals(0, errors.size());
    assertEquals("Foo", containerType.getName());
  }

  @Test
  void containerTypeMinLockersValidity() {
    containerType.setMinLockers((short) 0);
    var errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.minLockers.invalid", errors.get(0).message());

    containerType.setMinLockers((short) -1);
    errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.minLockers.invalid", errors.get(0).message());

    containerType.setMinLockers((short) 3);
    errors = containerTypesRepository.validate(containerType);
    assertEquals(0, errors.size());
    assertEquals(3, containerType.getMinLockers());
  }

  @Test
  void containerTypeMaxLockersValidity() {
    containerType.setMaxLockers((short) 3);
    var errors = containerTypesRepository.validate(containerType);
    assertEquals(0, errors.size());

    containerType.setMaxLockers((short) 2);
    errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.maxLockers.invalid", errors.get(0).message());

    containerType.setMaxLockers((short) -2);
    errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.maxLockers.invalid", errors.get(0).message());

    containerType.setMaxLockers((short) 9);
    errors = containerTypesRepository.validate(containerType);
    assertEquals(0, errors.size());
    assertEquals(9, containerType.getMaxLockers());
  }

  @Test
  void containerTypeNumberFormattingValidity() {
    containerType.setNumberFormatting(null);
    var errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.numberFormatting.blank", errors.get(0).message());

    containerType.setNumberFormatting("");
    errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.numberFormatting.blank", errors.get(0).message());

    containerType.setNumberFormatting("             ");
    errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.numberFormatting.blank", errors.get(0).message());

    containerType.setNumberFormatting("foo");
    errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.numberFormatting.invalid", errors.get(0).message());

    containerType.setNumberFormatting("%");
    errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.numberFormatting.invalid", errors.get(0).message());

    containerType.setNumberFormatting("%f");
    errors = containerTypesRepository.validate(containerType);
    assertEquals(1, errors.size());
    assertEquals("containerType.error.numberFormatting.invalid", errors.get(0).message());

    containerType.setNumberFormatting("%d");
    errors = containerTypesRepository.validate(containerType);
    assertEquals(0, errors.size());
    assertEquals("%d", containerType.getNumberFormatting());
    assertEquals("13", containerType.getLabel(13));

    containerType.setNumberFormatting("Box%04d");
    assertEquals("Box%04d", containerType.getNumberFormatting());
    assertEquals("Box0013", containerType.getLabel(13));
  }

}

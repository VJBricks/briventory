package models;

import jooq.tables.records.ContainerRecord;
import org.jooq.DSLContext;
import orm.LazyLoader;
import orm.Model;
import orm.RepositoriesHandler;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;
import repositories.ContainerTypesRepository;
import repositories.ContainersRepository;
import repositories.LockersRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static jooq.Tables.CONTAINER;

public abstract class Container extends Model implements ValidatableModel<ValidationError> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The identifier. */
  private Long id;
  /** The {@link LazyLoader} instance to retrieve the corresponding {@link ContainerType}. */
  private final LazyLoader<Long, ContainerType> containerTypeLazyLoader =
      new LazyLoader<>(idCT -> RepositoriesHandler.of(ContainerTypesRepository.class)
                                                  .findContainerTypeById(idCT));
  /** The {@link LazyLoader} instance to retrieve the associated {@link Locker} instances. */
  private final LazyLoader<Container, List<Locker>> lockersLazyLoader =
      new LazyLoader<>(container -> RepositoriesHandler.of(LockersRepository.class)
                                                       .getLockers(container));

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link Container}.
   *
   * @param id the identifier of this {@link Container}.
   * @param idContainerType the identifier of the corresponding {@link ContainerType}.
   * @param lockers the {@link List} of {@link Locker} instances. If set to {@code null}, the {@link List} has not been
   * fetched yet.
   */
  protected Container(final long id, final long idContainerType, final List<Locker> lockers) {
    this.id = id;
    containerTypeLazyLoader.setKey(idContainerType);
    lockersLazyLoader.setKey(this);
    if (lockers != null) {
      lockersLazyLoader.setValue(lockers);
    }
  }

  /**
   * Creates a new instance of {@link Container}.
   *
   * @param containerType the {@link ContainerType}.
   */
  protected Container(final ContainerType containerType) {
    containerTypeLazyLoader.setKey(containerType.getId());
    containerTypeLazyLoader.setValue(containerType);
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  public final ContainerRecord getUpdatableRecord1(final DSLContext dslContext) {
    final ContainerRecord containerRecord = dslContext.newRecord(CONTAINER);
    return containerRecord.setId(id)
                          .setIdContainerType(containerTypeLazyLoader.getKey());
  }

  // *******************************************************************************************************************
  // ValidatableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> errors(final DSLContext dslContext) {
    List<ValidationError> errors = new LinkedList<>();
    if (containerTypeLazyLoader.getKey() == null)
      errors.add(new ValidationError("idContainerType", "container.error.containerType.missing"));
    return errors;
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the identifier. */
  public Long getId() { return id; }

  /**
   * Sets the identifier.
   *
   * @param <C> the real subtype of {@link Container}.
   * @param id the identifier.
   *
   * @return this instance.
   */
  @SuppressWarnings("unchecked")
  public <C extends Container> C setId(final Long id) {
    this.id = id;
    return (C) this;
  }

  /** @return the identifier of the {@link ContainerType}. */
  public Long getIdContainerType() { return containerTypeLazyLoader.getKey(); }

  /** @return the {@link ContainerType} instance, or {@code null}. */
  public ContainerType getContainerType() { return containerTypeLazyLoader.getValue(); }

  /**
   * Sets the identifier of the {@link ContainerType}.
   *
   * @param <C> the real subtype of {@link Container}.
   * @param idContainerType the identifier of the {@link ContainerType}.
   *
   * @return this instance.
   */
  @SuppressWarnings("unchecked")
  public final <C extends Container> C setIdContainerType(final Long idContainerType) {
    containerTypeLazyLoader.setKey(idContainerType);
    return (C) this;
  }

  /**
   * Sets the {@link ContainerType}.
   *
   * @param <C> the real subtype of {@link Container}.
   * @param containerType the {@link ContainerType}.
   *
   * @return this instance.
   */
  @SuppressWarnings("unchecked")
  public final <C extends Container> C setContainerType(final ContainerType containerType) {
    containerTypeLazyLoader.setKey(containerType.getId());
    containerTypeLazyLoader.setValue(containerType);
    return (C) this;
  }

  @SuppressWarnings("unchecked")
  public final <C extends Container> C setLockers(final List<Locker> lockers) {
    lockersLazyLoader.setValue(lockers == null ? new LinkedList<>() : lockers);
    return (C) this;
  }

  public final List<Locker> getLockers() {
    final var lockers = lockersLazyLoader.getValue();
    if (lockers == null) return Collections.emptyList();
    return Collections.unmodifiableList(lockers);
  }

  public static List<Container> getAll() {
    return RepositoriesHandler.of(ContainersRepository.class).getContainers();
  }

}

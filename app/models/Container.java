package models;

import jooq.tables.records.ContainerRecord;
import org.jooq.DSLContext;
import orm.ManyModelsLoader;
import orm.Model;
import orm.ModelLoader;
import orm.RepositoriesHandler;
import orm.models.DeletableModel;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;
import repositories.ContainerTypesRepository;
import repositories.ContainersRepository;
import repositories.LockersRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static jooq.Tables.CONTAINER;

public abstract class Container extends Model implements ValidatableModel<ValidationError>,
    DeletableModel<ValidationError, ContainerRecord> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The identifier. */
  private Long id;
  /** The {@link ModelLoader} instance to retrieve the corresponding {@link ContainerType}. */
  private final ModelLoader<Long, ContainerType> containerTypeLoader =
      RepositoriesHandler.of(ContainerTypesRepository.class)
                         .createModelLoader();
  /** The {@link ManyModelsLoader} instance to retrieve the associated {@link Locker} instances. */
  private final ManyModelsLoader<Container, Locker> lockersLoader =
      RepositoriesHandler.of(LockersRepository.class)
                         .createLockersLoader(this);

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
    containerTypeLoader.setKey(idContainerType);
    lockersLoader.setKey(this);
    if (lockers != null) {
      lockersLoader.setValue(lockers);
    }
  }

  /**
   * Creates a new instance of {@link Container}.
   *
   * @param containerType the {@link ContainerType}.
   */
  protected Container(final ContainerType containerType) {
    containerTypeLoader.setKey(containerType.getId());
    containerTypeLoader.setValue(containerType);
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  public final ContainerRecord createRecord1(final DSLContext dslContext) {
    final ContainerRecord containerRecord = dslContext.newRecord(CONTAINER);
    containerRecord.setIdContainerType(containerTypeLoader.getKey());
    if (id != null) containerRecord.setId(id);
    return containerRecord;
  }

  public final void refresh1(final ContainerRecord containerRecord) {
    id = containerRecord.getId();
  }

  public final ContainerRecord createDeletionRecord(final DSLContext dslContext) {
    return createRecord1(dslContext);
  }

  // *******************************************************************************************************************
  // ValidatableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> validate(final DSLContext dslContext) {
    List<ValidationError> errors = new LinkedList<>();
    if (containerTypeLoader.getKey() == null)
      errors.add(new ValidationError("idContainerType", "container.error.containerType.missing"));
    return errors;
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the identifier. */
  public Long getId() { return id; }

  /** @return the identifier of the {@link ContainerType}. */
  public Long getIdContainerType() { return containerTypeLoader.getKey(); }

  /** @return the {@link ContainerType} instance, or {@code null}. */
  public ContainerType getContainerType() { return containerTypeLoader.getValue(); }

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
    containerTypeLoader.setKey(idContainerType);
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
    containerTypeLoader.setKey(containerType.getId());
    containerTypeLoader.setValue(containerType);
    return (C) this;
  }

  @SuppressWarnings("unchecked")
  public final <C extends Container> C setLockers(final List<Locker> lockers) {
    lockersLoader.setValue(lockers == null ? new LinkedList<>() : lockers);
    return (C) this;
  }

  public final List<Locker> getLockers() {
    final var lockers = lockersLoader.getValue();
    if (lockers == null) return Collections.emptyList();
    return Collections.unmodifiableList(lockers);
  }

  public static List<Container> getAll() {
    return RepositoriesHandler.of(ContainersRepository.class).getContainers();
  }

}

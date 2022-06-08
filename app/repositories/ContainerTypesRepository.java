package repositories;

import com.google.inject.Inject;
import database.BriventoryDB;
import jooq.tables.records.ContainerTypeRecord;
import models.ContainerType;
import org.jooq.DSLContext;
import orm.Mapper;
import orm.ModelLoader;
import orm.Repository;
import play.data.validation.ValidationError;

import javax.inject.Singleton;
import java.util.List;

import static jooq.Tables.CONTAINER_TYPE;

@Singleton
public final class ContainerTypesRepository extends Repository<ContainerType> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link ContainerType} from an instance of
   * {@link ContainerTypeRecord}.
   */
  private static final Mapper<ContainerTypeRecord, ContainerType> CONTAINER_TYPE_MAPPER =
      containerTypeRecord -> new ContainerType(containerTypeRecord.getId(),
                                               containerTypeRecord.getName(),
                                               containerTypeRecord.getMinLockers(),
                                               containerTypeRecord.getMaxLockers(),
                                               containerTypeRecord.getNumberFormatting());

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link ContainersRepository}.
   *
   * @param briventoryDB the {@link BriventoryDB}.
   */
  @Inject
  public ContainerTypesRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // Lazy Loaders Creation
  // *******************************************************************************************************************

  /** @return the {@link ModelLoader} to lazy load a {@link ContainerType} instance. */
  public ModelLoader<Long, ContainerType> createModelLoader() {
    return createModelLoader(this::findContainerTypeById);
  }

  // *******************************************************************************************************************
  // General Data Retrieval
  // *******************************************************************************************************************

  /** @return all {@link ContainerType} instances that are stored in the database. */
  public List<ContainerType> getContainerTypes() {
    return fetch(CONTAINER_TYPE_MAPPER, dslContext -> dslContext.selectFrom(CONTAINER_TYPE));
  }

  /**
   * find the {@link ContainerType} corresponding to the identifier given.
   *
   * @param dslContext the {@link DSLContext}.
   * @param id the identifier of the {@link ContainerType} to find.
   *
   * @return the instance of {@link ContainerType} corresponding to the identifier given, or {@code null} if the
   * identifier does not correspond to any container type.
   */
  private ContainerType findContainerTypeById(final DSLContext dslContext, final Long id) {
    if (id == null) return null;
    return fetchOne(CONTAINER_TYPE_MAPPER,
                    dslContext,
                    ctx -> ctx.selectFrom(CONTAINER_TYPE)
                              .where(CONTAINER_TYPE.ID.eq(id)));
  }

  // *******************************************************************************************************************
  // Validation, Persistence & Deletion
  // *******************************************************************************************************************

  /**
   * Persists a {@link ContainerType} into the database.
   *
   * @param containerType the {@link ContainerType} to persist.
   */
  public void persist(final ContainerType containerType) { super.persist(containerType); }

  /**
   * Deletes the provided {@link ContainerType} from the database.
   *
   * @param containerType the {@link ContainerType} to delete.
   */
  public void delete(final ContainerType containerType) { super.deleteInTransaction(containerType); }

  /**
   * Validates the provided {@link models.ContainerType}.
   *
   * @param containerType the {@link ContainerType} to validate.
   *
   * @return a {@link List} of {@link ValidationError} or an empty list if the model is valid.
   */
  public List<ValidationError> validate(final ContainerType containerType) {
    return super.validate(containerType);
  }

}

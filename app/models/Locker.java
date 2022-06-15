package models;

import jooq.tables.records.LockerRecord;
import org.jooq.DSLContext;
import orm.Model;
import orm.ModelLoader;
import orm.RepositoriesHandler;
import orm.models.DeletableModel;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;
import repositories.LockerSizesRepository;

import static jooq.Tables.LOCKER;

public final class Locker extends Model implements
    PersistableModel1<LockerRecord>,
    ValidatableModel<ValidationError>,
    DeletableModel<ValidationError, LockerRecord> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The identifier of this {@link Locker}. */
  private Long id;

  /** The {@link ModelLoader} that will handle the instance of {@link LockerSize}. */
  private final ModelLoader<Long, LockerSize> lockerSizeLoader =
      RepositoriesHandler.of(LockerSizesRepository.class)
                         .createModelLoader();

  /** The position of the locker in its container. */
  private Short position;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /** Creates an empty instance of {@link Locker}. */
  public Locker() { /* No-Op */ }

  /**
   * Creates an instance of {@link Locker}.
   *
   * @param id the identifier.
   * @param idLockerSize the identifier of the {@link LockerSize}.
   * @param position the position of the locker in its container.
   */
  public Locker(final long id, final long idLockerSize, final short position) {
    this.id = id;
    lockerSizeLoader.setKey(idLockerSize);
    this.position = position;
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  @Override
  public LockerRecord createRecord1(final DSLContext dslContext) {
    final LockerRecord lockerRecord = dslContext.newRecord(LOCKER);
    return lockerRecord.setId(id)
                       .setIdLockerSize(lockerSizeLoader.getKey());
  }

  public void refresh1(final LockerRecord lockerRecord) { id = lockerRecord.getId(); }

  // *******************************************************************************************************************
  // DeletableModel Overrides
  // *******************************************************************************************************************

  /**
   * @param dslContext the {@link DSLContext}.
   *
   * @return
   */
  @Override
  public LockerRecord createDeletionRecord(final DSLContext dslContext) {
    return createRecord1(dslContext);
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  public void setIdLockerSize(final Long idLockerSize) { lockerSizeLoader.setKey(idLockerSize); }

  public void setPosition(final Short position) { this.position = position; }

}

package models;

import jooq.tables.records.LockerSizeRecord;
import org.jooq.DSLContext;
import orm.Mapper;
import orm.Model;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;

import static jooq.Tables.LOCKER_SIZE;

/**
 * a locker size aims to factorize the sizes of the various lockers. Two (or more) lockers having the same size should
 * share the same size.
 */
public final class LockerSize extends Model
    implements PersistableModel1<LockerSizeRecord>, ValidatableModel<ValidationError> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link LockerSize} from an instance of
   * {@link LockerSizeRecord}.
   */
  public static final Mapper<LockerSizeRecord, LockerSize> LOCKER_SIZE_MAPPER =
      lockerSizeRecord -> new LockerSize(lockerSizeRecord.getId(),
                                         lockerSizeRecord.getName(),
                                         lockerSizeRecord.getWidth(),
                                         lockerSizeRecord.getLength(),
                                         lockerSizeRecord.getHeight());

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The identifier. */
  private Long id;
  /** The name. */
  private String name;
  /** The width. */
  private Double width;
  /** The length. */
  private Double length;
  /** The height. */
  private Double height;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link LockerSize}.
   *
   * @param id the identifier.
   * @param name the name.
   * @param width the width.
   * @param length the length.
   * @param height the height.
   */
  private LockerSize(final Long id, final String name, final Double width, final Double length, final Double height) {
    this.id = id;
    this.name = name;
    this.width = width;
    this.length = length;
    this.height = height;
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public LockerSizeRecord createRecord1(final DSLContext dslContext) {
    final LockerSizeRecord lockerSizeRecord = dslContext.newRecord(LOCKER_SIZE);
    return lockerSizeRecord.setId(id)
                           .setName(name)
                           .setWidth(width)
                           .setLength(length)
                           .setHeight(height);
  }

  /** {@inheritDoc} */
  public void refresh1(final LockerSizeRecord lockerSizeRecord) { id = lockerSizeRecord.getId(); }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the identifier. */
  public Long getId() { return id; }

  /** @return the name. */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the name.
   */
  public void setName(final String name) {
    this.name = name;
  }

  /** @return the width. */
  public Double getWidth() {
    return width;
  }

  /**
   * Sets the width.
   *
   * @param width the width.
   */
  public void setWidth(final Double width) {
    this.width = width;
  }

  /** @return the length. */
  public Double getLength() {
    return length;
  }

  /**
   * Sets the length.
   *
   * @param length the length.
   */
  public void setLength(final Double length) {
    this.length = length;
  }

  /** @return the height. */
  public Double getHeight() {
    return height;
  }

  /**
   * Sets the height.
   *
   * @param height the height.
   */
  public void setHeight(final Double height) {
    this.height = height;
  }

}

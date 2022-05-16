package models;

import jooq.tables.records.ContainerTypeRecord;
import org.jooq.DSLContext;
import orm.Model;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;

import java.util.IllegalFormatException;
import java.util.LinkedList;
import java.util.List;

import static jooq.Tables.CONTAINER_TYPE;

public final class ContainerType extends Model implements PersistableModel1<ContainerTypeRecord>,
                                                              ValidatableModel<ValidationError> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The identifier. */
  private Long id;
  /** The name. */
  private String name;
  /** The minimal amount of lockers that this type of container can hold. */
  private short minLockers;
  /** The maximal amount of lockers that this type of container can hold. */
  private short maxLockers;
  /** The format string that will be used to digitize the containers. */
  private String numberFormatting;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  public ContainerType(final long id, final String name, final short minLockers, final short maxLockers,
                       final String numberFormatting) {
    this(name, minLockers, maxLockers, numberFormatting);
    this.id = id;
  }

  ContainerType(final String name, final short minLockers, final short maxLockers, final String numberFormatting) {
    this.name = name;
    this.minLockers = minLockers;
    this.maxLockers = maxLockers;
    this.numberFormatting = numberFormatting;
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  @Override
  public ContainerTypeRecord getUpdatableRecord(final DSLContext dslContext) {
    final ContainerTypeRecord containerTypeRecord = dslContext.newRecord(CONTAINER_TYPE);
    return containerTypeRecord.setId(id)
                              .setName(name)
                              .setMinLockers(minLockers)
                              .setMaxLockers(maxLockers)
                              .setNumberFormatting(numberFormatting);
  }

  public void lastRefresh(final ContainerTypeRecord containerTypeRecord) { id = containerTypeRecord.getId(); }

  // *******************************************************************************************************************
  // ValidatableModel Overrides
  // *******************************************************************************************************************

  /** The name of the attribute {@link ContainerType#numberFormatting} in the {@link ValidationError} instances. */
  private static final String FIELD_NUMBER_FORMATTING = "numberFormatting";

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> errors(final DSLContext dslContext) {
    List<ValidationError> errors = new LinkedList<>();

    if (name == null || name.isBlank())
      errors.add(new ValidationError("name", "containerType.error.name.blank"));

    if (minLockers < 1)
      errors.add(new ValidationError("minLockers", "containerType.error.minLockers.invalid"));

    if (maxLockers < minLockers)
      errors.add(new ValidationError("maxLockers", "containerType.error.maxLockers.invalid"));

    if (numberFormatting == null || numberFormatting.isBlank())
      errors.add(new ValidationError(FIELD_NUMBER_FORMATTING, "containerType.error.numberFormatting.blank"));

    if (numberFormatting != null && !numberFormatting.isBlank() && !numberFormatting.contains("%"))
      errors.add(new ValidationError(FIELD_NUMBER_FORMATTING, "containerType.error.numberFormatting.invalid"));

    if (numberFormatting != null && !numberFormatting.isBlank()) {
      try {
        String.format(numberFormatting, 1);
      } catch (IllegalFormatException e) {
        errors.add(new ValidationError(FIELD_NUMBER_FORMATTING, "containerType.error.numberFormatting.invalid"));
      }
    }

    return errors;
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the identifier. */
  public Long getId() { return id; }

  void setId(final Long id) { this.id = id; }

  /** @return the name. */
  public String getName() { return name; }

  /**
   * Sets the name.
   *
   * @param name the name.
   *
   * @return this instance.
   */
  public ContainerType setName(final String name) {
    this.name = name;
    return this;
  }

  /** @return the minimal amount of lockers that this type of container can hold. */
  public short getMinLockers() { return minLockers; }

  /**
   * Sets the minimal amount of lockers that this type of container can hold.
   *
   * @param minLockers the minimal amount of lockers that this type of container can hold.
   *
   * @return this instance.
   */
  public ContainerType setMinLockers(final short minLockers) {
    this.minLockers = minLockers;
    return this;
  }

  /** @return the maximal amount of lockers that this type of container can hold. */
  public short getMaxLockers() { return maxLockers; }

  /**
   * Sets the maximal amount of lockers that this type of container can hold.
   *
   * @param maxLockers the maximal amount of lockers that this type of container can hold.
   *
   * @return this instance.
   */
  public ContainerType setMaxLockers(final short maxLockers) {
    this.maxLockers = maxLockers;
    return this;
  }

  /** @return the format string that will be used to digitize the containers. */
  public String getNumberFormatting() { return numberFormatting; }

  /**
   * Sets the format string that will be used to digitize the containers.
   *
   * @param numberFormatting the format string that will be used to digitize the containers.
   *
   * @return this instance.
   */
  public ContainerType setNumberFormatting(final String numberFormatting) {
    this.numberFormatting = numberFormatting;
    return this;
  }

  /**
   * Digitize the digit given.
   *
   * @param digit the digit to use.
   *
   * @return the label, according to {@link ContainerType#getNumberFormatting()}.
   */
  public String getLabel(final int digit) {
    return String.format(numberFormatting, digit);
  }

}

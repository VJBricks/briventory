package models;

import jooq.tables.records.ContainerRecord;
import jooq.tables.records.PrivateContainerRecord;
import org.jooq.DSLContext;
import orm.models.PersistableModel2;
import play.data.validation.ValidationError;

import java.util.Collections;
import java.util.List;

import static jooq.Tables.PRIVATE_CONTAINER;

public final class PrivateContainer
    extends Container
    implements PersistableModel2<ContainerRecord, PrivateContainerRecord> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The identifier of the {@link Account}, owning this container. */
  private Long idAccount;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  public PrivateContainer(final long id, final long idContainerType, final List<Locker> lockers, final long idAccount) {
    super(id, idContainerType, lockers);
    this.idAccount = idAccount;
  }

  public PrivateContainer(final ContainerType containerType, final Account account) {
    super(containerType);
    this.idAccount = account.getId();
  }

  public PrivateContainer(final SharedContainer sharedContainer, final Account account) {
    super(sharedContainer.getId(), sharedContainer.getContainerType());
    this.idAccount = account.getId();
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  @Override
  public PrivateContainerRecord createRecord2(final DSLContext dslContext) {
    final PrivateContainerRecord privateContainerRecord = dslContext.newRecord(PRIVATE_CONTAINER);
    return privateContainerRecord.setIdContainer(getId())
                                 .setIdAccount(idAccount);
  }

  @Override
  public void refresh2(final PrivateContainerRecord privateContainerRecord) { /* Nothing to do */ }

  // *******************************************************************************************************************
  // ValidatableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> validate(final DSLContext dslContext) {
    return Collections.emptyList();
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /**
   * Sets the identifier of the {@link Account}, owning this container.
   *
   * @param idAccount the identifier of the {@link Account}, owning this container.
   *
   * @return this instance.
   */
  public PrivateContainer setIdAccount(final Long idAccount) {
    this.idAccount = idAccount;
    return this;
  }

}

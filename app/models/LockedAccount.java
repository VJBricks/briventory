package models;

import jooq.tables.records.LockedAccountRecord;
import org.jooq.DSLContext;
import orm.Mapper;
import orm.Model;
import orm.models.DeletableModel;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;

import static jooq.Tables.LOCKED_ACCOUNT;

/**
 * {@code LockedAccount} represents the week entity, identifying that the corresponding {@link models.Account} is
 * locked.
 */
public final class LockedAccount extends Model implements PersistableModel1<LockedAccount, LockedAccountRecord>,
    ValidatableModel<ValidationError>,
    DeletableModel<LockedAccount, ValidationError, LockedAccountRecord> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link Administrator} from an instance of
   * {@link LockedAccountRecord}.
   */
  public static final Mapper<LockedAccountRecord, LockedAccount> LOCKED_ACCOUNT_MAPPER =
      lockedAccountRecord -> new LockedAccount(lockedAccountRecord.getIdAccount());

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The identifier of the {@link models.Account}. */
  private final long idAccount;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link LockedAccount} instance.
   *
   * @param idAccount the identifier of the {@link models.Account}.
   */
  LockedAccount(final long idAccount) {
    this.idAccount = idAccount;
  }

  // *******************************************************************************************************************
  // PersistableModel1, ValidatableModel & DeletableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public LockedAccountRecord createDeletionRecord(final DSLContext dslContext) {
    return createRecord1(dslContext);
  }

  /** {@inheritDoc} */
  @Override
  public LockedAccountRecord createRecord1(final DSLContext dslContext) {
    final var lockedAccountRecord = dslContext.newRecord(LOCKED_ACCOUNT);
    lockedAccountRecord.setIdAccount(idAccount);
    return lockedAccountRecord;
  }

  /** {@inheritDoc} */
  @Override
  public void refresh1(final LockedAccountRecord lockedAccountRecord) { /* Nothing to do */ }

}

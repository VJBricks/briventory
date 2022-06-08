package models;

import jooq.tables.records.AdministratorRecord;
import org.jooq.DSLContext;
import orm.Mapper;
import orm.Model;
import orm.models.DeletableModel;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;

import static jooq.Tables.ADMINISTRATOR;

/**
 * {@code Administrator} represents the week entity, identifying that the corresponding {@link models.Account} is an
 * administrator.
 */
public class Administrator extends Model implements PersistableModel1<AdministratorRecord>,
                                                        ValidatableModel<ValidationError>,
                                                        DeletableModel<ValidationError, AdministratorRecord> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link Administrator} from an instance of
   * {@link AdministratorRecord}.
   */
  public static final Mapper<AdministratorRecord, Administrator> ADMINISTRATOR_MAPPER =
      administratorRecord -> new Administrator(administratorRecord.getIdAccount());

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The identifier of the {@link models.Account}. */
  private final long idAccount;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link Administrator} instance.
   *
   * @param idAccount the identifier of the {@link models.Account}.
   */
  Administrator(final long idAccount) {
    this.idAccount = idAccount;
  }

  // *******************************************************************************************************************
  // PersistableModel1, ValidatableModel & DeletableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public AdministratorRecord createDeletionRecord(final DSLContext dslContext) {
    return createRecord1(dslContext);
  }

  /** {@inheritDoc} */
  @Override
  public AdministratorRecord createRecord1(final DSLContext dslContext) {
    final var administratorRecord = dslContext.newRecord(ADMINISTRATOR);
    administratorRecord.setIdAccount(idAccount);
    return administratorRecord;
  }

  /** {@inheritDoc} */
  @Override
  public void refresh1(final AdministratorRecord administratorRecord) { /* Nothing to do */ }

}

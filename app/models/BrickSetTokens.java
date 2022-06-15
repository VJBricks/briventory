package models;

import jooq.tables.records.BricksetTokensRecord;
import org.jooq.DSLContext;
import orm.Mapper;
import orm.Model;
import orm.models.DeletableModel;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;

import java.util.LinkedList;
import java.util.List;

import static jooq.Tables.BRICKSET_TOKENS;

/**
 * The {@code BricksetTokens} class is the representation of the table {@code brickset_tokens} in the
 * <em>Briventory</em> database.
 */
public final class BrickSetTokens extends Model implements PersistableModel1<BricksetTokensRecord>,
    ValidatableModel<ValidationError>,
    DeletableModel<ValidationError, BricksetTokensRecord> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link BrickSetTokens} from an instance of
   * {@link BricksetTokensRecord}.
   */
  public static final Mapper<BricksetTokensRecord, BrickSetTokens> BRICKSET_TOKENS_MAPPER =
      bricksetTokensRecord -> new BrickSetTokens(bricksetTokensRecord.getIdAccount(),
                                                 bricksetTokensRecord.getApiKey(),
                                                 bricksetTokensRecord.getUsername(),
                                                 bricksetTokensRecord.getPassword());

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************

  /** The foreign key identifier to the {@link Account}. */
  private long idAccount;
  /** The API key. */
  private String apiKey;
  /** The username. */
  private String username;
  /** The password. */
  private String password;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link BrickSetTokens}.
   *
   * @param idAccount the identifier of the {@link Account}.
   * @param apiKey the API key.
   * @param username the username.
   * @param password the password.
   */
  private BrickSetTokens(final long idAccount, final String apiKey, final String username, final String password) {
    this.idAccount = idAccount;
    this.apiKey = apiKey;
    this.username = username;
    this.password = password;
  }

  /**
   * Creates a new instance of {@link BrickSetTokens}.
   *
   * @param account the {@link Account}.
   * @param apiKey the API key.
   * @param username the username.
   * @param password the password.
   */
  public BrickSetTokens(final Account account, final String apiKey, final String username, final String password) {
    this(account.getId(), apiKey, username, password);
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  @Override
  public BricksetTokensRecord createRecord1(final DSLContext dslContext) {
    final BricksetTokensRecord bricksetTokensRecord = dslContext.newRecord(BRICKSET_TOKENS);
    return bricksetTokensRecord.setIdAccount(idAccount)
                               .setApiKey(apiKey)
                               .setUsername(username)
                               .setPassword(password);
  }

  @Override
  public void refresh1(final BricksetTokensRecord bricksetTokensRecord) { /* Nothing to do */ }

  // *******************************************************************************************************************
  // ValidatableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> validate(final DSLContext dslContext) {
    List<ValidationError> errors = new LinkedList<>();
    if (apiKey == null || apiKey.isBlank())
      errors.add(new ValidationError("apiKey", "bricksetTokens.error.apiKey.empty"));
    if (username == null || username.isBlank())
      errors.add(new ValidationError("username", "bricksetTokens.error.username.empty"));
    if (password == null || password.isBlank())
      errors.add(new ValidationError("password", "bricksetTokens.error.password.empty"));
    return errors;
  }

  // *******************************************************************************************************************
  // DeletableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public BricksetTokensRecord createDeletionRecord(final DSLContext dslContext) {
    return createRecord1(dslContext);
  }

  // *******************************************************************************************************************
  // Getters and Setters
  // *******************************************************************************************************************

  /**
   * Sets the identifier of the {@link Account}.
   *
   * @param idAccount the identifier of the {@link Account}.
   *
   * @return the current instance.
   */
  public BrickSetTokens setIdAccount(final long idAccount) {
    this.idAccount = idAccount;
    return this;
  }

  /** @return the API key. */
  public String getApiKey() { return apiKey; }

  /**
   * Sets the API key.
   *
   * @param apiKey the API key.
   *
   * @return the current instance.
   */
  public BrickSetTokens setApiKey(final String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  /** @return the username. */
  public String getUsername() { return username; }

  /**
   * Sets the username.
   *
   * @param username the username.
   *
   * @return the current instance.
   */
  public BrickSetTokens setUsername(final String username) {
    this.username = username;
    return this;
  }

  /** @return the password. */
  public String getPassword() { return password; }

  /**
   * Sets the password.
   *
   * @param password the password.
   *
   * @return the current instance.
   */
  public BrickSetTokens setPassword(final String password) {
    this.password = password;
    return this;
  }

}

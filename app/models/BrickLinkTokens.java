package models;

import jooq.tables.records.BricklinkTokensRecord;
import org.jooq.DSLContext;
import orm.Mapper;
import orm.Model;
import orm.models.DeletableModel;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static jooq.Tables.BRICKLINK_TOKENS;

/**
 * The {@code BrickLinkTokens} class is the representation of the table {@code bricklink_tokens} in the
 * <em>Briventory</em> database.
 * <p>
 * The {@code BrickLinkTokens} class handles the various information to connect to the BrickLink API. The values can be
 * obtained from <a href="https://www.bricklink.com/v2/api/register_consumer.page">BrickLink</a>.
 * </p>
 */
public final class BrickLinkTokens extends Model implements PersistableModel1<BrickLinkTokens, BricklinkTokensRecord>,
    ValidatableModel<ValidationError>, DeletableModel<BrickLinkTokens, ValidationError, BricklinkTokensRecord> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link BrickLinkTokens} from an instance of
   * {@link BricklinkTokensRecord}.
   */
  public static final Mapper<BricklinkTokensRecord, BrickLinkTokens> BRICKLINK_TOKENS_MAPPER =
      bricklinkTokensRecord -> new BrickLinkTokens(bricklinkTokensRecord.getIdAccount(),
                                                   bricklinkTokensRecord.getConsumerKey(),
                                                   bricklinkTokensRecord.getConsumerSecret(),
                                                   bricklinkTokensRecord.getTokenValue(),
                                                   bricklinkTokensRecord.getTokenSecret(),
                                                   bricklinkTokensRecord.getValidUntil());

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************

  /** The foreign key identifier to the {@link Account}. */
  private long idAccount;
  /** The consumer key. */
  private String consumerKey;
  /** The consumer token. */
  private String consumerSecret;
  /** The token value. */
  private String tokenValue;
  /** The token secret. */
  private String tokenSecret;
  /** The validity of the tokens. */
  private LocalDate validUntil;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link BrickLinkTokens}.
   *
   * @param idAccount the identifier of the {@link Account}.
   * @param consumerKey the consumer key.
   * @param consumerSecret the consumer secret.
   * @param tokenValue the token value.
   * @param tokenSecret the token secret.
   * @param validUntil the validity of the tokens.
   */
  private BrickLinkTokens(final long idAccount, final String consumerKey, final String consumerSecret,
                          final String tokenValue, final String tokenSecret, final LocalDate validUntil) {
    this.idAccount = idAccount;
    this.consumerKey = consumerKey;
    this.consumerSecret = consumerSecret;
    this.tokenValue = tokenValue;
    this.tokenSecret = tokenSecret;
    this.validUntil = validUntil;
  }

  /**
   * Creates a new instance of {@link BrickLinkTokens}.
   *
   * @param account the {@link Account}.
   * @param consumerKey the consumer key.
   * @param consumerSecret the consumer secret.
   * @param tokenValue the token value.
   * @param tokenSecret the token secret.
   */
  public BrickLinkTokens(final Account account, final String consumerKey, final String consumerSecret,
                         final String tokenValue, final String tokenSecret) {
    this(account.getId(), consumerKey, consumerSecret, tokenValue, tokenSecret, LocalDate.now());
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  @Override
  public BricklinkTokensRecord createRecord1(final DSLContext dslContext) {
    final BricklinkTokensRecord bricklinkTokensRecord = dslContext.newRecord(BRICKLINK_TOKENS);
    return bricklinkTokensRecord.setIdAccount(idAccount)
                                .setConsumerKey(consumerKey)
                                .setConsumerSecret(consumerSecret)
                                .setTokenValue(tokenValue)
                                .setTokenSecret(tokenSecret)
                                .setValidUntil(validUntil);
  }

  @Override
  public void refresh1(final BricklinkTokensRecord bricklinkTokensRecord) { /* Nothing to do */ }

  // *******************************************************************************************************************
  // ValidatableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> validate(final DSLContext dslContext) {
    List<ValidationError> errors = new LinkedList<>();
    if (consumerKey == null || consumerKey.isBlank())
      errors.add(new ValidationError("consumerKey", "bricklinkTokens.error.consumerKey.empty"));
    if (consumerSecret == null || consumerSecret.isBlank())
      errors.add(new ValidationError("consumerSecret", "bricklinkTokens.error.consumerSecret.empty"));
    if (tokenValue == null || tokenValue.isBlank())
      errors.add(new ValidationError("tokenValue", "bricklinkTokens.error.tokenValue.empty"));
    if (tokenSecret == null || tokenSecret.isBlank())
      errors.add(new ValidationError("tokenSecret", "bricklinkTokens.error.tokenSecret.empty"));
    return errors;
  }

  // *******************************************************************************************************************
  // DeletableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public BricklinkTokensRecord createDeletionRecord(final DSLContext dslContext) {
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
  public BrickLinkTokens setIdAccount(final long idAccount) {
    this.idAccount = idAccount;
    return this;
  }

  /** @return the consumer key. */
  public String getConsumerKey() { return consumerKey; }

  /**
   * Sets the consumer key.
   *
   * @param consumerKey the consumer key.
   *
   * @return the current instance.
   */
  public BrickLinkTokens setConsumerKey(final String consumerKey) {
    this.consumerKey = consumerKey;
    return this;
  }

  /** @return the consumer secret. */
  public String getConsumerSecret() { return consumerSecret; }

  /**
   * Sets the consumer secret.
   *
   * @param consumerSecret the consumer secret.
   *
   * @return the current instance.
   */
  public BrickLinkTokens setConsumerSecret(final String consumerSecret) {
    this.consumerSecret = consumerSecret;
    return this;
  }

  /** @return the access token value. */
  public String getTokenValue() { return tokenValue; }

  /**
   * Sets the access token value.
   *
   * @param tokenValue the access token value.
   *
   * @return the current instance.
   */
  public BrickLinkTokens setTokenValue(final String tokenValue) {
    this.tokenValue = tokenValue;
    return this;
  }

  /** @return the access token secret. */
  public String getTokenSecret() { return tokenSecret; }

  /**
   * Sets the access token secret.
   *
   * @param tokenSecret the access token secret.
   *
   * @return the current instance.
   */
  public BrickLinkTokens setTokenSecret(final String tokenSecret) {
    this.tokenSecret = tokenSecret;
    return this;
  }

  /** @return the validity of the tokens. */
  public LocalDate getValidUntil() { return validUntil; }

  /**
   * Sets the date until witch the tokens are valid.
   *
   * @param validUntil the date until witch the tokens are valid.
   *
   * @return the current instance.
   */
  public BrickLinkTokens setValidUntil(final LocalDate validUntil) {
    this.validUntil = validUntil;
    return this;
  }

}

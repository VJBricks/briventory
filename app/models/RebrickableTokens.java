package models;

import jooq.tables.records.RebrickableTokensRecord;
import org.jooq.DSLContext;
import orm.Model;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static jooq.Tables.REBRICKABLE_TOKENS;

public final class RebrickableTokens extends Model implements PersistableModel1<RebrickableTokensRecord>,
                                                                  ValidatableModel<ValidationError> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The foreign key identifier to the {@link Account}. */
  private long idAccount;
  /** The key. */
  private String key;
  /** The validity of the tokens. */
  private LocalDate validUntil;

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  @Override
  public RebrickableTokensRecord getUpdatableRecord(final DSLContext dslContext) {
    final RebrickableTokensRecord rebrickableTokensRecord = dslContext.newRecord(REBRICKABLE_TOKENS);
    return rebrickableTokensRecord.setIdAccount(idAccount)
                                  .setKey(key)
                                  .setValidUntil(validUntil);
  }

  @Override
  public void lastRefresh(final RebrickableTokensRecord rebrickableTokensRecord) { /* Nothing to do */ }

  // *******************************************************************************************************************
  // ValidatableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> errors(final DSLContext dslContext) {
    List<ValidationError> errors = new LinkedList<>();
    if (key == null || key.isBlank())
      errors.add(new ValidationError("key", "rebrickableTokens.error.key.empty"));
    return errors;
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /**
   * Sets the identifier of the {@link Account}.
   *
   * @param idAccount the identifier of the {@link Account}.
   *
   * @return the current instance.
   */
  public RebrickableTokens setIdAccount(final long idAccount) {
    this.idAccount = idAccount;
    return this;
  }

  /** @return the authentication key. */
  public String getKey() { return key; }

  /**
   * Sets the authentication key.
   *
   * @param key the authentication key.
   *
   * @return the current instance.
   */
  public RebrickableTokens setKey(final String key) {
    this.key = key;
    return this;
  }

  /** @return the validity of the tokens. */
  public LocalDate getValidUntil() { return validUntil; }

  /**
   * Sets the validity of these tokens.
   *
   * @param validUntil the validity.
   *
   * @return the current instance.
   */
  public RebrickableTokens setValidUntil(final LocalDate validUntil) {
    this.validUntil = validUntil;
    return this;
  }

}

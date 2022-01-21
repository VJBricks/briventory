package models;

import orm.models.Entity;
import orm.models.IStorableEntity;
import play.data.validation.ValidationError;
import repositories.AccountsRepository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public final class RebrickableTokens extends Entity<AccountsRepository> implements IStorableEntity<ValidationError> {

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
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link models.RebrickableTokens}.
   *
   * @param accountsRepository the {@link AccountsRepository}.
   */
  public RebrickableTokens(final AccountsRepository accountsRepository) {
    super(accountsRepository);
  }

  // *******************************************************************************************************************
  // Entity Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> isValid() {
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

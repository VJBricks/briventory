package controllers.accounts;

import controllers.forms.ValidatableWithAccountsRepository;
import controllers.forms.ValidateWithAccountsRepository;
import database.Constraints;
import models.Account;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import repositories.AccountsRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/** This {@link play.data.Form} handler is specific to the update of the e-mail address. */
@ValidateWithAccountsRepository
public final class EmailForm implements ValidatableWithAccountsRepository<List<ValidationError>> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************

  /** The identifier of the account. */
  @Required
  private long idAccount;

  /** The e-mail address. */
  @Required
  @MaxLength(Constraints.EMAIL_DOMAIN_LENGTH)
  private String email;

  // *******************************************************************************************************************
  // Construction & Initialisation
  // *******************************************************************************************************************

  /** Empty constructor for the binding with the request. */
  public EmailForm() { /* No-op. */ }

  /**
   * Initializes this form.
   *
   * @param idAccount the identifier of the account.
   * @param email the e-mail address.
   */
  public EmailForm(final long idAccount, final String email) {
    this.idAccount = idAccount;
    this.email = email;
  }

  // *******************************************************************************************************************
  // ValidatableWithAccountsRepository Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> validate(final AccountsRepository accountsRepository) {
    LinkedList<ValidationError> l = new LinkedList<>();

    final Optional<Account> optionalAccount = accountsRepository.findById(idAccount);
    if (optionalAccount.isEmpty()) {
      l.add(new ValidationError(null, "account.settings.error.user.invalid"));
    } else {
      final Account account = optionalAccount.get();
      if (accountsRepository.emailAlreadyExists(account, email))
        l.add(new ValidationError("email", "auth.signup.error.email.exist"));
      account.setEmail(email);
      l.addAll(accountsRepository.validate(account));
    }

    if (email.isBlank())
      l.add(new ValidationError("email", "error.required"));
    return l;
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the identifier of the account. */
  public long getIdAccount() { return idAccount; }

  /**
   * Sets the identifier of the account.
   *
   * @param idAccount the identifier of the account.
   */
  public void setIdAccount(final long idAccount) { this.idAccount = idAccount; }

  /** @return the e-mail address. */
  public String getEmail() { return email; }

  /**
   * Sets the e-mail address.
   *
   * @param email the e-mail address.
   */
  public void setEmail(final String email) {
    this.email = email;
  }

}


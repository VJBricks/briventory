package controllers.accounts;

import controllers.forms.ValidatableWithAccountsRepository;
import controllers.forms.ValidateWithAccountsRepository;
import database.Constraints;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import repositories.AccountsRepository;

import java.util.LinkedList;
import java.util.List;

@ValidateWithAccountsRepository
public final class EmailForm implements ValidatableWithAccountsRepository<List<ValidationError>> {

  /** The e-mail address. */
  @Required
  @MaxLength(Constraints.EMAIL_DOMAIN_LENGTH)
  private String email;

  /** Empty constructor for the binding with the request. */
  public EmailForm() { /* No-op. */ }

  /**
   * Initializes this form.
   *
   * @param email the e-mail address.
   */
  public EmailForm(final String email) {
    this.email = email;
  }

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

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> validate(final AccountsRepository accountsRepository) {
    LinkedList<ValidationError> l = new LinkedList<>();
    if (email.isBlank())
      l.add(new ValidationError("email", "error.required"));
    if (accountsRepository.emailAlreadyExists(email))
      l.add(new ValidationError("email", "auth.signup.error.email.exist"));
    return l;
  }

}

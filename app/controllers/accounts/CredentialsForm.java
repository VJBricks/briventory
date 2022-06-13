package controllers.accounts;

import at.favre.lib.crypto.bcrypt.BCrypt;
import controllers.forms.ValidatableWithAccountsRepository;
import controllers.forms.ValidateWithAccountsRepository;
import database.Constraints;
import models.Account;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import repositories.AccountsRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static controllers.accounts.AccountsController.nbvcxz;

/** This {@link play.data.Form} handler is specific to the update of the password. */
@ValidateWithAccountsRepository
public final class CredentialsForm implements ValidatableWithAccountsRepository<List<ValidationError>> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The identifier of the account. */
  @Required
  private long idAccount;

  /** The current password. */
  @Required
  @MaxLength(Constraints.PASSWORD_DOMAIN_LENGTH)
  private String currentPassword;

  /** The new password. */
  @Required
  @MaxLength(Constraints.PASSWORD_DOMAIN_LENGTH)
  private String newPassword;

  /** The new password, repeated. */
  @Required
  @MaxLength(Constraints.PASSWORD_DOMAIN_LENGTH)
  private String newPasswordRepeated;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /** Empty constructor for the binding with the request. */
  public CredentialsForm() { /* No-op. */ }

  /**
   * Initializes this form.
   *
   * @param idAccount the identifier of the {@link Account} concerned.
   */
  public CredentialsForm(final long idAccount) {
    this.idAccount = idAccount;
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
      final boolean passVerified = BCrypt.verifyer().verify(currentPassword.getBytes(),
                                                            account.getPassword().getBytes()).verified;
      if (!passVerified)
        l.add(new ValidationError("currentPassword", "auth.signup.error.currentPassword.mismatch"));

      final var nbvcxz = nbvcxz(account.getFirstname(), account.getLastname(), account.getEmail());
      if (nbvcxz.estimate(newPassword).getBasicScore() < Constraints.PASSWORD_MIN_STRENGTH)
        l.add(new ValidationError("newPassword", "auth.signup.error.password.weak",
                                  Collections.singletonList(Constraints.PASSWORD_MIN_STRENGTH)));
      if (!newPassword.equals(newPasswordRepeated))
        l.add(new ValidationError("newPasswordRepeated", "auth.signup.error.passwordRepetition.mismatch"));

      l.addAll(accountsRepository.validate(account));
    }

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

  /** @return the current password. */
  public String getCurrentPassword() { return currentPassword; }

  /**
   * Sets the current password.
   *
   * @param currentPassword the current password.
   */
  public void setCurrentPassword(final String currentPassword) { this.currentPassword = currentPassword; }

  /** @return the new password. */
  public String getNewPassword() { return newPassword; }

  /**
   * Sets the new password.
   *
   * @param newPassword the new password.
   */
  public void setNewPassword(final String newPassword) { this.newPassword = newPassword; }

  /** @return the new password, repeated. */
  public String getNewPasswordRepeated() { return newPasswordRepeated; }

  /**
   * Sets the new password, repeated.
   *
   * @param newPasswordRepeated the new password, repeated.
   */
  public void setNewPasswordRepeated(
      final String newPasswordRepeated) { this.newPasswordRepeated = newPasswordRepeated; }

}

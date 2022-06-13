package controllers.auth;

import controllers.forms.ValidatableWithAccountsRepository;
import controllers.forms.ValidateWithAccountsRepository;
import database.Constraints;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import repositories.AccountsRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static controllers.accounts.AccountsController.nbvcxz;

@ValidateWithAccountsRepository
public final class AdminSignUpForm implements ValidatableWithAccountsRepository<List<ValidationError>> {

  /** The message key to display the error that the field is required. */
  private static final String ERROR_REQUIRED = "error.required";

  /** The first name. */
  @Required
  @MaxLength(Constraints.NAME_DOMAIN_LENGTH)
  private String firstname;

  /** The last name. */
  @Required
  @MaxLength(Constraints.NAME_DOMAIN_LENGTH)
  private String lastname;

  /** The e-mail address. */
  @Required
  @MaxLength(Constraints.EMAIL_DOMAIN_LENGTH)
  @Email
  private String email;

  /** The password. */
  @Required
  @MinLength(Constraints.PASSWORD_MIN_LENGTH)
  private String password;

  /** The password repetition. */
  @Required
  private String passwordRepetition;

  /** @return the first name. */
  public String getFirstname() { return firstname; }

  /** @return the last name. */
  public String getLastname() { return lastname; }

  /**
   * Sets the first name.
   *
   * @param firstname the first name.
   */
  public void setFirstname(final String firstname) {
    this.firstname = firstname;
  }

  /**
   * Sets the last name.
   *
   * @param lastname the last name.
   */
  public void setLastname(final String lastname) {
    this.lastname = lastname;
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

  /** @return the password. */
  public String getPassword() { return password; }

  /**
   * Sets the password.
   *
   * @param password the password.
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /** @return the password repetition. */
  public String getPasswordRepetition() { return passwordRepetition; }

  /**
   * Sets the password repetition.
   *
   * @param passwordRepetition the password repetition.
   */
  public void setPasswordRepetition(final String passwordRepetition) {
    this.passwordRepetition = passwordRepetition;
  }

  @Override
  public List<ValidationError> validate(final AccountsRepository accountsRepository) {
    LinkedList<ValidationError> l = new LinkedList<>();

    if (firstname.isBlank())
      l.add(new ValidationError("firstname", ERROR_REQUIRED));

    if (lastname.isBlank())
      l.add(new ValidationError("lastname", ERROR_REQUIRED));

    if (email.isBlank())
      l.add(new ValidationError("email", ERROR_REQUIRED));

    if (accountsRepository.emailAlreadyExists(email))
      l.add(new ValidationError("email", "auth.signup.error.email.exist"));

    final var nbvcxz = nbvcxz(firstname, lastname, email);
    if (nbvcxz.estimate(password).getBasicScore() < Constraints.PASSWORD_MIN_STRENGTH)
      l.add(new ValidationError("password", "auth.signup.error.password.weak",
                                Collections.singletonList(Constraints.PASSWORD_MIN_STRENGTH)));

    if (!password.equals(passwordRepetition))
      l.add(new ValidationError("passwordRepetition", "auth.signup.error.passwordRepetition.mismatch"));
    return l;
  }

}

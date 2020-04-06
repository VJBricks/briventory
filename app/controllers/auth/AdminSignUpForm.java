package controllers.auth;

import com.nulabinc.zxcvbn.Zxcvbn;
import database.Constraints;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.Validatable;
import play.data.validation.Constraints.Validate;
import play.data.validation.ValidationError;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Validate
public final class AdminSignUpForm implements Validatable<List<ValidationError>> {

  /** The name. */
  @Required
  @MaxLength(Constraints.NAME_DOMAIN_LENGTH)
  private String name;

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

  /** @return the name. */
  public String getName() { return name; }

  /**
   * Sets the name.
   *
   * @param name the name.
   */
  public void setName(final String name) {
    this.name = name;
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

  public String getPassword() { return password; }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getPasswordRepetition() { return passwordRepetition; }

  public void setPasswordRepetition(final String passwordRepetition) {
    this.passwordRepetition = passwordRepetition;
  }

  @Override
  public List<ValidationError> validate() {
    LinkedList<ValidationError> l = new LinkedList<>();
    if (name.trim().isEmpty())
      l.add(new ValidationError("name", "error.required"));
    if (email.trim().isEmpty())
      l.add(new ValidationError("email", "error.required"));
    final Zxcvbn zxcvbn = new Zxcvbn();
    if (zxcvbn.measure(password).getScore() < Constraints.PASSWORD_MIN_STRENGTH)
      l.add(new ValidationError("password", "auth.signup.password.weak",
                                Collections.singletonList(Constraints.PASSWORD_MIN_STRENGTH)));
    if (!password.equals(passwordRepetition))
      l.add(new ValidationError("passwordRepetition", "auth.signup.passwordRepetition.mismatch"));
    return l;
  }

}

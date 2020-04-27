package controllers.auth;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.Validatable;
import play.data.validation.ValidationError;

import java.util.LinkedList;
import java.util.List;

/** This class validates the <em>Sign In</em> form. */
public final class SignInForm implements Validatable<List<ValidationError>> {

  /** The e-mail address. */
  @Required
  @Email
  private String email;

  /** The password. */
  @Required
  private String password;

  /** @return a {@link List} of {@link ValidationError} instances or an empty list if there is no error. */
  @Override
  public List<ValidationError> validate() {
    LinkedList<ValidationError> l = new LinkedList<>();

    if (email.trim().isEmpty())
      l.add(new ValidationError("email", "error.required"));

    if (password.isBlank())
      l.add(new ValidationError("password", "error.required"));

    return l;
  }

  /** @return the e-mail address. */
  public String getEmail() { return email; }

  /**
   * Sets the e-mail address.
   *
   * @param email the e-mail address.
   */
  public void setEmail(final String email) { this.email = email; }

  /** @return the password. */
  public String getPassword() { return password; }

  /**
   * Sets the password.
   *
   * @param password the password.
   */
  public void setPassword(final String password) { this.password = password; }

}

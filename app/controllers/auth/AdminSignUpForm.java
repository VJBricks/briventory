package controllers.auth;

import database.DomainConstraints;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.Validatable;
import play.data.validation.ValidationError;

import java.util.LinkedList;
import java.util.List;

public final class AdminSignUpForm implements Validatable<List<ValidationError>> {

  /** The name. */
  @Required
  @MaxLength(DomainConstraints.NAME_LENGTH)
  private String name;

  /** The e-mail address. */
  @Required
  @MaxLength(DomainConstraints.EMAIL_LENGTH)
  @Email
  private String email;

  /** The password. */
  @Required
  private String password;

  /** The password repetition. */
  @Required
  private String passwordRepetition;

  @Override
  public List<ValidationError> validate() {
    LinkedList<ValidationError> l = new LinkedList<>();
    if (name.isEmpty())
      l.add(new ValidationError("name", "adminSignUp.name.empty"));
    if (email.isEmpty())
      l.add(new ValidationError("email", "adminSignUp.email.empty"));
    if (password.isEmpty())
      l.add(new ValidationError("password", "adminSignUp.password.empty"));
    if (!password.equals(passwordRepetition))
      l.add(new ValidationError("passwordRepetition", "adminSignUp.passwordRepetition.mismatch"));
    return l;
  }

}

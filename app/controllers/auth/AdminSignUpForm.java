package controllers.auth;

import controllers.forms.ValidatableWithAccountsRepository;
import controllers.forms.ValidateWithAccountsRepository;
import database.Constraints;
import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.resources.ConfigurationBuilder;
import me.gosimple.nbvcxz.resources.Dictionary;
import me.gosimple.nbvcxz.resources.DictionaryBuilder;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import repositories.AccountsRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@ValidateWithAccountsRepository
public final class AdminSignUpForm implements ValidatableWithAccountsRepository<List<ValidationError>> {

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

  private Nbvcxz nbvcxz() {
    // From https://github.com/GoSimpleLLC/nbvcxz#requires-java
    // Create a map of excluded words on a per-user basis using a hypothetical "User" object that contains this info
    List<Dictionary> dictionaryList = ConfigurationBuilder.getDefaultDictionaries();
    dictionaryList.add(new DictionaryBuilder()
                           .setDictionaryName("exclude")
                           .setExclusion(true)
                           .addWord(firstname, 0)
                           .addWord(lastname, 0)
                           .addWord(email, 0)
                           .createDictionary());

    // Create our configuration object and set our custom minimum entropy, and custom dictionary list
    final var minimumEntry = 40d;
    var configuration = new ConfigurationBuilder()
        .setMinimumEntropy(minimumEntry)
        .setDictionaries(dictionaryList)
        .createConfiguration();

    return new Nbvcxz(configuration);
  }

  @Override
  public List<ValidationError> validate(final AccountsRepository accountsRepository) {
    LinkedList<ValidationError> l = new LinkedList<>();
    if (firstname.isBlank())
      l.add(new ValidationError("firstname", "error.required"));
    if (lastname.isBlank())
      l.add(new ValidationError("lastname", "error.required"));
    if (email.isBlank())
      l.add(new ValidationError("email", "error.required"));
    if (accountsRepository.emailAlreadyExists(email))
      l.add(new ValidationError("email", "auth.signup.error.email.exist"));
    final var nbvcxz = nbvcxz();
    if (nbvcxz.estimate(password).getBasicScore() < Constraints.PASSWORD_MIN_STRENGTH)
      l.add(new ValidationError("password", "auth.signup.error.password.weak",
                                Collections.singletonList(Constraints.PASSWORD_MIN_STRENGTH)));
    if (!password.equals(passwordRepetition))
      l.add(new ValidationError("passwordRepetition", "auth.signup.error.passwordRepetition.mismatch"));
    return l;
  }

}

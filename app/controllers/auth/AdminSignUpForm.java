package controllers.auth;

import controllers.forms.ValidatableWithDB;
import controllers.forms.ValidateWithDB;
import database.BriventoryDB;
import database.Constraints;
import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.resources.Configuration;
import me.gosimple.nbvcxz.resources.ConfigurationBuilder;
import me.gosimple.nbvcxz.resources.Dictionary;
import me.gosimple.nbvcxz.resources.DictionaryBuilder;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@ValidateWithDB
public final class AdminSignUpForm implements ValidatableWithDB<List<ValidationError>> {

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
                           .addWord(name, 0)
                           .addWord(email, 0)
                           .createDictionary());

    // Create our configuration object and set our custom minimum entropy, and custom dictionary list
    final double minimumEntroy = 40d;
    Configuration configuration = new ConfigurationBuilder()
        .setMinimumEntropy(minimumEntroy)
        .setDictionaries(dictionaryList)
        .createConfiguration();

    return new Nbvcxz(configuration);
  }

  private boolean emailAlreadyExists(final BriventoryDB briventoryDB) {
    final int count = briventoryDB.query(
        session ->
            session.createQuery("select count(u) from User u where lower(u.email) = lower(:email)", Integer.class)
                   .setParameter("email", email.trim())
                   .getSingleResult()
    ).join();

    return count > 0;
  }

  @Override
  public List<ValidationError> validate(final BriventoryDB briventoryDB) {
    LinkedList<ValidationError> l = new LinkedList<>();
    if (name.isBlank())
      l.add(new ValidationError("name", "error.required"));
    if (email.isBlank())
      l.add(new ValidationError("email", "error.required"));
    if (emailAlreadyExists(briventoryDB))
      l.add(new ValidationError("email", "auth.signup.error.email.exist"));
    final Nbvcxz nbvcxz = nbvcxz();
    if (nbvcxz.estimate(password).getBasicScore() < Constraints.PASSWORD_MIN_STRENGTH)
      l.add(new ValidationError("password", "auth.signup.error.password.weak",
                                Collections.singletonList(Constraints.PASSWORD_MIN_STRENGTH)));
    if (!password.equals(passwordRepetition))
      l.add(new ValidationError("passwordRepetition", "auth.signup.error.passwordRepetition.mismatch"));
    return l;
  }

}

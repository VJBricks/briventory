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

/** The {@link play.data.Form} is specific to update the first name and the last name of an account. */
@ValidateWithAccountsRepository
public final class NameForm implements ValidatableWithAccountsRepository<List<ValidationError>> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************

  /** The first name. */
  @Required
  @MaxLength(Constraints.NAME_DOMAIN_LENGTH)
  private String firstname;

  /** The last name. */
  @Required
  @MaxLength(Constraints.NAME_DOMAIN_LENGTH)
  private String lastname;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /** Empty constructor for the binding with the request. */
  public NameForm() { /* No-op. */ }

  /**
   * Initializes this form.
   *
   * @param firstname the first name.
   * @param lastname the last name.
   */
  public NameForm(final String firstname, final String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
  }

  // *******************************************************************************************************************
  // ValidatableWithAccountsRepository Overrides
  // *******************************************************************************************************************
  @Override
  public List<ValidationError> validate(final AccountsRepository accountsRepository) {
    LinkedList<ValidationError> l = new LinkedList<>();
    if (firstname.isBlank())
      l.add(new ValidationError("firstname", "error.required"));
    if (lastname.isBlank())
      l.add(new ValidationError("lastname", "error.required"));
    return l;
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the first name. */
  public String getFirstname() { return firstname; }

  /**
   * Sets the first name.
   *
   * @param firstname the first name.
   */
  public void setFirstname(final String firstname) {
    this.firstname = firstname;
  }

  /** @return the last name. */
  public String getLastname() { return lastname; }

  /**
   * Sets the last name.
   *
   * @param lastname the last name.
   */
  public void setLastname(final String lastname) {
    this.lastname = lastname;
  }

}

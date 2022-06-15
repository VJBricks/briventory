package controllers.accounts;

import models.BrickSetTokens;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.Validatable;
import play.data.validation.ValidationError;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The {@code BrickSetTokensForm} handle the values of the various tokens and secrets needed to sync with BrickSet .
 */
public final class BrickSetTokensForm implements Validatable<List<ValidationError>> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The API key. */
  @Required
  @MaxLength(database.Constraints.TOKEN_DOMAIN_LENGTH)
  private String apiKey;

  /** The username. */
  @Required
  @MaxLength(database.Constraints.TOKEN_DOMAIN_LENGTH)
  private String username;

  /** The password. */
  @Required
  @MaxLength(database.Constraints.TOKEN_DOMAIN_LENGTH)
  private String password;

  /** The form contains new entries. */
  private boolean isNew = true;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /** Creates a new instance of {@link BrickSetTokensForm}. */
  public BrickSetTokensForm() { /* No-Op */ }

  /**
   * Creates a new instance of {@link BrickSetTokensForm}.
   *
   * @param optionalBrickSetTokens the instance of {@link BrickSetTokens}, wrapped into an {@link Optional} instance, to
   * fill this form.
   */
  public BrickSetTokensForm(final Optional<BrickSetTokens> optionalBrickSetTokens) {
    optionalBrickSetTokens.ifPresent(brickSetTokens -> {
      apiKey = brickSetTokens.getApiKey();
      username = brickSetTokens.getUsername();
      password = brickSetTokens.getPassword();
      isNew = false;
    });
  }

  // *******************************************************************************************************************
  // Validatable Overrides
  // *******************************************************************************************************************

  /**
   * Validates the various tokens.
   *
   * @return the validation errors or an empty list if there is no error.
   */
  @Override
  public List<ValidationError> validate() {
    return Collections.emptyList();
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return {@code true} if it the model does not exist, otherwise {@code false}. */
  public boolean isNew() { return isNew; }

  /** Sets this form as a filled form. */
  public void setAsFilled() { isNew = false; }

  /** @return the API key. */
  public String getApiKey() { return apiKey; }

  /**
   * Sets the API key.
   *
   * @param apiKey the API key.
   */
  public void setApiKey(final String apiKey) { this.apiKey = apiKey; }

  /** @return the username. */
  public String getUsername() { return username; }

  /**
   * Sets the username.
   *
   * @param username the username.
   */
  public void setUsername(final String username) { this.username = username; }

  /** @return the password. */
  public String getPassword() { return password; }

  /**
   * Sets the password.
   *
   * @param password the password.
   */
  public void setPassword(final String password) { this.password = password; }

}

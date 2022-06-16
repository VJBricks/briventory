package controllers.accounts;

import models.RebrickableTokens;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.Validatable;
import play.data.validation.ValidationError;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The {@code RebrickableTokensForm} handle the values of the various tokens and secrets needed to sync with BrickSet .
 */
public final class RebrickableTokensForm implements Validatable<List<ValidationError>> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The API key. */
  @Required
  @MaxLength(database.Constraints.TOKEN_DOMAIN_LENGTH)
  private String key;

  /** The form contains new entries. */
  private boolean isNew = true;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /** Creates a new instance of {@link RebrickableTokensForm}. */
  public RebrickableTokensForm() { /* No-Op */ }

  /**
   * Creates a new instance of {@link RebrickableTokensForm}.
   *
   * @param optionalRebrickableTokens the instance of {@link RebrickableTokens}, wrapped into an {@link Optional}
   * instance, to fill this form.
   */
  public RebrickableTokensForm(final Optional<RebrickableTokens> optionalRebrickableTokens) {
    optionalRebrickableTokens.ifPresent(rebrickableTokens -> {
      key = rebrickableTokens.getKey();
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
  public String getKey() { return key; }

  /**
   * Sets the API key.
   *
   * @param key the API key.
   */
  public void setKey(final String key) { this.key = key; }

}

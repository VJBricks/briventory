package controllers.accounts;

import models.BrickLinkTokens;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.Validatable;
import play.data.validation.ValidationError;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class BrickLinkTokensForm implements Validatable<List<ValidationError>> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The consumer key. */
  @Required
  @MaxLength(database.Constraints.TOKEN_DOMAIN_LENGTH)
  private String consumerKey;

  /** The consumer secret. */
  @Required
  @MaxLength(database.Constraints.TOKEN_DOMAIN_LENGTH)
  private String consumerSecret;

  /** The access token value. */
  @Required
  @MaxLength(database.Constraints.TOKEN_DOMAIN_LENGTH)
  private String tokenValue;

  /** The access token secret. */
  @Required
  @MaxLength(database.Constraints.TOKEN_DOMAIN_LENGTH)
  private String tokenSecret;

  /** The form contains new entries. */
  private boolean isNew = true;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /** Creates a new instance of {@link BrickLinkTokensForm}. */
  public BrickLinkTokensForm() { /* No-Op */ }

  /**
   * Creates a new instance of {@link BrickLinkTokensForm}.
   *
   * @param optionalBricklinkTokens the instance of {@link BrickLinkTokens}, wrapped into an {@link Optional} instance,
   * to fill this form.
   */
  public BrickLinkTokensForm(final Optional<BrickLinkTokens> optionalBricklinkTokens) {
    optionalBricklinkTokens.ifPresent(bricklinkTokens -> {
      consumerKey = bricklinkTokens.getConsumerKey();
      consumerSecret = bricklinkTokens.getConsumerSecret();
      tokenValue = bricklinkTokens.getTokenValue();
      tokenSecret = bricklinkTokens.getTokenSecret();
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

  /** @return the consumer key. */
  public String getConsumerKey() { return consumerKey; }

  /**
   * Sets the consumer key.
   *
   * @param consumerKey the consumer key.
   */
  public void setConsumerKey(final String consumerKey) { this.consumerKey = consumerKey; }

  /** @return the consumer secret. */
  public String getConsumerSecret() { return consumerSecret; }

  /**
   * Sets the consumer secret.
   *
   * @param consumerSecret the consumer secret.
   */
  public void setConsumerSecret(final String consumerSecret) { this.consumerSecret = consumerSecret; }

  /** @return the token value. */
  public String getTokenValue() { return tokenValue; }

  /**
   * Sets the token value.
   *
   * @param tokenValue the token value.
   */
  public void setTokenValue(final String tokenValue) { this.tokenValue = tokenValue; }

  /** @return the token secret. */
  public String getTokenSecret() { return tokenSecret; }

  /**
   * Sets the token secret.
   *
   * @param tokenSecret the token secret.
   */
  public void setTokenSecret(final String tokenSecret) { this.tokenSecret = tokenSecret; }

}

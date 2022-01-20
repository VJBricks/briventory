package controllers.accounts;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.Validatable;
import play.data.validation.ValidationError;

import java.util.Collections;
import java.util.List;

public final class BrickLinkTokensForm implements Validatable<List<ValidationError>> {

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

  /**
   * Validates the various tokens.
   *
   * @return the validation errors or an empty list if there is no error.
   */
  @Override
  public List<ValidationError> validate() {
    return Collections.emptyList();
  }

}

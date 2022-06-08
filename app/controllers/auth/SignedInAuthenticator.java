package controllers.auth;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/** This class handles the authentication. */
@Singleton
public final class SignedInAuthenticator extends Security.Authenticator {

  /** The injected {@link SessionHelper} instance. */
  private final SessionHelper sessionHelper;

  /**
   * Creates a new instance of {@link SessionHelper} by injecting the necessary parameters.
   *
   * @param sessionHelper the {@link SessionHelper}.
   */
  @Inject
  public SignedInAuthenticator(final SessionHelper sessionHelper) {
    this.sessionHelper = sessionHelper;
  }

  /**
   * Retrieves the e-mail address from the session stored in the {@link Http.Request}.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the e-mail address or an empty {@link Optional} instance.
   */
  @Override
  public Optional<String> getUsername(final Http.Request request) {
    return sessionHelper.retrieveEmail(request);
  }

  /**
   * Redirects the user to the sign-in page if the session is not valid.
   *
   * @param request the {@link Http.Request} to retrieve the session.
   *
   * @return the redirection on the sign-in page.
   */
  @Override
  public Result onUnauthorized(final Http.Request request) {
    return redirect(routes.PublicAuthController.signIn(request.uri()));
  }

}

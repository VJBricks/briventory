package controllers.auth;

import controllers.routes;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * The {@code SecuredAuth} {@link Controller} handle everything related to the authentication in the App that needs an
 * existing session.
 */
@Security.Authenticated(Secured.class)
public final class SecuredAuth extends Controller {

  /** @return the redirection to the index page with an empty session. */
  public Result signOut() {
    return redirect(routes.GlobalController.index()).withNewSession();
  }

}

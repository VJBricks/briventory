package controllers.auth;

import org.webjars.play.WebJarsUtil;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * The {@code Auth} {@link Controller} handle everything related to the authentication in the App. It also handle the
 * various sign up processes.
 */
public final class Auth extends Controller {

  /** The injected {@link WebJarsUtil}. */
  private final WebJarsUtil webJarsUtil;
  /** The injected {@link MessagesApi}. */
  private final MessagesApi messagesApi;
  /** The injected {@link FormFactory}. */
  private final FormFactory formFactory;

  /**
   * Creates a new {@link Auth} controller by injecting the parameters.
   *
   * @param webJarsUtil the {@link WebJarsUtil}.
   * @param messagesApi the {@link MessagesApi}.
   * @param formFactory the {@link FormFactory}.
   */
  @Inject
  public Auth(final WebJarsUtil webJarsUtil, final MessagesApi messagesApi, final FormFactory formFactory) {
    this.webJarsUtil = webJarsUtil;
    this.messagesApi = messagesApi;
    this.formFactory = formFactory;
  }

  public Result signIn() {
    return ok("signIn");
  }

  public Result doSignIn(final Http.Request request) {
    return ok("doSignIn");
  }

  public Result adminSignUp(final Http.Request request) {
    return ok(views.html.auth.adminSignUp.render(formFactory.form(AdminSignUpForm.class),
                                                 request,
                                                 webJarsUtil,
                                                 messagesApi.preferred(request)));
  }

  public Result doAdminSignUp(final Http.Request request) {
    Form<AdminSignUpForm> form = formFactory.form(AdminSignUpForm.class).bindFromRequest(request);
    if (form.hasErrors())
      return badRequest(views.html.auth.adminSignUp.render(form,
                                                           request,
                                                           webJarsUtil,
                                                           messagesApi.preferred(request)));

    return redirect(routes.Auth.signIn());
  }

}

package controllers.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.BriventoryDB;
import models.Administrator;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * The {@code PublicAuth} {@link Controller} handle everything related to the authentication in the App that doesn't
 * need an existing session. It also handle the various sign up processes.
 */
public final class PublicAuth extends Controller {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  /** The injected {@link MessagesApi}. */
  private final MessagesApi messagesApi;
  /** The injected {@link FormFactory}. */
  private final FormFactory formFactory;
  /** The injected {@link BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;
  /** The injected {@link SessionHelper} instance. */
  private final SessionHelper sessionHelper;

  // *******************************************************************************************************************
  // Injected Templates
  // *******************************************************************************************************************

  /** The {@link views.html.index} template. */
  private final views.html.auth.signIn signIn;
  /** The {@link views.html.auth.adminSignUp} template. */
  private final views.html.auth.adminSignUp adminSignUp;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link PublicAuth} controller by injecting the parameters.
   *
   * @param messagesApi the {@link MessagesApi}.
   * @param formFactory the {@link FormFactory}.
   * @param briventoryDB the {@link BriventoryDB}.
   * @param sessionHelper the {@link SessionHelper}.
   * @param signIn the {@link views.html.auth.signIn} template.
   * @param adminSignUp the {@link views.html.auth.adminSignUp} template.
   */
  @Inject
  public PublicAuth(final MessagesApi messagesApi, final FormFactory formFactory, final BriventoryDB briventoryDB,
                    final SessionHelper sessionHelper, final views.html.auth.signIn signIn,
                    final views.html.auth.adminSignUp adminSignUp) {
    this.messagesApi = messagesApi;
    this.formFactory = formFactory;
    this.briventoryDB = briventoryDB;
    this.sessionHelper = sessionHelper;
    this.signIn = signIn;
    this.adminSignUp = adminSignUp;
  }

  // *******************************************************************************************************************
  // Sign In Matters
  // *******************************************************************************************************************

  /**
   * Returns the {@link views.html.auth.signIn} view to the user.
   *
   * @param request the {@link Http.Request}.
   * @param redirectUrl the redirect URL.
   *
   * @return the {@link views.html.auth.signIn} view, encapsulate into a {@link Result} instance.
   */
  public Result signIn(final Http.Request request, final String redirectUrl) {
    Form<SignInForm> form = formFactory.form(SignInForm.class);
    Optional<String> url = Optional.ofNullable(redirectUrl);
    if (url.isPresent() && !url.get().isBlank()) {
      var signInForm = new SignInForm();
      signInForm.setRedirectUrl(url.get());
      form = form.fill(signInForm);
    }
    return ok(signIn.render(form, messagesApi.preferred(request), request));
  }

  /**
   * Performs the sign in, using the form values retrieved into the request. The view encapsulated into the {@link
   * Result} instance depends on the data validation:
   * <ul>
   *   <li>a <em>bad request</em> {@link Result} if the form contains errors or on wrong credentials;</li>
   *   <li>the redirection to the index page if the authentication succeeded.</li>
   * </ul>
   * <p><strong>Note:</strong> this methods returns a {@link CompletionStage} due to the database connection to
   * validate the credentials.</p>
   *
   * @param request the {@link Http.Request}.
   *
   * @return see the description.
   */
  public CompletionStage<Result> doSignIn(final Http.Request request) {

    return briventoryDB.query(session -> {
      Form<SignInForm> form = formFactory.form(SignInForm.class).bindFromRequest(request);
      if (form.hasErrors())
        return badRequest(signIn.render(form,
                                        messagesApi.preferred(request),
                                        request));

      List<User> users = User.findByEmail(session, form.get().getEmail());

      if (users.size() > 1) {
        // TODO Send mail to maintainer if > 1, duplicates e-mail should never happen
        return badRequest(signIn.render(form.withGlobalError("auth.signin.error"),
                                        messagesApi.preferred(request),
                                        request));
      }

      if (users.isEmpty()) {
        return badRequest(signIn.render(form.withGlobalError("auth.signin.error.badcredentials"),
                                        messagesApi.preferred(request),
                                        request));
      }

      final boolean passVerified = BCrypt.verifyer().verify(form.get().getPassword().getBytes(),
                                                            users.get(0).getPassword().getBytes()).verified;

      if (!passVerified) {
        return badRequest(signIn.render(form.withGlobalError("auth.signin.error.badcredentials"),
                                        messagesApi.preferred(request),
                                        request));
      }

      final String redirectUrl = form.get().getRedirectUrl();
      final Result result = redirectUrl == null || redirectUrl.isBlank() ?
          redirect(controllers.routes.GlobalController.index()) :
          redirect(redirectUrl);
      return result.withSession(sessionHelper.withUser(users.get(0), request));
    });
  }

  // *******************************************************************************************************************
  // Administrator Sign Up Matters
  // *******************************************************************************************************************

  /**
   * Returns the {@link views.html.auth.adminSignUp} view to the user.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.auth.adminSignUp} view, encapsulate into a {@link Result} instance.
   */
  public Result adminSignUp(final Http.Request request) {
    return ok(adminSignUp.render(formFactory.form(AdminSignUpForm.class),
                                 request,
                                 messagesApi.preferred(request)));
  }

  /**
   * Performs the administrator sign up, using the form values retrieved into the request. The view encapsulated into
   * the {@link Result} instance depends on the data validation:
   * <ul>
   *   <li>a <em>bad request</em> {@link Result} if the form contains errors or on wrong credentials;</li>
   *   <li>the redirection to the index page if the authentication succeeded.</li>
   * </ul>
   * <p><strong>Note:</strong> this methods returns a {@link CompletionStage} due to the database connection to
   * validate the credentials.</p>
   *
   * @param request the {@link Http.Request}.
   *
   * @return see the description.
   */
  public CompletionStage<Result> doAdminSignUp(final Http.Request request) {
    return briventoryDB.persist(entityManager -> {
      Form<AdminSignUpForm> form = formFactory.form(AdminSignUpForm.class).bindFromRequest(request);
      if (form.hasErrors())
        return badRequest(adminSignUp.render(form,
                                             request,
                                             messagesApi.preferred(request)));

      final var admin = new Administrator();
      admin.setName(form.get().getName());
      admin.setEmail(form.get().getEmail());
      admin.setClearPassword(form.get().getPassword());
      entityManager.persist(admin);

      return redirect(routes.PublicAuth.signIn(null));
    });
  }

}

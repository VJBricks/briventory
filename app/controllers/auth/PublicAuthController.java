package controllers.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import models.Account;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.AccountsRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * The {@code PublicAuth} {@link Controller} handle everything related to the authentication in the App that doesn't
 * need an existing session. It also handles the various sign-up processes.
 */
public final class PublicAuthController extends Controller {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  /** The injected {@link MessagesApi}. */
  private final MessagesApi messagesApi;
  /** The injected {@link FormFactory}. */
  private final FormFactory formFactory;
  /** The injected {@link SessionHelper} instance. */
  private final SessionHelper sessionHelper;
  /** The injected {@link AccountsRepository} instance. */
  private final AccountsRepository accountsRepository;

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
   * Creates a new {@link PublicAuthController} controller by injecting the parameters.
   *
   * @param messagesApi the {@link MessagesApi} instance.
   * @param formFactory the {@link FormFactory} instance.
   * @param sessionHelper the {@link SessionHelper} instance.
   * @param signIn the {@link views.html.auth.signIn} template.
   * @param adminSignUp the {@link views.html.auth.adminSignUp} template.
   * @param accountsRepository the {@link AccountsRepository} instance.
   */
  @Inject
  public PublicAuthController(final MessagesApi messagesApi, final FormFactory formFactory,
                              final AccountsRepository accountsRepository,
                              final SessionHelper sessionHelper, final views.html.auth.signIn signIn,
                              final views.html.auth.adminSignUp adminSignUp) {
    this.messagesApi = messagesApi;
    this.formFactory = formFactory;
    this.sessionHelper = sessionHelper;
    this.signIn = signIn;
    this.adminSignUp = adminSignUp;
    this.accountsRepository = accountsRepository;
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
   * Performs the sign in, using the form values retrieved into the request. The view encapsulated into the
   * {@link Result} instance depends on the data validation:
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

    Form<SignInForm> form = formFactory.form(SignInForm.class).bindFromRequest(request);
    if (form.hasErrors()) {
      return CompletableFuture.completedStage(badRequest(signIn.render(form, messagesApi.preferred(request), request)));
    }

    Optional<Account> optionalAccount = accountsRepository.findByEmail(form.get().getEmail());

    if (optionalAccount.isEmpty()) {
      return CompletableFuture.completedStage(
          badRequest(signIn.render(form.withGlobalError("auth.signin.error.badcredentials"),
                                   messagesApi.preferred(request),
                                   request)));
    }

    final boolean passVerified = BCrypt.verifyer().verify(form.get().getPassword().getBytes(),
                                                          optionalAccount.get().getPassword().getBytes()).verified;

    if (!passVerified) {
      return CompletableFuture.completedStage(
          badRequest(signIn.render(form.withGlobalError("auth.signin.error.badcredentials"),
                                   messagesApi.preferred(request),
                                   request)));
    }

    final String redirectUrl = form.get().getRedirectUrl();
    final Result result = redirectUrl == null || redirectUrl.isBlank() ?
                              redirect(controllers.routes.GlobalController.index()) :
                              redirect(redirectUrl);
    return CompletableFuture.completedStage(
        result.withSession(sessionHelper.withAccount(optionalAccount.get(), request)));
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
  public Result doAdminSignUp(final Http.Request request) {

    Form<AdminSignUpForm> form = formFactory.form(AdminSignUpForm.class).bindFromRequest(request);
    if (form.hasErrors())
      return badRequest(adminSignUp.render(form,
                                           request,
                                           messagesApi.preferred(request)));

    final var account = new Account(form.get().getFirstname(),
                                    form.get().getLastname(),
                                    form.get().getEmail(),
                                    form.get().getPassword(),
                                    true);
    accountsRepository.persist(account);
    return redirect(routes.PublicAuthController.signIn(null));
  }

}

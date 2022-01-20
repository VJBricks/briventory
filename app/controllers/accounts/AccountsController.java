package controllers.accounts;

import controllers.ErrorsController;
import controllers.auth.SessionHelper;
import controllers.auth.SignedInAuthenticator;
import models.Account;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.Optional;

@Security.Authenticated(SignedInAuthenticator.class)
public final class AccountsController extends Controller {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************

  /** The injected {@link MessagesApi} instance. */
  private final MessagesApi messagesApi;
  /** The injected {@link SessionHelper} instance. */
  private final SessionHelper sessionHelper;
  /** The injected {@link ErrorsController} instance. */
  private final ErrorsController errorsController;
  /** The injected {@link FormFactory} instance. */
  private final FormFactory formFactory;

  // *******************************************************************************************************************
  // Injected Templates
  // *******************************************************************************************************************

  /** The {@link views.html.users.settings } template. */
  private final views.html.users.settings settings;

  /** The {@link views.html.users.activity } template. */
  private final views.html.users.activity activity;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link AccountsController} by injecting the necessary parameters.
   *
   * @param messagesApi the {@link MessagesApi} instance.
   * @param sessionHelper the {@link SessionHelper} instance.
   * @param errorsController the {@link ErrorsController} instance.
   * @param formFactory the {@link FormFactory} instance.
   * @param settings the {@link views.html.users.settings} template.
   * @param activity the {@link views.html.users.activity} template.
   */
  @Inject
  public AccountsController(final MessagesApi messagesApi, final SessionHelper sessionHelper,
                            final ErrorsController errorsController, final FormFactory formFactory,
                            final views.html.users.settings settings,
                            final views.html.users.activity activity) {
    this.messagesApi = messagesApi;
    this.sessionHelper = sessionHelper;
    this.errorsController = errorsController;
    this.formFactory = formFactory;
    this.settings = settings;
    this.activity = activity;
  }

  // *******************************************************************************************************************
  // Entry Points
  // *******************************************************************************************************************

  /**
   * Returns the <em>activity</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.users.activity} page.
   */
  public Result activity(final Http.Request request) {
    final Optional<Account> accountOptional = sessionHelper.retrieveAccount(request);
    if (accountOptional.isPresent()) {
      final var preferred = messagesApi.preferred(request);
      return ok(activity.render(accountOptional.get(), preferred));
    }
    return errorsController.forbidden(request);
  }

  /**
   * Returns the <em>settings</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.users.settings} page.
   */
  public Result settings(final Http.Request request) {
    final Optional<Account> optionalAccount = sessionHelper.retrieveAccount(request);
    if (optionalAccount.isPresent()) {
      final var preferred = messagesApi.preferred(request);

      Form<EmailForm> emailForm = formFactory.form(EmailForm.class);
      emailForm = emailForm.fill(new EmailForm(optionalAccount.get().getEmail()));

      Form<NameForm> nameForm = formFactory.form(NameForm.class);
      nameForm = nameForm.fill(new NameForm(optionalAccount.get().getFirstname(), optionalAccount.get().getLastname()));

      return ok(settings.render(optionalAccount.get(), emailForm, nameForm, request, preferred));
    }
    return errorsController.forbidden(request);
  }

  public Result updateEmail(final Http.Request request) {
    return ok();
  }

  public Result updateName(final Http.Request request) {
    return ok();
  }

}

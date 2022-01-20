package controllers;

import controllers.auth.SessionHelper;
import controllers.auth.SignedInAuthenticator;
import models.Account;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.Optional;

/** This controller contains global actions. */
@Security.Authenticated(SignedInAuthenticator.class)
public final class GlobalController extends Controller {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************

  /** The injected {@link MessagesApi} instance. */
  private final MessagesApi messagesApi;
  /** The injected {@link SessionHelper} instance. */
  private final SessionHelper sessionHelper;
  /** The injected {@link ErrorsController} instance. */
  private final ErrorsController errorsController;

  // *******************************************************************************************************************
  // Injected Templates
  // *******************************************************************************************************************

  /** The {@link views.html.index} template. */
  private final views.html.index index;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link GlobalController} by injecting the necessary parameters.
   *
   * @param messagesApi the {@link MessagesApi} instance.
   * @param sessionHelper the {@link SessionHelper} instance.
   * @param errorsController the {@link ErrorsController} instance.
   * @param index the {@link views.html.index} template.
   */
  @Inject
  public GlobalController(final MessagesApi messagesApi, final SessionHelper sessionHelper,
                          final ErrorsController errorsController,
                          final views.html.index index) {
    this.messagesApi = messagesApi;
    this.sessionHelper = sessionHelper;
    this.errorsController = errorsController;
    this.index = index;
  }

  // *******************************************************************************************************************
  // Entry Points
  // *******************************************************************************************************************

  /**
   * Returns the <em>index</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.index} page.
   */
  public Result index(final Http.Request request) {
    final Optional<Account> accountOptional = sessionHelper.retrieveAccount(request);
    if (accountOptional.isPresent()) {
      final var preferred = messagesApi.preferred(request);
      return ok(index.render(accountOptional.get(), preferred));
    }
    return errorsController.forbidden(request);

  }

}

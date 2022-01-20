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

/** This controller handle all actions related to the Mini Figures. */
@Security.Authenticated(SignedInAuthenticator.class)
public final class MiniFiguresController extends Controller {

  /** The injected {@link MessagesApi} instance. */
  private final MessagesApi messagesApi;
  /** The injected {@link SessionHelper} instance. */
  private final SessionHelper sessionHelper;

  /** The injected {@link views.html.minifigures.index} template. */
  private final views.html.minifigures.index index;

  /**
   * Creates a new {@link MiniFiguresController} instance by injecting the necessary parameters.
   *
   * @param messagesApi the {@link MessagesApi}.
   * @param sessionHelper the {@link SessionHelper}.
   * @param index the {@link views.html.minifigures.index} template.
   */
  @Inject
  public MiniFiguresController(final MessagesApi messagesApi, final SessionHelper sessionHelper,
                               final views.html.minifigures.index index) {
    this.messagesApi = messagesApi;
    this.sessionHelper = sessionHelper;
    this.index = index;
  }

  /**
   * Returns the index page of the Mini Figures.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link Result} ecapsulating the {@link views.html.minifigures.index} page.
   */
  public Result index(final Http.Request request) {
    final Optional<Account> optionalAccount = sessionHelper.retrieveAccount(request);
    if (optionalAccount.isPresent())
      return ok(index.render(optionalAccount.get(), messagesApi.preferred(request)));
    return badRequest();
  }

}

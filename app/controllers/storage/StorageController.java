package controllers.storage;

import controllers.ErrorsController;
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

@Security.Authenticated(SignedInAuthenticator.class)
public final class StorageController extends Controller {

  private final MessagesApi messagesApi;
  private final SessionHelper sessionHelper;
  private final ErrorsController errorsController;
  private final views.html.storage.containers containers;

  @Inject
  public StorageController(final MessagesApi messagesApi, final SessionHelper sessionHelper,
                           final ErrorsController errorsController,
                           final views.html.storage.containers containers) {
    this.messagesApi = messagesApi;
    this.sessionHelper = sessionHelper;
    this.errorsController = errorsController;
    this.containers = containers;
  }

  public Result containers(final Http.Request request) {
    final Optional<Account> accountOptional = sessionHelper.retrieveAccount(request);
    if (accountOptional.isPresent()) {
      final var preferred = messagesApi.preferred(request);
      return ok(containers.render(accountOptional.get(), preferred));
    }
    return errorsController.forbidden(request);
  }

}

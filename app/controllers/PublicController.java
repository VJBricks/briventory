package controllers;

import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

public final class PublicController extends Controller {

  /** The injected {@link MessagesApi} instance. */
  private final MessagesApi messagesApi;

  @Inject
  public PublicController(final MessagesApi messagesApi) {
    this.messagesApi = messagesApi;
  }

  public Result credits(final Http.Request request) {
    return ok(views.html.credits.render(messagesApi.preferred(request)));
  }

}

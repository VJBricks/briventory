package controllers;

import controllers.auth.Secured;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;

/** This controller contains global actions. */
@Security.Authenticated(Secured.class)
public final class GlobalController extends Controller {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************

  /** The injected {@link MessagesApi} instance. */
  private final MessagesApi messagesApi;

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
   * @param index the {@link views.html.index} template.
   */
  @Inject
  public GlobalController(final MessagesApi messagesApi, final views.html.index index) {
    this.messagesApi = messagesApi;
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
    return ok(index.render(messagesApi.preferred(request)));
  }

}

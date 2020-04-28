package controllers;

import controllers.auth.Secured;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;

/** This controller handle all actions related to the Mini Figures. */
@Security.Authenticated(Secured.class)
public final class MiniFiguresController extends Controller {

  /** The injected {@link MessagesApi} instance. */
  private final MessagesApi messagesApi;

  /** The injected {@link views.html.minifigures.index} template. */
  private final views.html.minifigures.index index;

  /**
   * Creates a new {@link MiniFiguresController} instance by injecting the necessary parameters.
   *
   * @param messagesApi the {@link MessagesApi}.
   * @param index the {@link views.html.minifigures.index} template.
   */
  @Inject
  public MiniFiguresController(final MessagesApi messagesApi, final views.html.minifigures.index index) {
    this.messagesApi = messagesApi;
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
    return ok(index.render(messagesApi.preferred(request)));
  }

}

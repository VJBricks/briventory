package controllers;

import database.BriventoryDB;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

public final class ErrorsController extends Controller {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************

  /** The injected {@link MessagesApi} instance. */
  private final MessagesApi messagesApi;
  /** The injected {@link BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;

  // *******************************************************************************************************************
  // Injected Templates
  // *******************************************************************************************************************

  /** The {@link views.html.errors.badRequest} template. */
  private final views.html.errors.badRequest badRequestTemplate;
  /** The {@link views.html.errors.forbidden} template. */
  private final views.html.errors.forbidden forbiddenTemplate;
  /** The {@link views.html.errors.notFound} template. */
  private final views.html.errors.notFound notFoundTemplate;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link GlobalController} by injecting the necessary parameters.
   *
   * @param messagesApi the {@link MessagesApi} instance.
   * @param briventoryDB the {@link BriventoryDB} instance.
   * @param badRequestTemplate the {@link views.html.errors.badRequest} template.
   * @param forbiddenTemplate the {@link views.html.errors.forbidden} template.
   * @param notFoundTemplate the {@link views.html.errors.notFound} template.
   */
  @Inject
  public ErrorsController(final MessagesApi messagesApi, final BriventoryDB briventoryDB,
                          final views.html.errors.badRequest badRequestTemplate,
                          final views.html.errors.forbidden forbiddenTemplate,
                          final views.html.errors.notFound notFoundTemplate) {
    this.messagesApi = messagesApi;
    this.briventoryDB = briventoryDB;
    this.badRequestTemplate = badRequestTemplate;
    this.forbiddenTemplate = forbiddenTemplate;
    this.notFoundTemplate = notFoundTemplate;
  }

  // *******************************************************************************************************************
  // Entry points
  // *******************************************************************************************************************

  /**
   * Display the <em>bad request</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.errors.badRequest} page.
   */
  public Result badRequest(final Http.Request request) {
    return Results.status(Http.Status.BAD_REQUEST,
                          badRequestTemplate.render(request, messagesApi.preferred(request)));
  }

  /**
   * Display the <em>forbidden</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.errors.forbidden} page.
   */
  public Result forbidden(final Http.Request request) {
    return Results.status(Http.Status.FORBIDDEN,
                          forbiddenTemplate.render(request, messagesApi.preferred(request)));
  }

  /**
   * Display the <em>not found</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.errors.notFound} page.
   */
  public Result notFound(final Http.Request request) {
    return Results.status(Http.Status.NOT_FOUND,
                          notFoundTemplate.render(request, messagesApi.preferred(request)));
  }

}

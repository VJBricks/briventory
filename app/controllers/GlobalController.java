package controllers;

import database.BriventoryDB;
import org.webjars.play.WebJarsUtil;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

/** This controller contains global actions. */
public final class GlobalController extends Controller {

  /** The injected {@link WebJarsUtil} instance. */
  private final WebJarsUtil webJarsUtil;
  /** The injected {@link MessagesApi} instance. */
  private final MessagesApi messagesApi;
  /** The injected {@link BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;

  /**
   * Creates a new instance of {@link GlobalController} by injecting the necessary parameters.
   *
   * @param webJarsUtil the {@link WebJarsUtil} instance.
   * @param messagesApi the {@link MessagesApi} instance.
   * @param briventoryDB the {@link BriventoryDB} instance.
   */
  @Inject
  public GlobalController(final WebJarsUtil webJarsUtil, final MessagesApi messagesApi,
                          final BriventoryDB briventoryDB) {
    this.webJarsUtil = webJarsUtil;
    this.messagesApi = messagesApi;
    this.briventoryDB = briventoryDB;
  }

  /**
   * Returns the <em>index</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.index} page.
   */
  public Result index(final Http.Request request) {
    return ok(views.html.index.render(webJarsUtil, messagesApi.preferred(request)));
  }

  /**
   * Display the <em>maintenance</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.errors.maintenance} page.
   */
  public Result maintenance(final Http.Request request) {
    return Results.status(Http.Status.SERVICE_UNAVAILABLE,
                          views.html.errors.maintenance.render(webJarsUtil, messagesApi.preferred(request)));
  }

  /**
   * Display the <em>status</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.status} page.
   */
  public Result status(final Http.Request request) {
    Messages messages = messagesApi.preferred(request);
    if (!briventoryDB.isInMaintenance())
      return badRequest(views.html.errors.badRequest.render(request, webJarsUtil, messages));

    return ok(views.html.status.render(briventoryDB.isDatabaseInitialized(),
                                       briventoryDB.hasActiveAdministrator(),
                                       request,
                                       webJarsUtil,
                                       messages));
  }

}

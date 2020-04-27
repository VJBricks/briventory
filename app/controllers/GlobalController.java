package controllers;

import database.BriventoryDB;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/** This controller contains global actions. */
public final class GlobalController extends Controller {

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

  /** The {@link views.html.index} template. */
  private final views.html.index index;
  /** The {@link views.html.errors.maintenance} template. */
  private final views.html.errors.maintenance maintenance;
  /** The {@link views.html.errors.badRequest} template. */
  private final views.html.errors.badRequest badRequest;
  /** The {@link views.html.status} template. */
  private final views.html.status status;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link GlobalController} by injecting the necessary parameters.
   *
   * @param messagesApi the {@link MessagesApi} instance.
   * @param briventoryDB the {@link BriventoryDB} instance.
   * @param index the {@link views.html.index} template.
   * @param maintenance the {@link views.html.errors.maintenance} template.
   * @param badRequest the {@link views.html.errors.badRequest} template.
   * @param status the {@link views.html.status} template.
   */
  @Inject
  public GlobalController(final MessagesApi messagesApi, final BriventoryDB briventoryDB, final views.html.index index,
                          final views.html.errors.maintenance maintenance,
                          final views.html.errors.badRequest badRequest,
                          final views.html.status status) {
    this.messagesApi = messagesApi;
    this.briventoryDB = briventoryDB;
    this.index = index;
    this.maintenance = maintenance;
    this.badRequest = badRequest;
    this.status = status;
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

  /**
   * Display the <em>maintenance</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.errors.maintenance} page.
   */
  public Result maintenance(final Http.Request request) {
    return Results.status(Http.Status.SERVICE_UNAVAILABLE,
                          maintenance.render(messagesApi.preferred(request)));
  }

  /**
   * Display the <em>status</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.status} page.
   */
  public CompletionStage<Result> status(final Http.Request request) {

    return briventoryDB.query(session -> {
      Messages messages = messagesApi.preferred(request);
      if (!briventoryDB.isInMaintenance(session))
        return badRequest(badRequest.render(request, messages));

      return ok(status.render(briventoryDB.isDatabaseInitialized(session),
                              briventoryDB.hasActiveAdministrator(session),
                              request,
                              messages));
    });
  }

}

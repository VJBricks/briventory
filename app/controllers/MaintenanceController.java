package controllers;

import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repositories.AccountsRepository;
import repositories.ConfigurationRepository;

import javax.inject.Inject;

public final class MaintenanceController extends Controller {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************

  /** The injected {@link MessagesApi} instance. */
  private final MessagesApi messagesApi;
  /** The injected {@link AccountsRepository} instance. */
  private final AccountsRepository accountsRepository;
  /** The injected {@link ConfigurationRepository} instance. */
  private final ConfigurationRepository revisionRepository;

  // *******************************************************************************************************************
  // Injected Templates
  // *******************************************************************************************************************

  /** The {@link views.html.errors.maintenance} template. */
  private final views.html.errors.maintenance maintenanceView;
  /** The {@link views.html.errors.badRequest} template. */
  private final views.html.errors.badRequest badRequestView;
  /** The {@link views.html.status} template. */
  private final views.html.status statusView;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link GlobalController} by injecting the necessary parameters.
   *
   * @param messagesApi the {@link MessagesApi} instance.
   * @param accountsRepository the {@link AccountsRepository} instance.
   * @param revisionRepository the {@link ConfigurationRepository} instance.
   * @param maintenanceView the {@link views.html.errors.maintenance} template.
   * @param badRequestView the {@link views.html.errors.badRequest} template.
   * @param statusView the {@link views.html.status} template.
   */
  @Inject
  public MaintenanceController(final MessagesApi messagesApi, final AccountsRepository accountsRepository,
                               final ConfigurationRepository revisionRepository,
                               final views.html.errors.maintenance maintenanceView,
                               final views.html.errors.badRequest badRequestView,
                               final views.html.status statusView) {
    this.messagesApi = messagesApi;
    this.accountsRepository = accountsRepository;
    this.revisionRepository = revisionRepository;
    this.maintenanceView = maintenanceView;
    this.badRequestView = badRequestView;
    this.statusView = statusView;
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
                          maintenanceView.render(messagesApi.preferred(request)));
  }

  /**
   * Display the <em>status</em> page.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@link views.html.status} page.
   */
  public Result status(final Http.Request request) {

    final boolean isDatabaseInitialized = revisionRepository.isDatabaseInitialized();
    final boolean hasActiveAdministrator = accountsRepository.hasActiveAdministrator();
    final boolean maintenance = !isDatabaseInitialized || !hasActiveAdministrator;

    final var messages = messagesApi.preferred(request);
    if (!maintenance)
      return badRequest(badRequestView.render(request, messages));

    return ok(statusView.render(isDatabaseInitialized,
                                hasActiveAdministrator,
                                request,
                                messages));
  }

}

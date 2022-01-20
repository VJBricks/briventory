package globalhandlers;

import controllers.MaintenanceController;
import play.api.http.JavaCompatibleHttpRequestHandler;
import play.http.DefaultHttpRequestHandler;
import play.http.HandlerForRequest;
import play.libs.streams.Accumulator;
import play.mvc.EssentialAction;
import play.mvc.Http;
import play.routing.Router;
import repositories.ConfigurationRepository;
import repositories.AccountsRepository;

import javax.inject.Inject;

/**
 * {@code CustomHttpRequestHandler} detects the initialization of the database. If it is correctly initialized, the App
 * routes are used as usual. Otherwise, only some routes are provided and every thing else will be redirected to a
 * maintenance page.
 * <p>In maintenance mode, the following routes are allowed:</p>
 * <ul>
 *   <li>{@code /maintenance}</li>
 *   <li>{@code /status}</li>
 *   <li>{@code /auth}</li>
 *   <li>{@code /assets}</li>
 *   <li>{@code /webjars}</li>
 *   <li>{@code /robots.txt}</li>
 * </ul>
 */
public final class CustomHttpRequestHandler extends DefaultHttpRequestHandler {

  /** The injected {@link controllers.MaintenanceController} instance. */
  private final MaintenanceController maintenanceController;
  /** The injected {@link ConfigurationRepository} instance. */
  private final ConfigurationRepository revisionRepository;
  /** The injected {@link AccountsRepository} instance. */
  private final AccountsRepository usersRepository;

  @Inject
  public CustomHttpRequestHandler(final JavaCompatibleHttpRequestHandler underlying,
                                  final MaintenanceController maintenanceController,
                                  final ConfigurationRepository revisionRepository,
                                  final AccountsRepository usersRepository) {
    super(underlying);
    this.maintenanceController = maintenanceController;
    this.revisionRepository = revisionRepository;
    this.usersRepository = usersRepository;
  }

  @Override
  public HandlerForRequest handlerForRequest(final Http.RequestHeader requestHeader) {

    boolean maintenance = false;

    try {
      final boolean isDatabaseInitialized = revisionRepository.isDatabaseInitialized();
      final boolean hasActiveAdministrator = usersRepository.hasActiveAdministrator();
      maintenance = !isDatabaseInitialized || !hasActiveAdministrator;
    } catch (Exception e) {
      maintenance = true;
    }

    if (maintenance && !requestHeader.uri().matches("^/(maintenance|status|auth|assets|webjars|robots.txt).*")) {
      var minimalRouter = Router.empty();
      var request = requestHeader.withBody(null);
      var handler = minimalRouter.route(requestHeader).orElseGet(
          () -> EssentialAction.of(rh -> Accumulator.done(maintenanceController.maintenance(request))));
      return new HandlerForRequest(requestHeader, handler);
    }
    return super.handlerForRequest(requestHeader);
  }

}

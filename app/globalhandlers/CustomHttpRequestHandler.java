package globalhandlers;

import controllers.MaintenanceController;
import database.BriventoryDB;
import play.api.http.JavaCompatibleHttpRequestHandler;
import play.api.mvc.Handler;
import play.http.DefaultHttpRequestHandler;
import play.http.HandlerForRequest;
import play.libs.streams.Accumulator;
import play.mvc.EssentialAction;
import play.mvc.Http;
import play.routing.Router;

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
  /** The injected {@link database.BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;

  @Inject
  public CustomHttpRequestHandler(final JavaCompatibleHttpRequestHandler underlying,
                                  final MaintenanceController maintenanceController,
                                  final BriventoryDB briventoryDB) {
    super(underlying);
    this.maintenanceController = maintenanceController;
    this.briventoryDB = briventoryDB;
  }

  @Override
  public HandlerForRequest handlerForRequest(final Http.RequestHeader requestHeader) {
    return briventoryDB.query(session -> {
      final boolean isInMaintenance = briventoryDB.isInMaintenance(session);

      if (isInMaintenance && !requestHeader.uri().matches("^/(maintenance|status|auth|assets|webjars|robots.txt).*")) {
        Router minimalRouter = Router.empty();
        Http.Request request = requestHeader.withBody(null);
        Handler handler = minimalRouter.route(requestHeader).orElseGet(
            () -> EssentialAction.of(rh -> Accumulator.done(maintenanceController.maintenance(request))));
        return new HandlerForRequest(requestHeader, handler);
      }
      return super.handlerForRequest(requestHeader);
    }).join();
  }

}

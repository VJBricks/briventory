package globalhandlers;

import controllers.GlobalController;
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

public final class CustomHttpRequestHandler extends DefaultHttpRequestHandler {

  /** The injected {@link controllers.GlobalController} instance. */
  private final GlobalController globalController;
  /** The injected {@link database.BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;

  @Inject
  public CustomHttpRequestHandler(final JavaCompatibleHttpRequestHandler underlying,
                                  final GlobalController globalController,
                                  final BriventoryDB briventoryDB) {
    super(underlying);
    this.globalController = globalController;
    this.briventoryDB = briventoryDB;
  }

  @Override
  public HandlerForRequest handlerForRequest(final Http.RequestHeader requestHeader) {
    final boolean isInMaintenance = briventoryDB.isInMaintenance();

    if (isInMaintenance && !requestHeader.uri().matches("^/(maintenance|status|auth|assets|webjars).*")) {
      Router minimalRouter = Router.empty();
      Http.Request request = requestHeader.withBody(null);
      Handler handler = minimalRouter.route(requestHeader).orElseGet(
          () -> EssentialAction.of(rh -> Accumulator.done(globalController.maintenance(request))));
      return new HandlerForRequest(requestHeader, handler);
    }
    return super.handlerForRequest(requestHeader);
  }

}

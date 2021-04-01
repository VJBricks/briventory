package globalhandlers;

import com.typesafe.config.Config;
import controllers.ErrorsController;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.i18n.MessagesApi;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public final class ErrorHandler extends DefaultHttpErrorHandler {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  /** The injected instance of {@link MessagesApi}. */
  private final MessagesApi messagesApi;
  /** The injected instance of {@link ErrorsController}. */
  private final ErrorsController errorsController;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link ErrorHandler} by injecting the necessary parameters.
   *
   * @param config the {@link Config} instance.
   * @param environment the {@link Environment} instance.
   * @param sourceMapper the {@link OptionalSourceMapper} instance.
   * @param routes the {@link Provider} instance.
   * @param errorsController the {@link ErrorsController} instance.
   * @param messagesApi the {@link MessagesApi} instance.
   */
  @Inject
  public ErrorHandler(final Config config, final Environment environment, final OptionalSourceMapper sourceMapper,
                      final Provider<Router> routes, final ErrorsController errorsController,
                      final MessagesApi messagesApi) {
    super(config, environment, sourceMapper, routes);
    this.errorsController = errorsController;
    this.messagesApi = messagesApi;
  }

  @Override
  protected CompletionStage<Result> onProdServerError(final RequestHeader request, final UsefulException exception) {
    return CompletableFuture.completedFuture(
        Results.internalServerError("A server error occurred: " + exception.getMessage()));
  }

  @Override
  protected CompletionStage<Result> onForbidden(final RequestHeader request, final String message) {
    return CompletableFuture.completedFuture(
        Results.forbidden("You're not allowed to access this resource."));
  }

}

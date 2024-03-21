package controllers.storage;

import controllers.ErrorsController;
import controllers.auth.SessionHelper;
import controllers.auth.SignedInAuthenticator;
import models.Account;
import models.Container;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import play.routing.JavaScriptReverseRouter;
import repositories.ContainerTypesRepository;
import repositories.ContainersRepository;
import repositories.LockerSizesRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static play.mvc.Http.MimeTypes.JAVASCRIPT;

@Security.Authenticated(SignedInAuthenticator.class)
public final class StorageController extends Controller {

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  /** The injected instance of {@link MessagesApi}. */
  private final MessagesApi messagesApi;
  /** The injected instance of {@link FormFactory}. */
  private final FormFactory formFactory;
  /** The injected instance of {@link SessionHelper}. */
  private final SessionHelper sessionHelper;
  /** The injected instance of {@link ErrorsController}. */
  private final ErrorsController errorsController;
  /** The injected instance of {@link ContainerTypesRepository}. */
  private final ContainerTypesRepository containerTypesRepository;
  /** The injected instance of {@link LockerSizesRepository}. */
  private final LockerSizesRepository lockerSizesRepository;
  /** The injected instance of {@link ContainersRepository}. */
  private final ContainersRepository containersRepository;
  /** The injected instance of {@link views.html.storage.storage}. */
  private final views.html.storage.storage storage;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link StorageController}, by using dependency injection.
   *
   * @param messagesApi the {@link MessagesApi}.
   * @param formFactory the {@link FormFactory}.
   * @param sessionHelper the {@link SessionHelper}.
   * @param errorsController the {@link ErrorsController}.
   * @param containerTypesRepository the {@link ContainersRepository}.
   * @param lockerSizesRepository the {@link LockerSizesRepository}.
   * @param containersRepository the {@link ContainersRepository}.
   * @param storage the {@link views.html.storage.storage}.
   */
  @Inject
  public StorageController(final MessagesApi messagesApi,
                           final FormFactory formFactory,
                           final SessionHelper sessionHelper,
                           final ErrorsController errorsController,
                           final ContainerTypesRepository containerTypesRepository,
                           final LockerSizesRepository lockerSizesRepository,
                           final ContainersRepository containersRepository,
                           final views.html.storage.storage storage) {
    this.messagesApi = messagesApi;
    this.formFactory = formFactory;
    this.sessionHelper = sessionHelper;
    this.errorsController = errorsController;
    this.containerTypesRepository = containerTypesRepository;
    this.lockerSizesRepository = lockerSizesRepository;
    this.containersRepository = containersRepository;
    this.storage = storage;
  }

  // *******************************************************************************************************************
  // Entry Points
  // *******************************************************************************************************************

  /**
   * Provides the JavaScript routes in a JS object called {@code settingsJSRoutes}.
   *
   * @param request the {@link Http.Request}.
   *
   * @return the {@code settingsJSRoutes} JS object.
   */
  public Result js(final Http.Request request) {
    return ok(JavaScriptReverseRouter.create("storageJSRoutes", "jQuery.ajax", request.host(),
                                             controllers.storage.routes.javascript.StorageController.containers()))
        .as(JAVASCRIPT);
  }

  public Result storage(final Http.Request request) {
    final Optional<Account> accountOptional = sessionHelper.retrieveAccount(request);
    if (accountOptional.isPresent()) {
      final var preferred = messagesApi.preferred(request);
      return ok(storage.render(accountOptional.get(),
                               formFactory.form(FilterForm.class),
                               containerTypesRepository.getAll(),
                               containerTypesRepository.getUnused(),
                               lockerSizesRepository.getAll(),
                               lockerSizesRepository.getUnused(),
                               preferred));
    }
    return errorsController.forbidden(request);
  }

  public Result containers(final Http.Request request) {
    final Optional<Account> accountOptional = sessionHelper.retrieveAccount(request);
    if (accountOptional.isPresent()) {
      final var preferred = messagesApi.preferred(request);
      FilterForm filterForm = formFactory.form(FilterForm.class)
                                         .bindFromRequest(request)
                                         .get();

      final Account account = accountOptional.get();
      final List<Container> containers = containersRepository.findAll(account,
                                                                      filterForm.getSharedContainers(),
                                                                      filterForm.getPrivateContainers(),
                                                                      filterForm.getIdContainerType(),
                                                                      filterForm.getIdLockerSize());

      return ok(views.html.storage.containers.render(containers, preferred));
    }
    return errorsController.forbidden(request);
  }

  public Result lockerSizes(final Http.Request request) {
    return status(Http.Status.NOT_IMPLEMENTED);
  }

  public Result lockers(final Http.Request request) {
    return status(Http.Status.NOT_IMPLEMENTED);
  }

}

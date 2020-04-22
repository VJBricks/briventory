package controllers.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ch.varani.briventory.models.Admin;
import database.BriventoryDB;
import org.webjars.play.WebJarsUtil;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * The {@code Auth} {@link Controller} handle everything related to the authentication in the App. It also handle the
 * various sign up processes.
 */
public final class Auth extends Controller {

  /** The cost for BCrypt. */
  private static final int BCRYPT_COST = 13;

  /** The injected {@link WebJarsUtil}. */
  private final WebJarsUtil webJarsUtil;
  /** The injected {@link MessagesApi}. */
  private final MessagesApi messagesApi;
  /** The injected {@link FormFactory}. */
  private final FormFactory formFactory;
  /** The injected {@link BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;

  /**
   * Creates a new {@link Auth} controller by injecting the parameters.
   *
   * @param webJarsUtil the {@link WebJarsUtil}.
   * @param messagesApi the {@link MessagesApi}.
   * @param formFactory the {@link FormFactory}.
   * @param briventoryDB the {@link BriventoryDB}.
   */
  @Inject
  public Auth(final WebJarsUtil webJarsUtil, final MessagesApi messagesApi, final FormFactory formFactory,
              final BriventoryDB briventoryDB) {
    this.webJarsUtil = webJarsUtil;
    this.messagesApi = messagesApi;
    this.formFactory = formFactory;
    this.briventoryDB = briventoryDB;
  }

  public Result signIn() {
    return ok("signIn");
  }

  public Result doSignIn(final Http.Request request) {
    return ok("doSignIn");
  }

  public Result adminSignUp(final Http.Request request) {
    return ok(views.html.auth.adminSignUp.render(formFactory.form(AdminSignUpForm.class),
                                                 request,
                                                 webJarsUtil,
                                                 messagesApi.preferred(request)));
  }

  public CompletionStage<Result> doAdminSignUp(final Http.Request request) {
    return CompletableFuture.supplyAsync(() -> {
      Form<AdminSignUpForm> form = formFactory.form(AdminSignUpForm.class).bindFromRequest(request);
      if (form.hasErrors())
        return badRequest(views.html.auth.adminSignUp.render(form,
                                                             request,
                                                             webJarsUtil,
                                                             messagesApi.preferred(request)));

      return briventoryDB.persist(entityManager -> {
        final Admin admin = new Admin().setName(form.get().getName())
                                       .setEmail(form.get().getEmail())
                                       .setPassword(BCrypt.withDefaults()
                                                          .hashToString(BCRYPT_COST, form.get()
                                                                                         .getPassword()
                                                                                         .toCharArray()));
        entityManager.persist(admin);

        return redirect(routes.Auth.signIn());
      }).join();

    });
  }

}

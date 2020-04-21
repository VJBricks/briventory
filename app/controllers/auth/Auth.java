package controllers.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ch.varani.briventory.tables.records.AdminRecord;
import ch.varani.briventory.tables.records.UserRecord;
import database.BriventoryDBContext;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.webjars.play.WebJarsUtil;
import play.api.db.Database;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecution;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import static ch.varani.briventory.Tables.ADMIN;
import static ch.varani.briventory.Tables.USER;

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
  /** The {@link Executor} retrieved from the injected {@link BriventoryDBContext} instance. */
  private final Executor executor;
  /** The injected {@link Database} instance. */
  private final Database database;

  /**
   * Creates a new {@link Auth} controller by injecting the parameters.
   *
   * @param webJarsUtil the {@link WebJarsUtil}.
   * @param messagesApi the {@link MessagesApi}.
   * @param formFactory the {@link FormFactory}.
   * @param context the {@link BriventoryDBContext}.
   * @param database the {@link Database}.
   */
  @Inject
  public Auth(final WebJarsUtil webJarsUtil, final MessagesApi messagesApi, final FormFactory formFactory,
              final BriventoryDBContext context, final Database database) {
    this.webJarsUtil = webJarsUtil;
    this.messagesApi = messagesApi;
    this.formFactory = formFactory;
    executor = HttpExecution.fromThread((Executor) context);
    this.database = database;
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

      return database.withTransaction(connection -> {
        DSLContext dialect = DSL.using(connection, SQLDialect.POSTGRES);

        final UserRecord newUser = dialect.newRecord(USER);
        newUser.setName(form.get().getName());
        newUser.setEmail(form.get().getEmail());
        newUser.setPassword(BCrypt.withDefaults().hashToString(BCRYPT_COST, form.get().getPassword().toCharArray()));
        newUser.store();

        final AdminRecord newAdmin = dialect.newRecord(ADMIN);
        newAdmin.setIduser(newUser.getId());
        newAdmin.store();

        return redirect(routes.Auth.signIn());
      });

    });
  }

}

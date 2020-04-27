package controllers.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.BriventoryDB;
import models.Admin;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * The {@code Auth} {@link Controller} handle everything related to the authentication in the App. It also handle the
 * various sign up processes.
 */
public final class Auth extends Controller {

  // *******************************************************************************************************************
  // Constants
  // *******************************************************************************************************************
  /** The cost for BCrypt. */
  private static final int BCRYPT_COST = 13;

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  /** The injected {@link MessagesApi}. */
  private final MessagesApi messagesApi;
  /** The injected {@link FormFactory}. */
  private final FormFactory formFactory;
  /** The injected {@link BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;

  // *******************************************************************************************************************
  // Injected Templates
  // *******************************************************************************************************************

  /** The {@link views.html.index} template. */
  private final views.html.auth.signIn signIn;
  /** The {@link views.html.auth.adminSignUp} template. */
  private final views.html.auth.adminSignUp adminSignUp;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link Auth} controller by injecting the parameters.
   *
   * @param messagesApi the {@link MessagesApi}.
   * @param formFactory the {@link FormFactory}.
   * @param briventoryDB the {@link BriventoryDB}.
   * @param signIn the {@link views.html.auth.signIn} template.
   * @param adminSignUp the {@link views.html.auth.adminSignUp} template.
   */
  @Inject
  public Auth(final MessagesApi messagesApi, final FormFactory formFactory,
              final BriventoryDB briventoryDB, final views.html.auth.signIn signIn,
              final views.html.auth.adminSignUp adminSignUp) {
    this.messagesApi = messagesApi;
    this.formFactory = formFactory;
    this.briventoryDB = briventoryDB;
    this.signIn = signIn;
    this.adminSignUp = adminSignUp;
  }

  public Result signIn(final Http.Request request) {
    return ok(signIn.render(formFactory.form(SignInForm.class),
                            messagesApi.preferred(request),
                            request));
  }

  public CompletionStage<Result> doSignIn(final Http.Request request) {

    return briventoryDB.query(session -> {
      Form<SignInForm> form = formFactory.form(SignInForm.class).bindFromRequest(request);
      if (form.hasErrors())
        return badRequest(signIn.render(form,
                                        messagesApi.preferred(request),
                                        request));

      List<User> users = session.createQuery("select u from User u where u.email = :email", User.class)
                                .setParameter("email", form.get().getEmail())
                                .getResultList();

      if (users.size() > 1) {
        // TODO Send mail to maintainer if > 1
        return badRequest(signIn.render(form.withGlobalError("auth.signin.error"),
                                        messagesApi.preferred(request),
                                        request));
      }

      if (users.isEmpty()) {
        return badRequest(signIn.render(form.withGlobalError("auth.signin.error.badcredentials"),
                                        messagesApi.preferred(request),
                                        request));
      }

      final boolean passVerified = BCrypt.verifyer().verify(form.get().getPassword().getBytes(),
                                                            users.get(0).getPassword().getBytes()).verified;

      if (!passVerified) {
        return badRequest(signIn.render(form.withGlobalError("auth.signin.error.badcredentials"),
                                        messagesApi.preferred(request),
                                        request));
      }

      return redirect(controllers.routes.GlobalController.index())
          .addingToSession(request, "iduser", users.get(0).getId().toString());
    });
  }

  public Result adminSignUp(final Http.Request request) {
    return ok(adminSignUp.render(formFactory.form(AdminSignUpForm.class),
                                 request,
                                 messagesApi.preferred(request)));
  }

  public CompletionStage<Result> doAdminSignUp(final Http.Request request) {
    return briventoryDB.persist(entityManager -> {
      Form<AdminSignUpForm> form = formFactory.form(AdminSignUpForm.class).bindFromRequest(request);
      if (form.hasErrors())
        return badRequest(adminSignUp.render(form,
                                             request,
                                             messagesApi.preferred(request)));

      final Admin admin = new Admin();
      admin.setName(form.get().getName());
      admin.setEmail(form.get().getEmail());
      admin.setPassword(BCrypt.withDefaults()
                              .hashToString(BCRYPT_COST, form.get()
                                                             .getPassword()
                                                             .toCharArray()));
      entityManager.persist(admin);

      return redirect(routes.Auth.signIn());
    });
  }

}

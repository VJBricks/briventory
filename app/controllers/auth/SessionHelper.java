package controllers.auth;

import database.BriventoryDB;
import models.User;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/** {@code SessionHelper} provides various methods to store or retrieves values from a {@link Http.Session} instance. */
@Singleton
public final class SessionHelper {

  /** The key into the {@link Http.Session} corresponding to the id of the user. */
  private static final String USER_ID_KEY = "iduser";

  /** The injected {@link BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;

  /**
   * Creates a new instance of {@link SessionHelper} by injecting the necessary parameters.
   *
   * @param briventoryDB the {@link BriventoryDB}.
   */
  @Inject
  private SessionHelper(final BriventoryDB briventoryDB) { this.briventoryDB = briventoryDB; }

  /**
   * Adds the {@link User} id into the session.
   *
   * @param user the {@link User} to get the id.
   * @param request the {@link Http.Request} containing the {@link Http.Session} to modify.
   *
   * @return the modified {@link Http.Session} instance.
   */
  public Http.Session withUser(final User user, final Http.Request request) {
    return request.session().adding(USER_ID_KEY, user.getId().toString());
  }

  public Optional<String> retrieveEmail(final Http.Request request) {
    Optional<String> userIdValue = request.session().get(USER_ID_KEY);
    if (userIdValue.isEmpty()) return Optional.empty();

    try {
      final long userId = Long.parseLong(userIdValue.get());
      final User user = briventoryDB.query(
          session -> User.findFromId(session, userId)
      ).join();
      return Optional.of(user.getEmail());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

}

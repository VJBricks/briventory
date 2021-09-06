package controllers.auth;

import models.User;
import play.mvc.Http;
import repositories.UsersRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/** {@code SessionHelper} provides various methods to store or retrieves values from a {@link Http.Session} instance. */
@Singleton
public final class SessionHelper {

  /** The key into the {@link Http.Session} corresponding to the id of the user. */
  private static final String USER_ID_KEY = "iduser";

  /** The injected {@link UsersRepository} instance. */
  private final UsersRepository usersRepository;

  /**
   * Creates a new instance of {@link SessionHelper} by injecting the necessary parameters.
   *
   * @param usersRepository the {@link UsersRepository} instance.
   */
  @Inject
  private SessionHelper(final UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

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

  /**
   * Retrieves the {@link User} instance from the session.
   *
   * @param request the {@link Http.Request} holding the session.
   *
   * @return an {@link Optional} instance containing the {@link User} instance or an empty one if the session does not
   * contains a valid user.
   */
  public Optional<User> retrieveUser(final Http.Request request) {
    Optional<String> userIdValue = request.session().get(USER_ID_KEY);
    if (userIdValue.isEmpty()) return Optional.empty();

    try {
      final var userId = Long.parseLong(userIdValue.get());
      return usersRepository.findById(userId);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Retrieves the e-mail address from the {@link User} stored into the session.
   *
   * @param request the {@link Http.Request} holding the sessio.
   *
   * @return an {@link Optional} instance containing the e-mail address or an empty one if the session does not contains
   * a valid user.
   */
  public Optional<String> retrieveEmail(final Http.Request request) {
    Optional<String> userIdValue = request.session().get(USER_ID_KEY);
    if (userIdValue.isEmpty()) return Optional.empty();

    try {
      final var userId = Long.parseLong(userIdValue.get());
      final Optional<User> user = usersRepository.findById(userId);
      return user.map(User::getEmail);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

}

package controllers.auth;

import models.Account;
import play.mvc.Http;
import repositories.AccountsRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/** {@code SessionHelper} provides various methods to store or retrieves values from a {@link Http.Session} instance. */
@Singleton
public final class SessionHelper {

  /** The key into the {@link Http.Session} corresponding to the id of the user. */
  private static final String USER_ID_KEY = "iduser";

  /** The injected {@link AccountsRepository} instance. */
  private final AccountsRepository accountsRepository;

  /**
   * Creates a new instance of {@link SessionHelper} by injecting the necessary parameters.
   *
   * @param accountsRepository the {@link AccountsRepository} instance.
   */
  @Inject
  private SessionHelper(final AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  /**
   * Adds the {@link Account} id into the session.
   *
   * @param account the {@link Account} to get the id.
   * @param request the {@link Http.Request} containing the {@link Http.Session} to modify.
   *
   * @return the modified {@link Http.Session} instance.
   */
  public Http.Session withAccount(final Account account, final Http.Request request) {
    return request.session().adding(USER_ID_KEY, account.getId().toString());
  }

  /**
   * Retrieves the {@link Account} instance from the session.
   *
   * @param request the {@link Http.Request} holding the session.
   *
   * @return an {@link Optional} instance containing the {@link Account} instance or an empty one if the session does
   * not contain a valid user.
   */
  public Optional<Account> retrieveAccount(final Http.Request request) {
    Optional<String> userIdValue = request.session().get(USER_ID_KEY);
    if (userIdValue.isEmpty()) return Optional.empty();

    try {
      final var userId = Long.parseLong(userIdValue.get());
      return accountsRepository.findById(userId);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Retrieves the e-mail address from the {@link Account} stored into the session.
   *
   * @param request the {@link Http.Request} holding the session.
   *
   * @return an {@link Optional} instance containing the e-mail address or an empty one if the session does not contains
   * a valid user.
   */
  public Optional<String> retrieveEmail(final Http.Request request) {
    Optional<String> userIdValue = request.session().get(USER_ID_KEY);
    if (userIdValue.isEmpty()) return Optional.empty();

    try {
      final var userId = Long.parseLong(userIdValue.get());
      final Optional<Account> accountOptional = accountsRepository.findById(userId);
      return accountOptional.map(Account::getEmail);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

}

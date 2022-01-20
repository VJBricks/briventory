package models;

import repositories.AccountsRepository;

/**
 * The {@code BricksetTokens} class is the representation of the table {@code brickset_tokens} in the
 * <em>Briventory</em> database.
 */
public final class BricksetTokens extends Entity<AccountsRepository> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************

  /** The foreign key identifier to the {@link Account}. */
  private long idAccount;
  /** The API key. */
  private String apiKey;
  /** The username. */
  private String username;
  /** The password. */
  private String password;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link BricksetTokens}.
   *
   * @param accountsRepository the {@link AccountsRepository}.
   */
  public BricksetTokens(final AccountsRepository accountsRepository) {
    super(accountsRepository);
  }

  // *******************************************************************************************************************
  // Getters and Setters
  // *******************************************************************************************************************

  /**
   * Sets the identifier of the {@link Account}.
   *
   * @param idAccount the identifier of the {@link Account}.
   *
   * @return the current instance.
   */
  public BricksetTokens setIdAccount(final long idAccount) {
    this.idAccount = idAccount;
    return this;
  }

  /** @return the API key. */
  public String getApiKey() { return apiKey; }

  /**
   * Sets the API key.
   *
   * @param apiKey the API key.
   *
   * @return the current instance.
   */
  public BricksetTokens setApiKey(final String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  /** @return the username. */
  public String getUsername() { return username; }

  /**
   * Sets the username.
   *
   * @param username the username.
   *
   * @return the current instance.
   */
  public BricksetTokens setUsername(final String username) {
    this.username = username;
    return this;
  }

  /** @return the password. */
  public String getPassword() { return password; }

  /**
   * Sets the password.
   *
   * @param password the password.
   *
   * @return the current instance.
   */
  public BricksetTokens setPassword(final String password) {
    this.password = password;
    return this;
  }

}

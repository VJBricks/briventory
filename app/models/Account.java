package models;

import at.favre.lib.crypto.bcrypt.BCrypt;
import repositories.AccountsRepository;

import java.util.Objects;
import java.util.Optional;

/** The {@code Account} class is the representation of the table {@code account} in the <em>Briventory</em> database. */
public final class Account extends Entity<AccountsRepository> {

  // *******************************************************************************************************************
  // Constants
  // *******************************************************************************************************************
  /** The cost for BCrypt. */
  private static final int BCRYPT_COST = 13;

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The database identifier. */
  private Long id;
  /** The foreign key identifier of the {@link ColorsSource}. */
  private Long idColorSource;
  /** The first name. */
  private String firstname;
  /** The last name. */
  private String lastname;
  /** The e-mail address. */
  private String email;
  /** The password. */
  private String password;

  /** Does this account has administrator rights ? */
  private Boolean isAdministrator;
  /** Does this account is locked. */
  private Boolean isLocked;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Builds an empty {@link Account} instance.
   *
   * @param accountRepository the instance of {@link AccountsRepository}.
   */
  public Account(final AccountsRepository accountRepository) { super(accountRepository); }

  // *******************************************************************************************************************
  // Object Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("%s %s <%s> (administrator = %s, locked = %s)",
                         getFirstname(),
                         getLastname(),
                         getEmail(),
                         isAdministrator(),
                         isLocked());
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(getId(),
                        getEmail(),
                        getFirstname(),
                        getLastname(),
                        getPassword(),
                        getIdColorSource());
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object obj) {
    if (obj == null) return false;
    if (!getClass().equals(obj.getClass())) return false;
    final var account = (Account) obj;
    if (id == null && account.id != null) return false;
    if (id != null && account.id == null) return false;
    if (id != null) return id.equals(account.id);
    return firstname.equals(account.firstname) &&
           lastname.equals(account.lastname) &&
           email.equals(account.email);
  }

  // *******************************************************************************************************************
  // Getters and Setters
  // *******************************************************************************************************************

  /**
   * Sets the identifier.
   *
   * @param id the identifier.
   *
   * @return this instance.
   */
  public Account setId(final Long id) {
    this.id = id;
    return this;
  }

  /** @return the identifier. */
  public Long getId() { return id; }

  /**
   * Sets the {@link ColorsSource}.
   *
   * @param idColorSource the identifier of the {@link ColorsSource}.
   *
   * @return this instance.
   */
  public Account setIdColorSource(final Long idColorSource) {
    this.idColorSource = idColorSource;
    return this;
  }

  /**
   * Sets the {@link ColorsSource}.
   *
   * @param colorSource the {@link ColorsSource} instance.
   *
   * @return this instance.
   */
  public Account setColorSource(final ColorsSource colorSource) {
    setIdColorSource(colorSource.getId());
    return this;
  }

  /** @return the identifier of the {@link ColorsSource}. */
  public Long getIdColorSource() { return idColorSource; }

  /** @return the {@link ColorsSource} instance. */
  public Optional<ColorsSource> getColorsSource() {
    return Optional.ofNullable(getRepository().getColorSourcesRepository().findById(idColorSource));
  }

  /**
   * Sets the first name.
   *
   * @param firstname the first name.
   *
   * @return this instance.
   */
  public Account setFirstname(final String firstname) {
    this.firstname = firstname;
    return this;
  }

  /** @return the first name. */
  public String getFirstname() { return firstname; }

  /**
   * Sets the last name.
   *
   * @param lastname the last name.
   *
   * @return this instance.
   */
  public Account setLastname(final String lastname) {
    this.lastname = lastname;
    return this;
  }

  /** @return the last name. */
  public String getLastname() { return lastname; }

  /**
   * Sets the e-mail address.
   *
   * @param email the e-mail address.
   *
   * @return this instance.
   */
  public Account setEmail(final String email) {
    this.email = email;
    return this;
  }

  /** @return the e-mail address. */
  public String getEmail() { return email; }

  /**
   * Sets the hashed password.
   *
   * @param password the hashed password.
   *
   * @return this instance.
   */
  public Account setPassword(final String password) {
    this.password = password;
    return this;
  }

  /**
   * Sets the password using the clear text. The hash will be performed before setting the password to this {@link
   * Account}.
   *
   * @param clearPassword the password as clear text.
   *
   * @return this {@link Account}.
   */
  public Account setClearPassword(final String clearPassword) {
    if (clearPassword != null && !clearPassword.isBlank())
      setPassword(BCrypt.withDefaults().hashToString(BCRYPT_COST, clearPassword.toCharArray()));
    return this;
  }

  /** @return the hashed password. */
  public String getPassword() { return password; }

  // *******************************************************************************************************************
  // Locked Uses Matter
  // *******************************************************************************************************************

  /** @return {@code true} if this user is locked, otherwise {@code false}. */
  public boolean isLocked() {
    if (isLocked == null)
      isLocked = getRepository().isLocked(this);
    return isLocked;
  }

  /**
   * @return an {@link Optional} instance to determine if the value has been fetch ({@code true} or {@code false}) or is
   * not present if it has not been fetch.
   */
  public Optional<Boolean> isLockedOptional() { return Optional.ofNullable(isLocked); }

  /**
   * Sets the locked status for this account.
   *
   * @param isLocked {@code true} to lock this account, {@code false} to unlock.
   *
   * @return this instance.
   */
  public Account setLocked(final boolean isLocked) {
    this.isLocked = isLocked;
    return this;
  }

  // *******************************************************************************************************************
  // Administrators Matter
  // *******************************************************************************************************************

  /** @return {@code true} if this user is an administrator, otherwise {@code false}. */
  public boolean isAdministrator() {
    if (isAdministrator == null)
      isAdministrator = getRepository().isAdministrator(this);
    return isAdministrator;
  }

  /**
   * @return an {@link Optional} instance to determine if the value has been fetch ({@code true} or {@code false}) or is
   * not present if it has not been fetch.
   */
  public Optional<Boolean> isAdministratorOptional() {
    return Optional.ofNullable(isAdministrator);
  }

  /**
   * Add or remove the administration rights for this {@link Account}.
   *
   * @param isAdministrator {@code true} to add administration rights, {@code false} to remove.
   *
   * @return this instance.
   */
  public Account setAdministrator(final boolean isAdministrator) {
    this.isAdministrator = isAdministrator;
    return this;
  }

}

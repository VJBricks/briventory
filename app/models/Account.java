package models;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jooq.tables.records.AccountRecord;
import org.apache.commons.validator.routines.EmailValidator;
import org.jooq.DSLContext;
import orm.LazyLoader;
import orm.Mapper;
import orm.Model;
import orm.RepositoriesHandler;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;
import repositories.AccountsRepository;
import repositories.ColorSourcesRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static jooq.Tables.ACCOUNT;

/** The {@code Account} class is the representation of the table {@code account} in the <em>Briventory</em> database. */
public final class Account extends Model implements PersistableModel1<AccountRecord>,
                                                        ValidatableModel<ValidationError> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /** the {@link Mapper} that will create an instance of {@link Account} from an instance of {@link AccountRecord}. */
  public static final Mapper<AccountRecord, Account> ACCOUNT_MAPPER =
      accountRecord -> new Account(accountRecord.getId(),
                                   accountRecord.getIdColorSource(),
                                   accountRecord.getFirstname(),
                                   accountRecord.getLastname(),
                                   accountRecord.getEmail(),
                                   accountRecord.getPassword());

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
  private final LazyLoader<Long, ColorsSource> colorsSourceLazyLoader =
      new LazyLoader<>(idColorSource -> RepositoriesHandler.of(ColorSourcesRepository.class)
                                                           .findById(idColorSource));
  /** The first name. */
  private String firstname;
  /** The last name. */
  private String lastname;
  /** The e-mail address. */
  private String email;
  /**
   * The password.
   *
   * @deprecated It is maybe a bad idea to keep the hashed password in the memory !
   */
  @Deprecated(forRemoval = false)
  private String password;

  /** Does this account has administrator rights ? */
  private final LazyLoader<Account, Boolean> administratorLazyLoader =
      new LazyLoader<>(account -> RepositoriesHandler.of(AccountsRepository.class)
                                                     .isAdministrator(account));
  /** Does this account is locked. */
  private final LazyLoader<Account, Boolean> isLockedLazyLoader =
      new LazyLoader<>(account -> RepositoriesHandler.of(AccountsRepository.class)
                                                     .isLocked(account));

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /** Creates an empty {@link Account} instance. */
  public Account() { /* No-op. */ }

  /**
   * Creates a new {@link Account} instance.
   *
   * @param idColorSource the identifier of the {@link ColorsSource}. It can be {@code null} if this account has no
   * default color source attached to it.
   * @param firstname the first name.
   * @param lastname the last name.
   * @param email the email address.
   * @param password the hashed password.
   */
  public Account(final Long idColorSource, final String firstname, final String lastname, final String email,
                 final String password) {
    colorsSourceLazyLoader.setKey(idColorSource);
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
  }

  /**
   * Creates a new {@link Account} instance.
   *
   * @param id the identifier.
   * @param idColorSource the identifier of the {@link ColorsSource}. It can be {@code null} if this account has no
   * default color source attached to it.
   * @param firstname the first name.
   * @param lastname the last name.
   * @param email the email address.
   * @param password the hashed password.
   */
  private Account(final Long id, final Long idColorSource, final String firstname, final String lastname,
                  final String email, final String password) {
    this(idColorSource, firstname, lastname, email, password);
    this.id = id;
  }

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
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  @Override
  public AccountRecord getUpdatableRecord(final DSLContext dslContext) {
    final AccountRecord accountRecord = dslContext.newRecord(ACCOUNT);
    return accountRecord.setId(id)
                        .setFirstname(firstname)
                        .setLastname(lastname)
                        .setEmail(email)
                        .setIdColorSource(colorsSourceLazyLoader.getKey());
  }

  @Override
  public void lastRefresh(final AccountRecord accountRecord) { id = accountRecord.getId(); }

  // *******************************************************************************************************************
  // IValidatableModel Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> errors(final DSLContext dslContext) {
    List<ValidationError> errors = new LinkedList<>();
    if (firstname == null || firstname.isBlank())
      errors.add(new ValidationError("firstname", "account.error.firstname.empty"));
    if (lastname == null || lastname.isBlank())
      errors.add(new ValidationError("lastname", "account.error.lastname.empty"));
    if (password == null || password.isBlank())
      errors.add(new ValidationError("password", "account.error.password.empty"));
    if (email == null || email.isBlank())
      errors.add(new ValidationError("email", "account.error.email.empty"));
    if (!EmailValidator.getInstance().isValid(email))
      errors.add(new ValidationError("email", "account.error.email.invalid"));
    return errors;
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
    colorsSourceLazyLoader.setKey(idColorSource);
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
    colorsSourceLazyLoader.setKey(colorSource.getId());
    colorsSourceLazyLoader.setValue(colorSource);
    return this;
  }

  /** @return the identifier of the {@link ColorsSource}. */
  public Long getIdColorSource() { return colorsSourceLazyLoader.getKey(); }

  /** @return the {@link ColorsSource} instance. */
  public Optional<ColorsSource> getColorsSource() {
    return Optional.ofNullable(colorsSourceLazyLoader.getValue());
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
   * Sets the password using the clear text. The hash will be performed before setting the password to this
   * {@link Account}.
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
    final var isLocked = isLockedLazyLoader.getValue();
    if (isLocked != null) return isLocked;
    return false;
  }

  /**
   * @return an {@link Optional} instance to determine if the value has been fetch ({@code true} or {@code false}) or is
   * not present if it has not been fetch.
   */
  public Optional<Boolean> isLockedOptional() { return Optional.ofNullable(isLockedLazyLoader.getValue()); }

  /**
   * Sets the locked status for this account.
   *
   * @param isLocked {@code true} to lock this account, {@code false} to unlock.
   *
   * @return this instance.
   */
  public Account setLocked(final boolean isLocked) {
    isLockedLazyLoader.setValue(isLocked);
    return this;
  }

  // *******************************************************************************************************************
  // Administrators Matter
  // *******************************************************************************************************************

  /** @return {@code true} if this user is an administrator, otherwise {@code false}. */
  public boolean isAdministrator() {
    final var isAdministrator = administratorLazyLoader.getValue();
    if (isAdministrator != null) return isAdministrator;
    return false;
  }

  /**
   * @return an {@link Optional} instance to determine if the value has been fetch ({@code true} or {@code false}) or is
   * not present if it has not been fetch.
   */
  public Optional<Boolean> isAdministratorOptional() {
    return Optional.ofNullable(administratorLazyLoader.getValue());
  }

  /**
   * Add or remove the administration rights for this {@link Account}.
   *
   * @param isAdministrator {@code true} to add administration rights, {@code false} to remove.
   *
   * @return this instance.
   */
  public Account setAdministrator(final boolean isAdministrator) {
    administratorLazyLoader.setValue(isAdministrator);
    return this;
  }

}

package models;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.Constraints;
import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static database.BriventoryDB.CACHE_REGION;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.JOINED;
import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Cacheable
@Cache(usage = READ_WRITE, region = "briventoryCache")
@Table(name = "user", schema = "public")
@Inheritance(strategy = JOINED)
public class User {

  // *******************************************************************************************************************
  // Constants
  // *******************************************************************************************************************
  /** The cost for BCrypt. */
  private static final int BCRYPT_COST = 13;

  // *******************************************************************************************************************
  // Attributes and Database Schema Definition
  // *******************************************************************************************************************
  /** The id of this {@link User}. */
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  /** The e-mail address. */
  @Column(name = "email", nullable = false)
  @Email
  @NotBlank
  private String email;

  /** The name. */
  @Column(name = "name", nullable = false)
  @Length(max = Constraints.NAME_DOMAIN_LENGTH)
  @NotBlank
  private String name;

  /** The password. */
  @Column(name = "password", nullable = false)
  @Length(min = Constraints.PASSWORD_MIN_LENGTH, max = Constraints.PASSWORD_DOMAIN_LENGTH)
  @NotBlank
  private String password;

  /** The default {@link ColorSource}. */
  @ManyToOne(fetch = FetchType.LAZY)
  @Cache(usage = NONSTRICT_READ_WRITE, region = CACHE_REGION)
  @JoinColumn(name = "idcolorsource")
  private ColorSource defaultColorSource;

  /** The state that this {@link User} is locked. */
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
  private LockedUser lockedUser;

  /** The state that this {@link User} is locked. */
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
  private Administrator administrator;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /** Builds an empty {@link User} instance. */
  public User() { /* No-op constructor */}

  // *******************************************************************************************************************
  // Object Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("%s - %s <%s> (admin = %s, locked = %s)", getClass().getName(), name, email, isAdministrator(),
                         isLocked());
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(id, email, name, password, defaultColorSource, lockedUser);
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object obj) {
    if (obj == null) return false;
    if (!getClass().equals(obj.getClass())) return false;
    final var user = (User) obj;
    if (id == null && user.id != null) return false;
    if (id != null && user.id == null) return false;
    if (id != null) return id.equals(user.id);
    return name.equals(user.name) &&
           email.equals(user.email);
  }

  // *******************************************************************************************************************
  // Getters and Setters
  // *******************************************************************************************************************

  /** @return the primary key of this {@link User}. */
  public Long getId() { return id; }

  /**
   * Sets the primary key.
   *
   * @param id the primary key value.
   */
  public void setId(final Long id) { this.id = id; }

  /** @return the e-mail address of this {@link User}. */
  public String getEmail() { return email; }

  /**
   * Sets the e-mail address.
   *
   * @param email the e-mail address.
   */
  public void setEmail(final String email) { this.email = email; }

  /** @return the name of this {@link User}. */
  public String getName() { return name; }

  /**
   * Sets the name.
   *
   * @param name the name.
   */
  public void setName(final String name) { this.name = name; }

  /** @return the password. */
  public String getPassword() { return password; }

  /**
   * Sets the password.
   *
   * @param password the password.
   */
  protected void setPassword(final String password) { this.password = password; }

  /**
   * Encrypt the password given, using {@link BCrypt}.
   *
   * @param clearPassword the clear password.
   */
  public void setClearPassword(final String clearPassword) {
    this.password = BCrypt.withDefaults().hashToString(BCRYPT_COST, clearPassword.toCharArray());
  }

  /** @return the user's default {@link ColorSource}. */
  public ColorSource getDefaultColorSource() {
    return defaultColorSource;
  }

  /**
   * Sets the default {@link ColorSource}.
   *
   * @param colorSource the {@link ColorSource} to be used the default one.
   */
  public void setDefaultColorSource(final ColorSource colorSource) { this.defaultColorSource = colorSource; }

  // *******************************************************************************************************************
  // Locked Uses Matter
  // *******************************************************************************************************************

  /** @return {@code true} if this user is locked, otherwise {@code false}. */
  public boolean isLocked() { return lockedUser != null; }

  /** Lock this {@link User}. */
  public void lock() {
    lockedUser = new LockedUser();
    lockedUser.setUser(this);
  }

  /** Unlock this {@link User}. */
  public void unlock() {
    lockedUser.setUser(null);
    lockedUser = null;
  }

  /** @return the {@link LockedUser} instance or {@code null} if this user is not locked. */
  public LockedUser getLockedUser() {
    return lockedUser;
  }

  // *******************************************************************************************************************
  // Administrators Matter
  // *******************************************************************************************************************

  /** @return {@code true} if this user is an administrator, otherwise {@code false}. */
  public boolean isAdministrator() { return administrator != null; }

  /**
   * Add or remove the administration rights for this {@link User}.
   *
   * @param isAdministrator {@code true} to add administration rights, {@code false} to remove.
   */
  public final void setAdministrator(final boolean isAdministrator) {
    if (isAdministrator && !isAdministrator()) {
      administrator = new Administrator();
      administrator.setUser(this);
    } else if (!isAdministrator) {
      administrator.setUser(null);
      administrator = null;
    }
  }

  /** @return the {@link Administrator} instance or {@code null} if this user is not an administrator. */
  public Administrator getAdministrator() {
    return administrator;
  }

}

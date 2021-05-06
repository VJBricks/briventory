package models;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.Constraints;
import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

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
@DiscriminatorOptions(insert = false)
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
  /**@OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
  )
  @JoinColumn(name = "id", referencedColumnName = "iduser")*/
  @Transient
  private LockedUser lockedUser;
  // *******************************************************************************************************************
  // Object Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("%s - %s <%s>", getClass().getName(), name, email);
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
  public void setDefaultColorSource(final ColorSource colorSource) {
    this.defaultColorSource = colorSource;
  }

  /** @return {@code true} is this user is locked, otherwise {@code false}. */
  public boolean isLocked() { return lockedUser == null; }

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

  // *******************************************************************************************************************
  // Data Retrieval
  // *******************************************************************************************************************

  /**
   * Retrives the {@link User} holding the id given.
   *
   * @param session the Hibernate {@link Session}.
   * @param id the id.
   *
   * @return the {@link User} instance or {@code null}.
   */
  public static User findFromId(final Session session, final long id) {
    return session.createQuery("select u from User u where u.id = :id", User.class)
                  .setParameter("id", id)
                  .setCacheMode(CacheMode.NORMAL)
                  .setCacheRegion(CACHE_REGION)
                  .setCacheable(true)
                  .getSingleResult();
  }

  /**
   * Retrieves all {@link User}s using the e-mail address given. Normally, e-mail addresses are unique, so a singleton
   * list or an empty one should be returned by this method.
   *
   * @param session the Hibernate {@link Session}.
   * @param email the e-mail address.
   *
   * @return a list of {@link User} instance.
   */
  public static List<User> findByEmail(final Session session, final String email) {
    return session.createQuery("select u from User u where u.email = :email", User.class)
                  .setParameter("email", email)
                  .setCacheMode(CacheMode.NORMAL)
                  .setCacheRegion(CACHE_REGION)
                  .setCacheable(true)
                  .getResultList();
  }

  public static List<User> getAll(final Session session) {
    return session.createQuery("select u from User u", User.class)
                  .setCacheMode(CacheMode.NORMAL)
                  .setCacheRegion(CACHE_REGION)
                  .setCacheable(true)
                  .getResultList();
  }

}

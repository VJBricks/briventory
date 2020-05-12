package models;

import org.hibernate.Session;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "user", schema = "public")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorOptions(insert = false)
public abstract class User {
  /** The id of this {@link User}. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /** The e-mail address. */
  @Column(name = "email", nullable = false)
  private String email;
  /** The name. */
  @Column(name = "name", nullable = false)
  private String name;
  /** The password. */
  @Column(name = "password", nullable = false)
  private String password;
  /** The default {@link ColorSource}. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idcolorsource")
  private ColorSource defaultColorSource;

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
  public void setPassword(final String password) { this.password = password; }

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
                  .getSingleResult();
  }

  /**
   * Retrives all {@link User}s using the e-mail address given. Normally, e-mail addresses are unique, so a singleton
   * list or an empty one should be returned by this method.
   *
   * @param session the Hibernate {@link Session}.
   * @param email the e-mail address.
   *
   * @return a list of {@link User} instance.
   */
  public static List<User> findFromEmail(final Session session, final String email) {
    return session.createQuery("select u from User u where u.email = :email", User.class)
                  .setParameter("email", email)
                  .getResultList();
  }

}

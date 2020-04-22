package ch.varani.briventory.models;

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

@Entity
@Table(name = "user", schema = "public")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User<T extends User<T>> {
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
   *
   * @return this instance to chain calls.
   */
  @SuppressWarnings("unchecked")
  public T setId(final Long id) {
    this.id = id;
    return (T) this;
  }

  /** @return the e-mail address of this {@link User}. */
  public String getEmail() { return email; }

  /**
   * Sets the e-mail address.
   *
   * @param email the e-mail address.
   *
   * @return this instance to chain calls.
   */
  @SuppressWarnings("unchecked")
  public T setEmail(final String email) {
    this.email = email;
    return (T) this;
  }

  /** @return the name of this {@link User}. */
  public String getName() { return name; }

  /**
   * Sets the name.
   *
   * @param name the name.
   *
   * @return this instance to chain calls.
   */
  @SuppressWarnings("unchecked")
  public T setName(final String name) {
    this.name = name;
    return (T) this;
  }

  /** @return the password. */
  public String getPassword() { return password; }

  /**
   * Sets the password.
   *
   * @param password the password.
   *
   * @return this instance to chain calls.
   */
  @SuppressWarnings("unchecked")
  public T setPassword(final String password) {
    this.password = password;
    return (T) this;
  }

  /** @return the user's default {@link ColorSource}. */
  public ColorSource getDefaultColorSource() {
    return defaultColorSource;
  }

}

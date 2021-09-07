package models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "briventoryCache")
@Table(name = "lockeduser", schema = "public")
public final class LockedUser {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************

  /** The id of the corresponding {@link User} entity. */
  @Id
  private Long idUser;

  /** The corresponding {@link User} instance. */
  @MapsId
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "iduser")
  private User user;

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the locked {@link User} instance. */
  User getUser() { return user; }

  /**
   * Sets the {@link User} that should be locked. Giving a {@code null} value unset the user.
   *
   * @param user the {@link User} instance.
   */
  void setUser(final User user) {
    this.user = user;
  }

}

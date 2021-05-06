package models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "briventoryCache")
@Table(name = "lockeduser", schema = "public")
public final class LockedUser {

  /** The primary and foreign key for this {@link LockedUser}. */
  /**
   * @Id
   * @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
   */
  @Id
  @Column(name = "iduser")
  private Long id;
  @Transient
  private User user;

  public User getUser() { return user; }

  public void setUser(final User user) { this.user = user; }

}

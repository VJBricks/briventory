package models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import play.Logger;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PostRemove;
import javax.persistence.PreRemove;
import javax.persistence.Table;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "briventoryCache")
@Table(name = "admin", schema = "public")
public final class Administrator implements PreDeleteEventListener {

  // *******************************************************************************************************************
  // Serialization Matter
  // *******************************************************************************************************************
  private static final long serialVersionUID = 2577920491731592939L;

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************

  /** The id of this {@link User}. */
  @Id
  private Long idUser;

  /** The corresponding {@link User} instance. */
  @MapsId
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "iduser")
  private User user;

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the primary key of this {@link Administrator}. */
  public Long getId() { return idUser; }

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

  // *******************************************************************************************************************
  // Persistence events
  // *******************************************************************************************************************
  @PreRemove
  private void preRemoveEvent() {
    Logger.of(Administrator.class).debug("PreRemove Event fired");
  }

  @PostRemove
  private void postRemoveEvent() {
    Logger.of(Administrator.class).debug("PostRemove Event fired");
  }

  @Override
  public boolean onPreDelete(final PreDeleteEvent event) {
    return false;
  }

}

package models;

import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import play.Logger;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.PostRemove;
import javax.persistence.PreRemove;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "admin", schema = "public")
@PrimaryKeyJoinColumn(name = "iduser", referencedColumnName = "id")
public final class Administrator extends User implements PreDeleteEventListener {

  /** The serial UID of this class. */
  private static final long serialVersionUID = 5047206730724369715L;

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

package models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "briventoryCache")
@Table(name = "revision", schema = "public")
@IdClass(value = Revision.class)
public final class Revision implements Serializable {

  /** The {@link Serializable} version UID. */
  private static final long serialVersionUID = -4908303767525733628L;

  /** The database revision. */
  @Id
  @Column(name = "database", nullable = false)
  private int database;

  /** @return the database revision. */
  public int getDatabase() { return database; }

  /**
   * Sets the database revision.
   *
   * @param database the database revision.
   */
  public void setDatabase(final int database) { this.database = database; }

}

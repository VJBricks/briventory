package ch.varani.briventory.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "revision", schema = "public")
public class Revision {

  /** The database revision. */
  @Id
  @Column(name = "database", nullable = false)
  private int database;

  /** @return the database revision. */
  public int getDatabase() { return database; }

}

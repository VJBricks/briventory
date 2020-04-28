package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.net.URI;

/** {@code ColorSource} represents an entity defining the color name and/or the color itself. */
@Entity
@Table(name = "colorsource", schema = "public")
@Inheritance(strategy = InheritanceType.JOINED)
public class ColorSource {

  /** The id of this {@link ColorSource}. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /** The name. */
  @Column(name = "name", nullable = false)
  private String name;
  /** The URI of this {@link ColorSource}. */
  @Column(name = "url", nullable = false)
  private URI url;

  /** @return the id of this {@link ColorSource}. */
  public Long getId() { return id; }

  /** @return the name of the source. */
  public String getName() { return name; }

  /** @return the {@link URI} for the source. */
  public URI getUrl() { return url; }

}

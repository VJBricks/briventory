package models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "briventoryCache")
@Table(name = "defaultcolorsource", schema = "public")
@PrimaryKeyJoinColumn(name = "idcolorsource")
public final class DefaultColorSource extends ColorSource implements Serializable {
  private static final long serialVersionUID = 6025088442254701054L;
}

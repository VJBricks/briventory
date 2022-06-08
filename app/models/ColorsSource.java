package models;

import jooq.tables.records.ColorSourceRecord;
import org.jooq.DSLContext;
import orm.Model;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;
import play.data.validation.ValidationError;

import java.util.LinkedList;
import java.util.List;

import static jooq.Tables.COLOR_SOURCE;

/**
 * The {@code ColorsSource} class is the representation of the table {@code colors_source} in the <em>Briventory</em>
 * database.
 */
public final class ColorsSource extends Model implements PersistableModel1<ColorSourceRecord>,
                                                             ValidatableModel<ValidationError> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The id of this {@link ColorsSource}. */
  private Long id;
  /** The name. */
  private String name;
  /** The URI of this {@link ColorsSource}. */
  private String url;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  public ColorsSource(final long id, final String name, final String url) {
    this.id = id;
    this.name = name;
    this.url = url;
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  @Override
  public ColorSourceRecord createRecord1(final DSLContext dslContext) {
    final ColorSourceRecord colorSourceRecord = dslContext.newRecord(COLOR_SOURCE);
    return colorSourceRecord.setId(id)
                            .setName(name)
                            .setUrl(url);
  }

  public void refresh1(final ColorSourceRecord colorSourceRecord) { id = colorSourceRecord.getId(); }

  // *******************************************************************************************************************
  // IValidatableEntity Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> validate(final DSLContext dslContext) {
    List<ValidationError> errors = new LinkedList<>();
    if (name == null || name.isBlank())
      errors.add(new ValidationError("name", "colorsSource.error.name.empty"));
    if (url == null || url.isBlank())
      errors.add(new ValidationError("url", "colorsSource.error.url.empty"));
    return errors;
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return the id of this {@link ColorsSource}. */
  public Long getId() { return id; }

  /**
   * Sets the identifier.
   *
   * @param id the identifier.
   *
   * @return this instance.
   */
  ColorsSource setId(final long id) {
    this.id = id;
    return this;
  }

  /** @return the name of the source. */
  public String getName() { return name; }

  /**
   * Sets the name.
   *
   * @param name the name.
   *
   * @return this instance.
   */
  public ColorsSource setName(final String name) {
    this.name = name;
    return this;
  }

  /** @return the URL for the source. */
  public String getUrl() { return url; }

  /**
   * Sets the URL.
   *
   * @param url the URL.
   *
   * @return this instance.
   */
  public ColorsSource setUrl(final String url) {
    this.url = url;
    return this;
  }

}

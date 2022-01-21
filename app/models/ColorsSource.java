package models;

import orm.models.Entity;
import orm.models.IStorableEntity;
import play.data.validation.ValidationError;
import repositories.ColorSourcesRepository;

import java.util.LinkedList;
import java.util.List;

/**
 * The {@code ColorsSource} class is the representation of the table {@code colors_source} in the <em>Briventory</em>
 * database.
 */
public final class ColorsSource extends Entity<ColorSourcesRepository> implements IStorableEntity<ValidationError> {

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

  /**
   * Creates a new instance of {@link ColorsSource}.
   *
   * @param repository the {@link ColorSourcesRepository} instance.
   */
  public ColorsSource(final ColorSourcesRepository repository) {
    super(repository);
  }

  // *******************************************************************************************************************
  // Entity Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ValidationError> isValid() {
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
  public ColorsSource setId(final long id) {
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

package models;

import repositories.ColorSourcesRepository;

/**
 * The {@code ColorsSource} class is the representation of the table {@code colors_source} in the <em>Briventory</em>
 * database.
 */
public final class ColorsSource extends Entity<ColorSourcesRepository> {

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

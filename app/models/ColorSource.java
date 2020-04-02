package models;

import java.net.URI;

/** {@code ColorSource} represents an entity defining the color name and/or the color itself. */
public final class ColorSource {

  /** The id of this {@link ColorSource}. */
  private Long id;
  /** The name. */
  private String name;
  /** The URI of this {@code ColorSource}. */
  private URI url;
  /** If {@code true}, this {@link ColorSource} is the App-wise default one. */
  private boolean defaultSource;

}

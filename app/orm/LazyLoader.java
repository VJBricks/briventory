package orm;

import java.util.function.Function;

/**
 * The {@code LazyLoader} class handle the lazy loading of data. It is based on the concept of key / value, where the
 * key is the filter element and the value is the result of the data loading.
 * <p>A key can be an object representation of a primitive type or an instance of a class. The key will be passed as
 * parameter to the {@code fetcher}, a {@link Function} that will perform the data loading.</p>
 * <p>The fetch of a {@code null} value is handled by an internal state.</p>
 *
 * @param <K> the key.
 * @param <V> the value.
 */
public final class LazyLoader<K, V> {
  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The key. It will be pass as a parameter to the {@link LazyLoader#fetcher} {@link Function}. */
  private K key;
  /** The internal state that will determine if a fetch has occurred ({@code true}) or not ({@code false}). */
  private boolean needFetching = true;
  /**
   * The value. It will be {@code null} if no data fetch has been performed. But it also can be null on fetch, the
   * internal state, represented by {@link LazyLoader#needFetching}, need to be used to know if the {@code null} value
   * is a real value or not.
   */
  private V value;
  /** The {@link Function} that will perform the data fetch. */
  private final Function<K, V> fetcher;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link LazyLoader} instance.
   *
   * @param fetcher the {@link Function} that will perform the data fetch.
   */
  public LazyLoader(final Function<K, V> fetcher) {
    this.fetcher = fetcher;
  }

  /**
   * Creates a new {@link LazyLoader} instance.
   *
   * @param key the key.
   * @param fetcher the {@link Function} that will perform the data fetch.
   */
  public LazyLoader(final K key, final Function<K, V> fetcher) {
    this(fetcher);
    this.key = key;
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return {@code true} if the data fetch has been performed, otherwise {@code false}. */
  public boolean isFetched() { return !needFetching; }

  /** @return the key. */
  public K getKey() { return key; }

  /**
   * Sets the key.
   * <p>If the value of the key has changed, the value is reset.</p>
   *
   * @param key the key.
   */
  public void setKey(final K key) {
    if (this.key == null || !this.key.equals(key)) {
      this.value = null;
      needFetching = true;
      this.key = key;
    }
  }

  /**
   * Retrieves the value. If the data fetch has already been performed, the value is directly returned (acting as a
   * singleton value). Otherwise, the data fetch is performed and the value is returned after it.
   *
   * @return the value.
   */
  public V getValue() {
    if (key == null) return null;
    if (needFetching) {
      value = fetcher.apply(key);
      needFetching = false;
    }
    return value;
  }

  /**
   * Sets the value.
   * <p>When this method is called, the next call to {@link LazyLoader#getValue()} will return the value without
   * performing a data fetch.</p>
   *
   * @param value the value.
   */
  public void setValue(final V value) {
    this.value = value;
    needFetching = false;
  }

}

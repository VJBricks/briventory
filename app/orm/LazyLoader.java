package orm;

import org.jooq.DSLContext;
import org.jooq.Function3;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * The {@code LazyLoader} class handle the lazy loading of data. It is based on the concept of key / value, where the
 * key is the filter element and the value is the result of the data loading.
 * <p>A key can be an object representation of a primitive type or an instance of a class. The key will be passed as
 * parameter to the {@code fetcher}, a {@link BiFunction} that will perform the data loading.</p>
 * <p><strong>Note</strong>: if the key is {@code null}, no fetch will be performed and
 * {@link LazyLoader#getValue(DSLContext)} will always return {@code null}.</p>
 * <p>The fetch of a {@code null} value is handled by an internal state.</p>
 *
 * @param <K> the key.
 * @param <V> the value.
 */
public abstract class LazyLoader<K, V> {
  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link PersistenceContext}. */
  private final PersistenceContext persistenceContext;

  /** The key. It will be pass as a parameter to the {@link LazyLoader#fetcher} {@link BiFunction}. */
  private K key;
  /** The internal state that will determine if a fetch has occurred ({@code true}) or not ({@code false}). */
  private boolean fetched = false;
  /**
   * The value. It will be {@code null} if no data fetch has been performed. But it also can be null on fetch, the
   * internal state, represented by {@link LazyLoader#fetched}, need to be used to know if the {@code null} value is a
   * real value or not.
   */
  private V value;
  /** The {@link BiFunction} that will perform the data fetch. */
  private final BiFunction<DSLContext, K, V> fetcher;
  /**
   * The function that will produce the corresponding {@link Action} instances, depending on the key and the value.
   */
  private final Function3<DSLContext, K, V, List<Action>> actionsCreator;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link LazyLoader} instance.
   *
   * @param persistenceContext the {@link PersistenceContext}.
   * @param fetcher the {@link BiFunction} that will perform the data fetch.
   * @param actionsCreator the {@link Function3} that will return a {@link List} of {@link Action} instances, that will
   * be executed during the persistence process.
   */
  protected LazyLoader(final PersistenceContext persistenceContext,
                       final BiFunction<DSLContext, K, V> fetcher,
                       final Function3<DSLContext, K, V, List<Action>> actionsCreator) {
    this.persistenceContext = persistenceContext;
    this.fetcher = fetcher;
    this.actionsCreator = actionsCreator;
  }

  /**
   * Creates a new {@link LazyLoader} instance.
   *
   * @param persistenceContext the {@link PersistenceContext}.
   * @param key the key.
   * @param fetcher the {@link BiFunction} that will perform the data fetch.
   * @param actionsCreator the {@link Function3} that will return a {@link List} of {@link Action} instances, that will
   * be executed during the persistence process.
   */
  protected LazyLoader(final PersistenceContext persistenceContext,
                       final K key,
                       final BiFunction<DSLContext, K, V> fetcher,
                       final Function3<DSLContext, K, V, List<Action>> actionsCreator) {
    this(persistenceContext, fetcher, actionsCreator);
    this.key = key;
  }

  // *******************************************************************************************************************
  // ModelActions Matters
  // *******************************************************************************************************************

  /**
   * Creates the corresponding {@link Action} to be executed during the persistence or the deletion process.
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return a {@link List} of {@link Action} instances.
   */
  final List<Action> createActions(final DSLContext dslContext) {
    if (isFetched() && hasChanged(dslContext))
      return actionsCreator.apply(dslContext, getKey(), getValue());
    return Collections.emptyList();
  }

  // *******************************************************************************************************************
  // Getters & Setters
  // *******************************************************************************************************************

  /** @return {@code true} if the data fetch has been performed, otherwise {@code false}. */
  public final boolean isFetched() { return fetched; }

  /** @return the key. */
  public final K getKey() { return key; }

  /**
   * Sets the key.
   * <p>If the value of the key has changed, the value is reset.</p>
   *
   * @param key the key.
   */
  public final void setKey(final K key) {
    if (this.key == null || !this.key.equals(key)) {
      this.value = null;
      fetched = false;
      this.key = key;
    }
  }

  /**
   * Retrieves the value. If the data fetch has already been performed, the value is directly returned (acting as a
   * singleton value). Otherwise, the data fetch is performed and the value is returned after it.
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return the value.
   */
  public final V getValue(final DSLContext dslContext) {
    if (key == null) return null;
    if (!fetched) {
      value = fetcher.apply(dslContext, key);
      fetched = true;
    }
    return value;
  }

  /**
   * Retrieves the value. If the data fetch has already been performed, the value is directly returned (acting as a
   * singleton value). Otherwise, the data fetch is performed and the value is returned after it.
   *
   * @return the value.
   */
  public final V getValue() {
    return persistenceContext.produceInConnection(this::getValue);
  }

  /**
   * Sets the value.
   * <p>When this method is called, the next call to {@link LazyLoader#getValue(DSLContext)} will return the value
   * without performing a data fetch.</p>
   *
   * @param value the value.
   */
  public final void setValue(final V value) {
    this.value = value;
    fetched = true;
  }

  /**
   * Fetches the value from the database.
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return the fetched value.
   */
  protected final V fetchValue(final DSLContext dslContext) {
    return fetcher.apply(dslContext, key);
  }

  /**
   * @param dslContext the {@link DSLContext}.
   *
   * @return {@code false} if the actual value correspond to the fetched value or the value has not been fetched.
   * Otherwise, this method returns {@code true}.
   */
  public final boolean hasChanged(final DSLContext dslContext) {
    if (!fetched) return false;
    return !value.equals(fetchValue(dslContext));
  }

}

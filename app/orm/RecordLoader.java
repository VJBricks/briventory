package orm;

import org.jooq.DSLContext;
import org.jooq.Function3;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * The {@code RecordLoader} is a {@link orm.LazyLoader} that will handle data that is not represented by a model.
 *
 * @param <K> the key.
 * @param <V> the value.
 *
 * @see orm.DeleteRecordAction
 * @see orm.PersistRecordAction
 */
public final class RecordLoader<K, V> extends LazyLoader<K, V> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The function that will produce the corresponding {@link ModelAction}, depending on the key and the value. */
  private final Function3<DSLContext, K, V, ModelAction> modelActionCreator;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  RecordLoader(final PersistenceContext persistenceContext,
               final K key,
               final BiFunction<DSLContext, K, V> fetcher,
               final Function3<DSLContext, K, V, ModelAction> modelActionCreator) {
    super(persistenceContext, key, fetcher);
    this.modelActionCreator = modelActionCreator;
  }

  // *******************************************************************************************************************
  // LazyLoader Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ModelAction> createModelActions(final DSLContext dslContext) {
    if (isFetched() && hasChanged(dslContext))
      return Collections.singletonList(modelActionCreator.apply(dslContext, getKey(), getValue()));
    return Collections.emptyList();
  }

}

package orm;

import org.jooq.DSLContext;
import org.jooq.Function3;

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
  // Construction & Initialization
  // *******************************************************************************************************************
  RecordLoader(final PersistenceContext persistenceContext,
               final K key,
               final BiFunction<DSLContext, K, V> fetcher,
               final Function3<DSLContext, K, V, List<ModelAction>> modelActionsCreator) {
    super(persistenceContext, key, fetcher, modelActionsCreator);
  }

}

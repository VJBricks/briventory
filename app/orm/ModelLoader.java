package orm;

import org.jooq.DSLContext;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public final class ModelLoader<K, M extends Model> extends LazyLoader<K, M> {

  ModelLoader(final PersistenceContext persistenceContext, final BiFunction<DSLContext, K, M> fetcher) {
    super(persistenceContext, fetcher);
  }

  ModelLoader(final PersistenceContext persistenceContext, final K key, final BiFunction<DSLContext, K, M> fetcher) {
    super(persistenceContext, key, fetcher);
  }

  /** {@inheritDoc} */
  @Override
  public List<ModelAction> createModelActions() {
    //if (!isFetched())
    return Collections.emptyList();
    //return Collections.singletonList(actionCreator.apply(getKey(), getValue()));
  }

}

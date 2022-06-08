package orm;

import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class OptionalModelLoader<K, M extends Model> extends LazyLoader<K, Optional<M>> {
  OptionalModelLoader(final PersistenceContext persistenceContext,
                      final BiFunction<DSLContext, K, Optional<M>> fetcher) {
    super(persistenceContext, fetcher);
  }

  OptionalModelLoader(final PersistenceContext persistenceContext,
                      final K key,
                      final BiFunction<DSLContext, K, Optional<M>> fetcher) {
    super(persistenceContext, key, fetcher);
  }

  /**
   * @return
   */
  @Override
  public List<ModelAction> createModelActions() {
    return null;
  }

}

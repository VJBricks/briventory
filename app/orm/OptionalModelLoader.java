package orm;

import org.jooq.DSLContext;
import org.jooq.Function3;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class OptionalModelLoader<K, M extends Model> extends LazyLoader<K, Optional<M>> {
  OptionalModelLoader(final PersistenceContext persistenceContext,
                      final BiFunction<DSLContext, K, Optional<M>> fetcher,
                      final Function3<DSLContext, K, Optional<M>, List<Action>> actionsCreator) {
    super(persistenceContext, fetcher, actionsCreator);
  }

  OptionalModelLoader(final PersistenceContext persistenceContext,
                      final K key,
                      final BiFunction<DSLContext, K, Optional<M>> fetcher,
                      final Function3<DSLContext, K, Optional<M>, List<Action>> actionsCreator) {
    super(persistenceContext, key, fetcher, actionsCreator);
  }

}

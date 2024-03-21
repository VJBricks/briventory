package orm;

import org.jooq.DSLContext;
import org.jooq.Function3;

import java.util.List;
import java.util.function.BiFunction;

public final class ManyModelsLoader<K, M extends Model> extends LazyLoader<K, List<M>> {

  ManyModelsLoader(final PersistenceContext persistenceContext,
                   final K key,
                   final BiFunction<DSLContext, K, List<M>> fetcher,
                   final Function3<DSLContext, K, List<M>, List<Action>> actionsCreator) {
    super(persistenceContext, key, fetcher, actionsCreator);
  }

}

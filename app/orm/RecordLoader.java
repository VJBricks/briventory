package orm;

import org.jooq.DSLContext;
import org.jooq.Function3;
import org.jooq.Record;

import java.util.List;
import java.util.function.BiFunction;

public final class RecordLoader<K, V, R extends Record> extends LazyLoader<K, V> {

  private final Function3<DSLContext, K, V, R> recordCreator;

  RecordLoader(final PersistenceContext persistenceContext,
               final K key,
               final BiFunction<DSLContext, K, V> fetcher,
               final Function3<DSLContext, K, V, R> recordCreator) {
    super(persistenceContext, key, fetcher);
    this.recordCreator = recordCreator;
  }

  /** {@inheritDoc} */
  @Override
  public List<ModelAction> createModelActions() {
    return null;
  }

}

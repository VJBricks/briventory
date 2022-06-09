package orm;

import org.jooq.DSLContext;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

public final class ManyModelsLoader<K, M extends Model> extends LazyLoader<K, List<M>> {

  ManyModelsLoader(final PersistenceContext persistenceContext,
                   final K key,
                   final BiFunction<DSLContext, K, List<M>> fetcher) {
    super(persistenceContext, key, fetcher);
  }

  /** {@inheritDoc} */
  @Override
  public List<ModelAction> createModelActions(final DSLContext dslContext) {
    if (!isFetched()) return Collections.emptyList();

    final List<ModelAction> actions = new LinkedList<>();
    /*final List<M> dbModels = fetchValue();
    final List<M> values = getValue();
    actions.addAll(dbModels.stream().filter(m -> m instanceof DeletableModel && !values.contains(m))
                           .map(m -> new DeleteAction((DeletableModel<?, ?>) m)).toList());*/

    //actions.addAll(values.stream().)
    return actions;
  }

}

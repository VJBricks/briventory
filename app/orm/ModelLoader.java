package orm;

import org.jooq.DSLContext;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public final class ModelLoader<K, M extends Model> extends LazyLoader<K, M> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /**
   * The function that will produce the corresponding instances of {@link ModelAction}, depending on the key, the old
   * value and the new value.
   */
  /*private final Function4<DSLContext, K, M, M, List<ModelAction>> modelActionsCreator;*/

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  ModelLoader(final PersistenceContext persistenceContext,
              final BiFunction<DSLContext, K, M> fetcher/*,
              final Function4<DSLContext, K, M, M, List<ModelAction>> modelActionsCreator*/
  ) {
    super(persistenceContext, fetcher);
    /*this.modelActionsCreator = modelActionsCreator;*/
  }

  ModelLoader(final PersistenceContext persistenceContext,
              final K key,
              final BiFunction<DSLContext, K, M> fetcher/*,
              final Function4<DSLContext, K, M, M, List<ModelAction>> modelActionsCreator*/) {
    super(persistenceContext, key, fetcher);
    /*this.modelActionsCreator = modelActionsCreator;*/
  }

  // *******************************************************************************************************************
  // Lazy Loader Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  public List<ModelAction> createModelActions(final DSLContext dslContext) {
    /*if (isFetched() && hasChanged(dslContext))
      return modelActionsCreator.apply(dslContext, getKey(), fetchValue(dslContext), getValue());*/
    return Collections.emptyList();
  }

}

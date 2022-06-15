package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;

import java.util.Collections;
import java.util.List;

/**
 * {@code PersistAction1} is aims to handle the persistance of a {@link PersistableModel1}.
 *
 * @param <P> the specific type handled, extending {@link PersistableModel1} and {@link ValidatableModel}.
 * @param <R> the specific type handled, extending {@link UpdatableRecord}.
 * @param <V> the type of the validation errors.
 */
public final class PersistAction1<V, R extends UpdatableRecord<R>, P extends PersistableModel1<R> & ValidatableModel<V>>
    extends ModelAction {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link List} of instance of {@link P} that will be persisted. */
  private final List<P> persistableModels;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link orm.PersistAction1}.
   *
   * @param persistableModels the {@link List} of instances of {@link P} to persist.
   */
  public PersistAction1(final List<P> persistableModels) {
    this.persistableModels = persistableModels;
  }

  /**
   * Creates a new instance of {@link PersistAction1}.
   *
   * @param persistableModel the instance of {@link P} to persist.
   */
  public PersistAction1(final P persistableModel) {
    this.persistableModels = Collections.singletonList(persistableModel);
  }

  // *******************************************************************************************************************
  // ModelAction Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  void perform(final PersistenceContext persistenceContext, final DSLContext dslContext) {
    for (P persistableModel : persistableModels)
      persistenceContext.persist(dslContext, persistableModel);
  }

}

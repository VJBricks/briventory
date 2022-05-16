package orm;

import org.jooq.DSLContext;
import orm.models.PersistableModel;
import orm.models.ValidatableModel;

public abstract class ModelPersistor<V, P extends PersistableModel & ValidatableModel<V>> {

  /** The {@link PersistableModel} going to be persisted. */
  private final P persistableModel;

  /**
   * Creates a new {@link ModelPersistor}.
   *
   * @param persistableModel the {@link PersistableModel} going to be persisted.
   */
  ModelPersistor(final P persistableModel) {
    this.persistableModel = persistableModel;
  }

  /** @return the instance of {@link P}. */
  protected P getPersistableModel() { return persistableModel; }

  protected abstract void persistAndRefreshModel(DSLContext dslContext);

  void persist(final PersistenceContext persistenceContext, final DSLContext dslContext) {
    for (ModelAction modelAction : persistableModel.getPrePersistenceActions(dslContext))
      modelAction.perform(persistenceContext, dslContext);

    persistAndRefreshModel(dslContext);

    for (ModelAction modelAction : persistableModel.getPostPersistenceActions(dslContext))
      modelAction.perform(persistenceContext, dslContext);
  }

}

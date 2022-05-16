package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;

final class ModelPersistor1<V, R extends UpdatableRecord<R>,
                               P extends PersistableModel1<R> & ValidatableModel<V>>
    extends ModelPersistor<V, P> {

  /**
   * Creates a new {@link ModelPersistor}.
   *
   * @param persistableModel the {@link PersistableModel1} going to be persisted.
   */
  ModelPersistor1(final P persistableModel) { super(persistableModel); }

  @Override
  protected void persistAndRefreshModel(final DSLContext dslContext) {
    final R r = getPersistableModel().getUpdatableRecord(dslContext);
    r.merge();
    getPersistableModel().lastRefresh(r);
  }

}

package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel2;
import orm.models.ValidatableModel;

public final class ModelPersistor2<V, R1 extends UpdatableRecord<R1>,
                                      R2 extends UpdatableRecord<R2>,
                                      P extends PersistableModel2<R1, R2> & ValidatableModel<V>>
    extends ModelPersistor<V, P> {
  /**
   * Creates a new {@link ModelPersistor}.
   *
   * @param persistableModel the {@link P} going to be persisted.
   */
  ModelPersistor2(final P persistableModel) { super(persistableModel); }

  @Override
  protected void persistAndRefreshModel(final DSLContext dslContext) {
    final R1 r1 = getPersistableModel().getUpdatableRecord1(dslContext);
    r1.merge();
    final R2 r2 = getPersistableModel().getUpdatableRecord2(dslContext, r1);
    r2.merge();
    getPersistableModel().lastRefresh(r2);
  }

}

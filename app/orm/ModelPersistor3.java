package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel3;
import orm.models.ValidatableModel;

public final class ModelPersistor3<V, R1 extends UpdatableRecord<R1>,
                                      R2 extends UpdatableRecord<R2>,
                                      R3 extends UpdatableRecord<R3>,
                                      P extends PersistableModel3<R1, R2, R3> & ValidatableModel<V>>
    extends ModelPersistor<V, P> {
  /**
   * Creates a new {@link ModelPersistor}.
   *
   * @param persistableModel the {@link P} going to be persisted.
   */
  ModelPersistor3(final P persistableModel) {
    super(persistableModel);
  }

  @Override
  protected void persistAndRefreshModel(final DSLContext dslContext) {
    final R1 r1 = getPersistableModel().getUpdatableRecord1(dslContext);
    r1.merge();
    final R2 r2 = getPersistableModel().getUpdatableRecord2(dslContext, r1);
    r2.merge();
    final R3 r3 = getPersistableModel().getUpdatableRecord3(dslContext, r2);
    r3.merge();
    getPersistableModel().lastRefresh(r3);
  }

}

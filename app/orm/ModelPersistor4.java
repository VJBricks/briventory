package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel4;
import orm.models.ValidatableModel;

public final class ModelPersistor4<V, R1 extends UpdatableRecord<R1>,
                                      R2 extends UpdatableRecord<R2>,
                                      R3 extends UpdatableRecord<R3>,
                                      R4 extends UpdatableRecord<R4>,
                                      P extends PersistableModel4<R1, R2, R3, R4> & ValidatableModel<V>>
    extends ModelPersistor<V, P> {
  /**
   * Creates a new {@link ModelPersistor}.
   *
   * @param persistableModel the {@link P} going to be persisted.
   */
  ModelPersistor4(final P persistableModel) {
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
    final R4 r4 = getPersistableModel().getUpdatableRecord4(dslContext, r3);
    r4.merge();
    getPersistableModel().lastRefresh(r4);
  }

}

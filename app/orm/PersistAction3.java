package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel3;
import orm.models.ValidatableModel;

import java.util.Collections;
import java.util.List;

public final class PersistAction3<V, R1 extends UpdatableRecord<R1>,
                                     R2 extends UpdatableRecord<R2>,
                                     R3 extends UpdatableRecord<R3>,
                                     P extends PersistableModel3<R1, R2, R3> & ValidatableModel<V>>
    extends ModelAction {

  private final List<P> persistableModels;

  public PersistAction3(final List<P> persistableModels) {
    this.persistableModels = persistableModels;
  }

  public PersistAction3(final P persistableModel) {
    this.persistableModels = Collections.singletonList(persistableModel);
  }

  @Override
  void perform(final PersistenceContext persistenceContext, final DSLContext dslContext) {
    for (P persistableModel : persistableModels)
      persistenceContext.persist(dslContext, persistableModel);
  }

}

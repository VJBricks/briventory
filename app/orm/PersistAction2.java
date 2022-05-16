package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel2;
import orm.models.ValidatableModel;

import java.util.Collections;
import java.util.List;

public final class PersistAction2<V, R1 extends UpdatableRecord<R1>,
                                     R2 extends UpdatableRecord<R2>,
                                     P extends PersistableModel2<R1, R2> & ValidatableModel<V>>
    extends ModelAction {

  private final List<P> persistableModels;

  public PersistAction2(final List<P> persistableModels) {
    this.persistableModels = persistableModels;
  }

  public PersistAction2(final P persistableModel) {
    this.persistableModels = Collections.singletonList(persistableModel);
  }

  @Override
  void perform(final PersistenceContext persistenceContext, final DSLContext dslContext) {
    for (P persistableModel : persistableModels)
      persistenceContext.persist(dslContext, persistableModel);
  }

}

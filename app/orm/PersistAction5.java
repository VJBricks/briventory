package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel5;
import orm.models.ValidatableModel;

import java.util.Collections;
import java.util.List;

public final class PersistAction5<V, R1 extends UpdatableRecord<R1>,
                                     R2 extends UpdatableRecord<R2>,
                                     R3 extends UpdatableRecord<R3>,
                                     R4 extends UpdatableRecord<R4>,
                                     R5 extends UpdatableRecord<R5>,
                                     P extends PersistableModel5<R1, R2, R3, R4, R5> & ValidatableModel<V>>
    extends ModelAction {

  private final List<P> persistableModels;

  public PersistAction5(final List<P> persistableModels) {
    this.persistableModels = persistableModels;
  }

  public PersistAction5(final P persistableModel) {
    this.persistableModels = Collections.singletonList(persistableModel);
  }

  @Override
  void perform(final PersistenceContext persistenceContext, final DSLContext dslContext) {
    for (P persistableModel : persistableModels)
      persistenceContext.persist(dslContext, persistableModel);
  }

}

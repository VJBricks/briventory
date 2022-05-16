package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;

import java.util.Collections;
import java.util.List;

public final class PersistAction1<V, M extends Model, R extends UpdatableRecord<R>,
                                     P extends PersistableModel1<R> & ValidatableModel<V>>
    extends ModelAction {

  private final List<P> persistableModels;

  public PersistAction1(final List<P> persistableModels) {
    this.persistableModels = persistableModels;
  }

  public PersistAction1(final P persistableModel) {
    this.persistableModels = Collections.singletonList(persistableModel);
  }

  @Override
  void perform(final PersistenceContext persistenceContext, final DSLContext dslContext) {
    for (P persistableModel : persistableModels)
      persistenceContext.persist(dslContext, persistableModel);
  }

}

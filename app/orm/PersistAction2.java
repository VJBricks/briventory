package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel2;
import orm.models.ValidatableModel;

import java.util.Collections;
import java.util.List;

public final class PersistAction2<M extends Model,
    V,
    R1 extends UpdatableRecord<R1>,
    R2 extends UpdatableRecord<R2>,
    P extends PersistableModel2<M, R1, R2> & ValidatableModel<V>>
    extends ModelAction<M> {

  private final List<P> persistableModels;

  PersistAction2(final Repository<M> repository, final List<P> persistableModels) {
    super(repository);
    this.persistableModels = persistableModels;
  }

  PersistAction2(final Repository<M> repository, final P persistableModel) {
    super(repository);
    this.persistableModels = Collections.singletonList(persistableModel);
  }

  @Override
  void perform(final DSLContext dslContext) {
    for (P persistableModel : persistableModels)
      getRepository().persist(dslContext, persistableModel);
  }

}

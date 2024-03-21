package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel5;
import orm.models.ValidatableModel;

import java.util.Collections;
import java.util.List;

public final class PersistAction5<M extends Model,
    V,
    R1 extends UpdatableRecord<R1>,
    R2 extends UpdatableRecord<R2>,
    R3 extends UpdatableRecord<R3>,
    R4 extends UpdatableRecord<R4>,
    R5 extends UpdatableRecord<R5>,
    P extends PersistableModel5<M, R1, R2, R3, R4, R5> & ValidatableModel<V>>
    extends ModelAction<M> {

  private final List<P> persistableModels;

  PersistAction5(final Repository<M> repository, final List<P> persistableModels) {
    super(repository);
    this.persistableModels = persistableModels;
  }

  PersistAction5(final Repository<M> repository, final P persistableModel) {
    super(repository);
    this.persistableModels = Collections.singletonList(persistableModel);
  }

  @Override
  void perform(final DSLContext dslContext) {
    for (P persistableModel : persistableModels)
      getRepository().persist(dslContext, persistableModel);
  }

}

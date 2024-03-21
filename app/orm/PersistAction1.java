package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.PersistableModel1;
import orm.models.ValidatableModel;

import java.util.Collections;
import java.util.List;

/**
 * {@code PersistAction1} is aims to handle the persistance of a {@link PersistableModel1}.
 *
 * @param <M> the specific {@link Model}.
 * @param <P> the specific type handled, extending {@link PersistableModel1} and {@link ValidatableModel}.
 * @param <R> the specific type handled, extending {@link UpdatableRecord}.
 * @param <V> the type of the validation errors.
 */
public final class PersistAction1<M extends Model,
    V,
    R extends UpdatableRecord<R>,
    P extends PersistableModel1<M, R> & ValidatableModel<V>>
    extends ModelAction<M> {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link List} of instance of {@link P} that will be persisted. */
  private final List<P> persistableModels;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link orm.PersistAction1}.
   *
   * @param repository the corresponding {@link Repository} that will handle the persistence.
   * @param persistableModels the {@link List} of instances of {@link P} to persist.
   */
  public PersistAction1(final Repository<M> repository, final List<P> persistableModels) {
    super(repository);
    this.persistableModels = persistableModels;
  }

  /**
   * Creates a new instance of {@link PersistAction1}.
   *
   * @param repository the corresponding {@link Repository} that will handle the persistence.
   * @param persistableModel the instance of {@link P} to persist.
   */
  public PersistAction1(final Repository<M> repository, final P persistableModel) {
    super(repository);
    this.persistableModels = Collections.singletonList(persistableModel);
  }

  // *******************************************************************************************************************
  // ModelAction Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  void perform(final DSLContext dslContext) {
    for (P persistableModel : persistableModels)
      getRepository().persist(dslContext, persistableModel);
  }

}

package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.models.DeletableModel;

import java.util.Collections;
import java.util.List;

/**
 * A {@code DeleteAction} is a particular {@link ModelAction} that aims to delete all the {@link DeletableModel}
 * provided.
 *
 * @param <M> the specific {@link Model}.
 * @param <R> the precise subtype of {@link UpdatableRecord}.
 * @param <D> the precise subtype of {@link DeletableModel}.
 * @param <V> the type of the errors instances, produced during the validation of the model.
 */
public final class DeleteAction<M extends Model,
    V,
    R extends UpdatableRecord<R>,
    D extends DeletableModel<M, V, R>> extends ModelAction<M> {
  /** The {@link List} containing all {@link DeletableModel} instances. */
  private final List<D> deletableModels;

  /**
   * Creates a new {@link DeleteAction}.
   *
   * @param repository the corresponding {@link Repository} that will handle the deletion.
   * @param deletableModels the {@link List} of {@link DeletableModel} instances to delete.
   */
  public DeleteAction(final Repository<M> repository, final List<D> deletableModels) {
    super(repository);
    this.deletableModels = deletableModels;
  }

  /**
   * Creates a new {@link DeleteAction}.
   *
   * @param repository the corresponding {@link Repository} that will handle the deletion.
   * @param deletableModel the instance of {@link DeletableModel} to delete.
   */
  public DeleteAction(final Repository<M> repository, final D deletableModel) {
    super(repository);
    this.deletableModels = Collections.singletonList(deletableModel);
  }

  /**
   * Performs the deletion of all {@link DeletableModel} instances stored in {@link DeleteAction#deletableModels}.
   *
   * @param dslContext the {@link DSLContext}.
   */
  @Override
  public void perform(final DSLContext dslContext) {
    getRepository().deleteAllInTransaction(deletableModels);
  }

}

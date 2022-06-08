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
 * @param <R> the precise subtype of {@link UpdatableRecord}.
 * @param <D> the precise subtype of {@link DeletableModel}.
 * @param <V> the type of the errors instances, produced during the validation of the model.
 */
public final class DeleteAction<V, R extends UpdatableRecord<R>, D extends DeletableModel<V, R>> extends ModelAction {
  /** The {@link List} containing all {@link DeletableModel} instances. */
  private final List<D> deletableModels;

  /**
   * Creates a new {@link DeleteAction}.
   *
   * @param deletableModels the {@link List} of {@link DeletableModel} instances to delete.
   */
  public DeleteAction(final List<D> deletableModels) {
    this.deletableModels = deletableModels;
  }

  /**
   * Creates a new {@link DeleteAction}.
   *
   * @param deletableModel the instance of {@link DeletableModel} to delete.
   */
  public DeleteAction(final D deletableModel) {
    this.deletableModels = Collections.singletonList(deletableModel);
  }

  /**
   * Performs the deletion of all {@link DeletableModel} instances stored in {@link DeleteAction#deletableModels}.
   *
   * @param persistenceContext the {@link PersistenceContext}, needed to call the corresponding {@code persist} method.
   * @param dslContext the {@link DSLContext}.
   */
  @Override
  public void perform(final PersistenceContext persistenceContext, final DSLContext dslContext) {
    for (D deletableModel : deletableModels)
      persistenceContext.delete(dslContext, deletableModel);
  }

}

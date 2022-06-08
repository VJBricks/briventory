package orm.models;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import orm.ModelAction;

import java.util.Collections;
import java.util.List;

/**
 * A {@code DeletableModel} allows a model to be handled by the deletion process.
 *
 * @param <E> the type of the errors for the validation process.
 * @param <R> the specific subtype of {@link UpdatableRecord} that will be used to delete the model.
 */
public interface DeletableModel<E, R extends UpdatableRecord<R>> {

  /**
   * Validates the deletion process for this model and exports all errors found.
   * <p><strong>Note</strong>: the deletion process will be aborted if a non-empty list is returned.</p>
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return a {@link List} containing all {@link E} instances, representing errors.
   */
  default List<E> validateForDeletion(DSLContext dslContext) { return Collections.emptyList(); }

  /**
   * Returns the {@link R} that will be used to delete this model.
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return the {@link R} that will be used to delete this model.
   */
  R createDeletionRecord(DSLContext dslContext);

  /**
   * Returns all {@link ModelAction} instances that has to be performed before the deletion of this model.
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return a {@link List} containing the {@link ModelAction} instances to perform before the deletion of this model.
   */
  default List<ModelAction> getPreDeletionActions(final DSLContext dslContext) {
    return Collections.emptyList();
  }

  /**
   * Returns all {@link ModelAction} instances that has to be performed after the deletion of this model.
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return a {@link List} containing the {@link ModelAction} instances to perform after the deletion of this model.
   */
  default List<ModelAction> getPostDeletionActions(final DSLContext dslContext) {
    return Collections.emptyList();
  }

}

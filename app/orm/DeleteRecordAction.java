package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

/**
 * {@code DeleteRecordAction} is a particular {@link ModelAction} that will delete from the database an
 * {@link org.jooq.UpdatableRecord}. This action should be used when no model exist to perform the deletion.
 *
 * @param <R> the specific type handled, extending {@link UpdatableRecord}.
 */
public final class DeleteRecordAction<R extends UpdatableRecord<R>> extends ModelAction {

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /** The {@link UpdatableRecord} that will be deleted. */
  private final R updatableRecord;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link DeleteRecordAction}.
   *
   * @param updatableRecord the {@link R} to delete.
   */
  public DeleteRecordAction(final R updatableRecord) {
    this.updatableRecord = updatableRecord;
  }

  // *******************************************************************************************************************
  // ModelAction Overrides
  // *******************************************************************************************************************

  /** {@inheritDoc} */
  @Override
  void perform(final PersistenceContext persistenceContext, final DSLContext dslContext) {
    dslContext.executeDelete(updatableRecord);
  }

}

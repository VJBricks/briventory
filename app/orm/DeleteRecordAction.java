package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

public final class DeleteRecordAction<R extends UpdatableRecord<R>> extends ModelAction {

  private final R updatableRecord;

  public DeleteRecordAction(final R updatableRecord) {
    this.updatableRecord = updatableRecord;
  }

  /**
   * @param persistenceContext the {@link PersistenceContext}, needed to call the corresponding {@code persist} method.
   * @param dslContext the {@link DSLContext}.
   */
  @Override
  void perform(final PersistenceContext persistenceContext, final DSLContext dslContext) {
    updatableRecord.delete();
  }

}

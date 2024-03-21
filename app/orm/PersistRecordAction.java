package orm;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;

public final class PersistRecordAction<R extends UpdatableRecord<R>> extends Action {
  private final R updatableRecord;

  public PersistRecordAction(final R updatableRecord) {
    this.updatableRecord = updatableRecord;
  }

  /**
   * Performs the persistence.
   *
   * @param dslContext the {@link DSLContext}.
   */
  void perform(final DSLContext dslContext) {
    updatableRecord.merge();
  }

}

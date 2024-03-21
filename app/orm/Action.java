package orm;

import org.jooq.DSLContext;

public abstract class Action {
  /**
   * Performs the corresponding action.
   *
   * @param dslContext the {@link DSLContext}.
   */
  abstract void perform(DSLContext dslContext);

}

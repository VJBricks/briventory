package orm.repositories;

import org.jooq.DSLContext;

/**
 * An <em>entity action</em> means to execute a database operation on a specific entity. {@code EntityAction} is the
 * class that provides the structural needs to create specific actions.
 */
public interface EntityAction {

  /**
   * Executes the action.
   *
   * @param dslContext the {@link DSLContext}.
   */
  void execute(DSLContext dslContext);

}

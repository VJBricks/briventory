package orm.models;

import org.jooq.DSLContext;

import java.util.Collections;
import java.util.List;

/**
 * A {@link ValidatableModel} instance is validated before being stored into the database.
 *
 * @param <E> the type of the instances, representing errors, used to populate the {@link List} of
 * {@link ValidatableModel#validate(DSLContext)}.
 */
public interface ValidatableModel<E> {

  /**
   * Gets the error messages that concern this model. If there is no error, en empty list should be returned.
   *
   * @param dslContext teh {@link DSLContext}.
   *
   * @return a {@link List} containing the error messages or an empty list if this model is valid.
   */
  default List<E> validate(final DSLContext dslContext) {
    return Collections.emptyList();
  }

}

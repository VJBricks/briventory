package orm.models;

import org.jooq.DSLContext;
import orm.Action;
import orm.Model;

import java.util.Collections;
import java.util.List;

/**
 * A {@code PersistableModel} is a {@link orm.Model} that can be persisted into the database. Only
 * {@link PersistableModel} can be passed to {@code persist} methods of the class {@link orm.PersistenceContext}.
 * <p>This class is the base class for the sub-interfaces that specialized the amount of {@link org.jooq.Record} that
 * has be be used to persist the {@link orm.Model}.</p>
 *
 * @param <M> the specific {@link Model}.
 */
public interface PersistableModel<M extends Model> {

  /**
   * Creates all {@link orm.ModelAction} that will be performed before the persistence of the model.
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return a {@link List} of {@link Action} instances. By default, this method returns an empty list.
   */
  default List<Action> getPrePersistenceActions(final DSLContext dslContext) { return Collections.emptyList(); }

  /**
   * Creates all {@link Action} that will be performed after the persistence of the model.
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return a {@link List} of {@link Action} instances. By default, this method returns an empty list.
   */
  default List<Action> getPostPersistenceActions(
      final DSLContext dslContext) { return Collections.emptyList(); }

}

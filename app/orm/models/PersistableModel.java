package orm.models;

import org.jooq.DSLContext;
import orm.ModelAction;

import java.util.Collections;
import java.util.List;

/**
 * A {@code PersistableModel} is a {@link orm.Model} that can be persisted into the database. Only
 * {@link PersistableModel} can be passed to {@code persist} methods of the class {@link orm.PersistenceContext}.
 * <p>This class is the base class for the sub-interfaces that specialized the amount of {@link org.jooq.Record} that
 * has be be used to persist the {@link orm.Model}.</p>
 */
public interface PersistableModel {

  /**
   * Creates all {@link ModelAction} that will be performed before the persistence of the model.
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return a {@link List} of {@link ModelAction} instances. By default, this method returns an empty list.
   */
  default List<ModelAction> getPrePersistenceActions(final DSLContext dslContext) { return Collections.emptyList(); }

  /**
   * Creates all {@link ModelAction} that will be performed after the persistence of the model.
   *
   * @param dslContext the {@link DSLContext}.
   *
   * @return a {@link List} of {@link ModelAction} instances. By default, this method returns an empty list.
   */
  default List<ModelAction> getPostPersistenceActions(final DSLContext dslContext) { return Collections.emptyList(); }

}

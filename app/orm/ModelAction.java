package orm;

import org.jooq.DSLContext;

/**
 * {@code ModelAction} is the base class for every actions that will be performed before or after a persistence or
 * deletion.
 * <p><strong>Note</strong>: {@code ModelAction} is not an interface, to avoid any access to the
 * {@link ModelAction#perform(PersistenceContext, DSLContext)} method outside the package (<em>java:S1610</em>).</p>
 */
@SuppressWarnings("java:S1610")
public abstract class ModelAction {

  abstract void perform(PersistenceContext persistenceContext, DSLContext dslContext);

}

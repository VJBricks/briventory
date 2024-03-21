package orm;

/**
 * {@code ModelAction} is the base class for every actions that will be performed before or after a persistence or
 * deletion.
 * <p><strong>Note</strong>: {@code ModelAction} is not an interface, to avoid any access to the
 * {@link ModelAction#perform(Repository, org.jooq.DSLContext)} method outside the package (<em>java:S1610</em>).</p>
 *
 * @param <M> the specific {@link Model}.
 */
@SuppressWarnings("java:S1610")
public abstract class ModelAction<M extends Model> extends Action {


  private final Repository<M> repository;

  public ModelAction(final Repository<M> repository) {
    this.repository = repository;
  }

  protected final Repository<M> getRepository() { return repository; }

}

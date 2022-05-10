package orm;

import org.jooq.Record;

/**
 * A {@code Mapper} transform a {@link Record} into a {@link Model}.
 *
 * @param <R> the specific type of {@link Record}.
 * @param <M> the specific type of {@link Model}.
 */
public interface Mapper<R extends Record, M extends Model> {
  /**
   * Creates a {@link M} from a {@link R}.
   *
   * @param r the {@link R} to get the data.
   *
   * @return an instance of {@link M}.
   */
  M map(R r);

  /**
   * Refreshes an instance of {@link M} with the {@link R} provided.
   *
   * @param r the {@link R} to get the data.
   * @param m the {@link M} to refresh.
   */
  void refresh(R r, M m);

}

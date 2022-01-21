package orm.repositories;

import orm.models.Entity;
import org.jooq.Record;

/**
 * {@code EntityReloader} defines the structure to create converter between an {@link Entity} and its corresponding
 * {@link Record}.
 *
 * @param <E> the precise subtype of the {@link Entity}.
 * @param <R> the precise subtype of the {@link org.jooq.Record}.
 */
public interface EntityReloader<R extends Record, E extends Entity<? extends Repository>> {

  /**
   * Reloads the internal attributes of the entity, by getting the values from the corresponding {@link Record}.
   *
   * @param sourceRecord the {@link Record}, where to pick up the data.
   * @param entity the {@link Entity} to reload.
   */
  void reload(R sourceRecord, E entity);

}

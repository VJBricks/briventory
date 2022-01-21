package orm.models;

import java.util.List;

/**
 * A {@link IStorableEntity} instance can be stored in the database.
 *
 * @param <V> the type of the instances, used to populate the {@link List} of {@link IStorableEntity#isValid()}.
 */
public interface IStorableEntity<V> {

  /**
   * Validates the value of all attributes of this entity. This method is called during the storage process.
   *
   * @return a {@link List}> of {@link V} instances. If there is no error, an empty list should be returned.
   */
  List<V> isValid();

}

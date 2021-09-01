package repositories;

import database.BriventoryDB;
import database.BriventoryDBException;
import org.hibernate.Session;

import java.util.List;
import java.util.concurrent.CompletionException;

/**
 * Base class for all mutable entities.
 *
 * @param <E> the type of the mutable entity handled.
 */
public abstract class MutableRepository<E> extends ImmutableRepository<E> {

  /**
   * Creates a new instance of {@link MutableRepository}.
   *
   * @param briventoryDB the {@link BriventoryDB} instance.
   */
  protected MutableRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  /**
   * Persists the entity into the database (save or update).
   *
   * @param entity the entity to persist.
   */
  public final void persist(final E entity) {
    try {
      getBriventoryDB().persist(session -> {
        if (!shallPersist(session, entity))
          throw new BriventoryDBException(String.format("Persistence of entity %s is not allowed", entity));
        session.saveOrUpdate(entity);
        session.flush();
        return entity;
      }).join();
    } catch (CompletionException e) {
      throw new BriventoryDBException(String.format("Persistence of entity %s failed", entity), e.getCause());
    }
  }

  /**
   * Shall the given entity be persisted ? By default, this method returns {@code true}. The {@link Session} can be used
   * to perform checks in the database.
   * <p>If the entity cannot be persisted, a {@link BriventoryDBException} will be thrown during the persistence
   * process.</p>
   *
   * @param session the {@link Session}.
   * @param entity the {@link E} to persist (insert or update).
   *
   * @return {@code true} if the deletion succeeded, otherwise {@code false}.
   */
  protected boolean shallPersist(final Session session, final E entity) {
    return true;
  }

  /**
   * Deletes the entity from the database.
   *
   * @param entity the entity to delete.
   */
  public final void delete(final E entity) {
    try {
      getBriventoryDB().remove(session -> {
        if (!shallDelete(session, entity))
          throw new BriventoryDBException(String.format("Removal of entity %s is not allowed", entity));
        session.remove(session.contains(entity) ? entity : session.merge(entity));
        session.flush();
        return null;
      }).join();
    } catch (CompletionException e) {
      throw new BriventoryDBException(String.format("Removal of entity %s failed", entity), e.getCause());
    }
  }

  /**
   * Deletes the entities from the database.
   *
   * @param entities the {@link List} of entities to delete.
   */
  public final void delete(final List<E> entities) {
    try {
      getBriventoryDB().remove(session -> {
        for (E entity : entities) {
          if (!shallDelete(session, entity))
            throw new BriventoryDBException(String.format("Removal of entity %s is not allowed", entity));
          session.remove(session.contains(entity) ? entity : session.merge(entity));
          session.flush();
        }
        return null;
      }).join();
    } catch (CompletionException e) {
      throw new BriventoryDBException("Removal of entities failed", e.getCause());
    }
  }

  /**
   * Shall the given entity be deleted ? By default, this method returns {@code true}. The {@link Session} can be used
   * to perform checks in the database.
   * <p>If the entity cannot be deleted, a {@link BriventoryDBException} will be thrown during the deletion
   * process.</p>
   *
   * @param session the {@link Session}.
   * @param entity the {@link E} to delete.
   *
   * @return {@code true} if the deletion succeeded, otherwise {@code false}.
   */
  protected boolean shallDelete(final Session session, final E entity) {
    return true;
  }

}

package repositories;

import database.BriventoryDB;
import database.BriventoryDBException;
import org.hibernate.Session;

import java.util.List;
import java.util.concurrent.CompletionException;

public abstract class MutableRepository<E> extends ImmutableRepository<E> {

  protected MutableRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  public final void persist(final E entity) {
    try {
      getBriventoryDB().persist(session -> {
        if (!shallPersist(session, entity))
          throw new BriventoryDBException(String.format("Persistence of entity %s is not allowed", entity));
        session.saveOrUpdate(session.contains(entity) ? this : session.merge(entity));
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

  public final void delete(final E entity) {
    try {
      getBriventoryDB().remove(session -> {
        if (!shallDelete(session, entity))
          throw new BriventoryDBException(String.format("Removal of entity %s is not allowed", entity));
        session.remove(session.contains(entity) ? this : session.merge(entity));
        session.flush();
        return null;
      }).join();
    } catch (CompletionException e) {
      throw new BriventoryDBException(String.format("Removal of entity %s failed", entity), e.getCause());
    }
  }

  public final void delete(final List<E> entities) {
    try {
      getBriventoryDB().remove(session -> {
        for (E entity : entities) {
          if (!shallDelete(session, entity))
            throw new BriventoryDBException(String.format("Removal of entity %s is not allowed", entity));
          session.remove(session.contains(entity) ? this : session.merge(entity));
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

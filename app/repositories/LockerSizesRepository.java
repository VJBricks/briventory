package repositories;

import com.google.inject.Inject;
import database.BriventoryDB;
import models.LockerSize;
import org.jooq.DSLContext;
import orm.ModelLoader;
import orm.PersistAction1;
import orm.Repository;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

import static jooq.Tables.LOCKER;
import static jooq.tables.LockerSize.LOCKER_SIZE;
import static models.LockerSize.LOCKER_SIZE_MAPPER;

@Singleton
public final class LockerSizesRepository extends Repository<LockerSize> {

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  @Inject
  public LockerSizesRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // Lazy Loader Creation
  // *******************************************************************************************************************

  /** @return the {@link ModelLoader} to lazy load a {@link LockerSize} instance. */
  public ModelLoader<Long, LockerSize> createModelLoader() {
    return createModelLoader(this::findById,
                             (dslContext, idLocker, lockerSize) -> Collections.singletonList(
                                 new PersistAction1<>(this, lockerSize)));
  }

  // *******************************************************************************************************************
  // General Data Retrieval
  // *******************************************************************************************************************

  /**
   * find the {@link LockerSize} corresponding to the identifier given.
   *
   * @param dslContext the {@link DSLContext}.
   * @param id the identifier of the {@link LockerSize} to find.
   *
   * @return the instance of {@link LockerSize} corresponding to the identifier given, or {@code null} if the identifier
   * does not correspond to any container type.
   */
  private LockerSize findById(final DSLContext dslContext, final Long id) {
    return fetchOne(LOCKER_SIZE_MAPPER,
                    dslContext,
                    ctx -> ctx.selectFrom(LOCKER_SIZE)
                              .where(LOCKER_SIZE.ID.eq(id)));
  }

  /** @return all {@link LockerSize} instances that are stored in the database. */
  public List<LockerSize> getAll() {
    return fetch(LOCKER_SIZE_MAPPER, dslContext -> dslContext.selectFrom(LOCKER_SIZE));
  }

  /** @return all {@link LockerSize} instances that are not used by any {@link models.Locker}. */
  public List<LockerSize> getUnused() {
    return fetch(LOCKER_SIZE_MAPPER, dslContext ->
        dslContext.select(LOCKER_SIZE.asterisk())
                  .from(LOCKER_SIZE)
                  .leftJoin(LOCKER).on(LOCKER_SIZE.ID.eq(LOCKER.ID_LOCKER_SIZE))
                  .where(LOCKER.ID_LOCKER_SIZE.isNull())
                  .coerce(LOCKER_SIZE));
  }

}

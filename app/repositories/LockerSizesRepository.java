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
                                 new PersistAction1<>(lockerSize)));
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

}

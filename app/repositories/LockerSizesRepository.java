package repositories;

import com.google.inject.Inject;
import database.BriventoryDB;
import jooq.tables.records.LockerSizeRecord;
import models.LockerSize;
import org.jooq.DSLContext;
import orm.Mapper;
import orm.ModelLoader;
import orm.Repository;

import javax.inject.Singleton;

import static jooq.tables.LockerSize.LOCKER_SIZE;

@Singleton
public final class LockerSizesRepository extends Repository<LockerSize> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link LockerSize} from an instance of
   * {@link LockerSizeRecord}.
   */
  private static final Mapper<LockerSizeRecord, LockerSize> LOCKER_SIZE_MAPPER =
      lockerSizeRecord -> new LockerSize(lockerSizeRecord.getId(),
                                         lockerSizeRecord.getName(),
                                         lockerSizeRecord.getWidth(),
                                         lockerSizeRecord.getLength(),
                                         lockerSizeRecord.getHeight());

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
    return createModelLoader(this::findById);
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

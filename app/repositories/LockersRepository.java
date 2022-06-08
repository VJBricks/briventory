package repositories;

import database.BriventoryDB;
import models.Container;
import models.Locker;
import org.jooq.DSLContext;
import org.jooq.Record3;
import orm.ManyModelsLoader;
import orm.Mapper;
import orm.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static jooq.tables.ContainerComposition.CONTAINER_COMPOSITION;
import static jooq.tables.Locker.LOCKER;

@Singleton
public final class LockersRepository extends Repository<Locker> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /** the {@link Mapper} that will create an instance of {@link Locker} from an instance of {@link Record3}. */
  static final Mapper<Record3<Long, Long, Short>, Locker> LOCKER_MAPPER =
      r3 -> new Locker(r3.getValue(LOCKER.ID),
                       r3.getValue(LOCKER.ID_LOCKER_SIZE),
                       r3.getValue(CONTAINER_COMPOSITION.POSITION));

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link LockersRepository}, by injecting the necessary parameters.
   *
   * @param briventoryDB the {@link BriventoryDB} instance.
   */
  @Inject
  public LockersRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // Lazy Loaders Creation
  // *******************************************************************************************************************
  public ManyModelsLoader<Container, Locker> createLockersLoader(final Container container) {
    return createManyModelsLoader(container, (dslContext, c) -> getLockers(dslContext, c));
  }

  // *******************************************************************************************************************
  // General Data Retrieval
  // *******************************************************************************************************************
  public Locker findById(final Long id) {
    return fetchOne(LOCKER_MAPPER,
                    dslContext -> dslContext.select(LOCKER.ID,
                                                    LOCKER.ID_LOCKER_SIZE,
                                                    CONTAINER_COMPOSITION.POSITION)
                                            .from(LOCKER)
                                            .innerJoin(CONTAINER_COMPOSITION)
                                            .on(LOCKER.ID.eq(CONTAINER_COMPOSITION.ID_LOCKER))
                                            .where(LOCKER.ID.eq(id)));
  }

  private List<Locker> getLockers(final DSLContext dslContext, final Container container) {
    return fetch(LOCKER_MAPPER,
                 dslContext,
                 ctx -> ctx.select(LOCKER.ID,
                                   LOCKER.ID_LOCKER_SIZE,
                                   CONTAINER_COMPOSITION.POSITION)
                           .from(CONTAINER_COMPOSITION)
                           .innerJoin(LOCKER).on(CONTAINER_COMPOSITION.ID_LOCKER.eq(LOCKER.ID))
                           .where(CONTAINER_COMPOSITION.ID_CONTAINER.eq(container.getId())));
  }

}

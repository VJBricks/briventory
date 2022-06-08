package repositories;

import com.google.inject.Inject;
import database.BriventoryDB;
import models.Locker;
import models.PrivateContainer;
import org.jooq.DSLContext;
import org.jooq.Record4;
import org.jooq.ResultQuery;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import orm.Mapper;
import orm.Repository;

import javax.inject.Singleton;
import java.util.List;

import static jooq.Tables.*;
import static org.jooq.impl.DSL.*;
import static repositories.ContainersRepository.LOCKER_ALIAS;

@Singleton
public final class PrivateContainersRepository extends Repository<PrivateContainer> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link PrivateContainer} from an instance of {@link Record4}.
   */
  private static final Mapper<Record4<Long, Long, List<Locker>, Long>, PrivateContainer> PRIVATE_CONTAINER_MAPPER =
      r4 -> new PrivateContainer(r4.getValue(CONTAINER.ID),
                                 r4.getValue(CONTAINER.ID_CONTAINER_TYPE),
                                 r4.value3(),
                                 r4.getValue(PRIVATE_CONTAINER.ID_ACCOUNT));

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  @Inject
  public PrivateContainersRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  Tuple2<Mapper<Record4<Long, Long, List<Locker>, Long>,
                   PrivateContainer>,
            ResultQuery<Record4<Long, Long, List<Locker>, Long>>> getPrivateContainersQuery(
      final DSLContext dslContext) {
    return Tuple.tuple(PRIVATE_CONTAINER_MAPPER,
                       dslContext.select(CONTAINER.ID,
                                         CONTAINER.ID_CONTAINER_TYPE,
                                         inline((List<Locker>) null).as(LOCKER_ALIAS),
                                         PRIVATE_CONTAINER.ID_ACCOUNT)
                                 .from(CONTAINER)
                                 .innerJoin(PRIVATE_CONTAINER).on(CONTAINER.ID.eq(PRIVATE_CONTAINER.ID_CONTAINER)));
  }

  public List<PrivateContainer> getPrivateContainers() {
    return fetch(this::getPrivateContainersQuery);
  }

  Tuple2<Mapper<Record4<Long, Long, List<Locker>, Long>,
                   PrivateContainer>,
            ResultQuery<Record4<Long, Long, List<Locker>, Long>>> getPrivateContainersWithLockersQuery(
      final DSLContext dslContext) {
    return Tuple.tuple(PRIVATE_CONTAINER_MAPPER,
                       dslContext.select(CONTAINER.ID,
                                         CONTAINER.ID_CONTAINER_TYPE,
                                         multiset(
                                             select(LOCKER.ID,
                                                    LOCKER.ID_LOCKER_SIZE,
                                                    CONTAINER_COMPOSITION.POSITION)
                                                 .from(CONTAINER_COMPOSITION)
                                                 .innerJoin(LOCKER).on(CONTAINER_COMPOSITION.ID_LOCKER.eq(LOCKER.ID))
                                                 .where(CONTAINER_COMPOSITION.ID_CONTAINER.eq(CONTAINER.ID))
                                         ).as(LOCKER_ALIAS)
                                          .convertFrom(results -> results.map(LockersRepository.LOCKER_MAPPER::map)),
                                         PRIVATE_CONTAINER.ID_ACCOUNT)
                                 .from(CONTAINER)
                                 .innerJoin(PRIVATE_CONTAINER).on(CONTAINER.ID.eq(PRIVATE_CONTAINER.ID_CONTAINER)));
  }

  public List<PrivateContainer> getPrivateContainersWithLockers() {
    return fetch(this::getPrivateContainersWithLockersQuery);
  }

}

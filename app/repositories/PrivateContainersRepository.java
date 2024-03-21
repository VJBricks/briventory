package repositories;

import com.google.inject.Inject;
import database.BriventoryDB;
import models.Account;
import models.Locker;
import models.PrivateContainer;
import models.SharedContainer;
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

  public void persist(final PrivateContainer privateContainer) {
    super.persist(privateContainer);
  }

  // *******************************************************************************************************************
  // Migration
  // *******************************************************************************************************************
  public PrivateContainer migrate(final SharedContainer sharedContainer, final Account account) {
    return migrate(sharedContainer::createRecord2,
                   new PrivateContainer(sharedContainer, account));
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

  Tuple2<Mapper<Record4<Long, Long, List<Locker>, Long>,
      PrivateContainer>,
      ResultQuery<Record4<Long, Long, List<Locker>, Long>>> getPrivateContainersQuery(
      final DSLContext dslContext,
      final Account account,
      final boolean alsoPrivateContainer,
      final Long idContainerType,
      final Long idLockerSize) {
    return Tuple.tuple(PRIVATE_CONTAINER_MAPPER,
                       dslContext.select(CONTAINER.ID,
                                         CONTAINER.ID_CONTAINER_TYPE,
                                         inline((List<Locker>) null).as(LOCKER_ALIAS),
                                         PRIVATE_CONTAINER.ID_ACCOUNT)
                                 .from(CONTAINER)
                                 .innerJoin(PRIVATE_CONTAINER).on(CONTAINER.ID.eq(PRIVATE_CONTAINER.ID_CONTAINER))
                                 .innerJoin(CONTAINER_COMPOSITION)
                                 .on(CONTAINER.ID.eq(CONTAINER_COMPOSITION.ID_CONTAINER))
                                 .innerJoin(LOCKER).on(CONTAINER_COMPOSITION.ID_LOCKER.eq(LOCKER.ID))
                                 .where(PRIVATE_CONTAINER.ID_ACCOUNT.eq(account.getId()))
                                 .and(condition(inline(alsoPrivateContainer)))
                                 .and(condition(coalesce(
                                     field(CONTAINER.ID_CONTAINER_TYPE.eq(idContainerType)),
                                     inline(true))))
                                 .and(condition(coalesce(
                                     field(LOCKER.ID_LOCKER_SIZE.eq(idLockerSize)),
                                     inline(true)))));
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

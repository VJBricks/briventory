package repositories;

import com.google.inject.Inject;
import database.BriventoryDB;
import models.Locker;
import models.PrivateContainer;
import models.SharedContainer;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.ResultQuery;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import orm.Mapper;
import orm.Repository;

import javax.inject.Singleton;
import java.util.List;

import static jooq.Tables.*;
import static models.SharedContainer.SHARED_CONTAINER_MAPPER;
import static org.jooq.impl.DSL.*;
import static repositories.ContainersRepository.LOCKER_ALIAS;

@Singleton
public final class SharedContainersRepository extends Repository<SharedContainer> {

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************
  @Inject
  public SharedContainersRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // Queries
  // *******************************************************************************************************************
  Tuple2<Mapper<Record3<Long, Long, List<Locker>>, SharedContainer>,
      ResultQuery<Record3<Long, Long, List<Locker>>>> getSharedContainersQuery(
      final DSLContext dslContext) {
    return Tuple.tuple(SHARED_CONTAINER_MAPPER,
                       dslContext.select(CONTAINER.ID,
                                         CONTAINER.ID_CONTAINER_TYPE,
                                         inline((List<Locker>) null).as(LOCKER_ALIAS))
                                 .from(CONTAINER)
                                 .innerJoin(SHARED_CONTAINER).on(CONTAINER.ID.eq(SHARED_CONTAINER.ID_CONTAINER)));
  }

  Tuple2<Mapper<Record3<Long, Long, List<Locker>>, SharedContainer>,
      ResultQuery<Record3<Long, Long, List<Locker>>>> getSharedContainersQuery(
      final DSLContext dslContext,
      final boolean alsoSharedContainers,
      final Long idContainerType,
      final Long idLockerSize) {
    return Tuple.tuple(SHARED_CONTAINER_MAPPER,
                       dslContext.select(CONTAINER.ID,
                                         CONTAINER.ID_CONTAINER_TYPE,
                                         inline((List<Locker>) null).as(LOCKER_ALIAS))
                                 .from(CONTAINER)
                                 .innerJoin(SHARED_CONTAINER).on(CONTAINER.ID.eq(SHARED_CONTAINER.ID_CONTAINER))
                                 .innerJoin(CONTAINER_COMPOSITION)
                                 .on(CONTAINER.ID.eq(CONTAINER_COMPOSITION.ID_CONTAINER))
                                 .innerJoin(LOCKER).on(CONTAINER_COMPOSITION.ID_LOCKER.eq(LOCKER.ID))
                                 .where(condition(inline(alsoSharedContainers)))
                                 .and(condition(coalesce(
                                     field(CONTAINER.ID_CONTAINER_TYPE.eq(idContainerType)),
                                     true)))
                                 .and(condition(coalesce(
                                     field(LOCKER.ID_LOCKER_SIZE.eq(idLockerSize)),
                                     true))));
  }

  public List<SharedContainer> getSharedContainers() {
    return fetch(this::getSharedContainersQuery);
  }

  Tuple2<Mapper<Record3<Long, Long, List<Locker>>,
      SharedContainer>,
      ResultQuery<Record3<Long, Long, List<Locker>>>> getSharedContainersWithLockersQuery(
      final DSLContext dslContext) {
    return Tuple.tuple(SHARED_CONTAINER_MAPPER,
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
                                          .convertFrom(results -> results.map(LockersRepository.LOCKER_MAPPER::map)))
                                 .from(CONTAINER)
                                 .innerJoin(SHARED_CONTAINER).on(CONTAINER.ID.eq(SHARED_CONTAINER.ID_CONTAINER)));
  }

  public List<SharedContainer> getSharedContainersWithLockers() {
    return fetch(this::getSharedContainersWithLockersQuery);
  }

  public void persist(final SharedContainer sharedContainer) {
    super.persist(sharedContainer);
  }

  // *******************************************************************************************************************
  // Migration
  // *******************************************************************************************************************
  public SharedContainer migrate(final PrivateContainer privateContainer) {
    return migrate(privateContainer::createRecord2,
                   new SharedContainer(privateContainer));
  }

}

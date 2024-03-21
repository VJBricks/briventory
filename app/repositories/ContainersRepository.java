package repositories;

import database.BriventoryDB;
import models.Account;
import models.Container;
import org.jooq.Name;
import org.jooq.impl.DSL;
import orm.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public final class ContainersRepository extends Repository<Container> {

  // *******************************************************************************************************************
  // Constants
  // *******************************************************************************************************************
  /**
   * The alias used in the {@link org.jooq.impl.DSL#multiset} sub-queries, when they concern lockers.
   */
  static final Name LOCKER_ALIAS = DSL.unquotedName("locker");

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  /**
   * The injected instance of {@link PrivateContainersRepository}.
   */
  private final PrivateContainersRepository privateContainersRepository;
  /**
   * The injected instance of {@link SharedContainersRepository}.
   */
  private final SharedContainersRepository sharedContainersRepository;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link ContainersRepository}.
   *
   * @param briventoryDB                the {@link BriventoryDB}.
   * @param privateContainersRepository the {@link PrivateContainersRepository} instance.
   * @param sharedContainersRepository  the {@link SharedContainersRepository} instance.
   */
  @Inject
  public ContainersRepository(final BriventoryDB briventoryDB,
                              final PrivateContainersRepository privateContainersRepository,
                              final SharedContainersRepository sharedContainersRepository) {
    super(briventoryDB);
    this.privateContainersRepository = privateContainersRepository;
    this.sharedContainersRepository = sharedContainersRepository;
  }

  // *******************************************************************************************************************
  // Deletion
  // *******************************************************************************************************************
  public void delete(final Container container) {
    super.deleteInTransaction(container);
  }

  // *******************************************************************************************************************
  // Containers retrieval
  // *******************************************************************************************************************
  // dans ContainersRepository
  public List<Container> getAll() {
    return unionAll(privateContainersRepository::getPrivateContainersQuery,
        sharedContainersRepository::getSharedContainersQuery);
  }

  public List<Container> getContainersWithLockers() {
    return unionAll(privateContainersRepository::getPrivateContainersWithLockersQuery,
        sharedContainersRepository::getSharedContainersWithLockersQuery);
  }

  public List<Container> findAll(final Account account,
                                 final boolean alsoSharedContainers,
                                 final boolean alsoPrivateContainers,
                                 final Long idContainerType,
                                 final Long idLockerSize) {
    return unionAll(dslContext -> privateContainersRepository.getPrivateContainersQuery(dslContext,
            account,
            alsoPrivateContainers,
            idContainerType,
            idLockerSize),
        dslContext -> sharedContainersRepository.getSharedContainersQuery(dslContext,
            alsoSharedContainers,
            idContainerType,
            idLockerSize));
  }

}

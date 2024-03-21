package models;

import jooq.tables.records.ContainerRecord;
import jooq.tables.records.SharedContainerRecord;
import org.jooq.DSLContext;
import org.jooq.Record3;
import orm.Mapper;
import orm.models.PersistableModel2;

import java.util.List;

import static jooq.Tables.SHARED_CONTAINER;

public final class SharedContainer
    extends Container
    implements PersistableModel2<SharedContainer, ContainerRecord, SharedContainerRecord> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link SharedContainer} from an instance of {@link Record3}.
   */
  public static final Mapper<Record3<Long, Long, List<Locker>>, SharedContainer> SHARED_CONTAINER_MAPPER =
      r3 -> new SharedContainer(r3.value1(), r3.value2(), r3.value3());

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  private SharedContainer(final long id, final long idContainerType,
                          final List<Locker> lockers) {
    super(id, idContainerType, lockers);
  }

  SharedContainer(final long id, final long idContainerType) {
    this(id, idContainerType, null);
  }

  public SharedContainer(final ContainerType containerType) {
    super(containerType);
  }

  public SharedContainer(final PrivateContainer privateContainer) {
    super(privateContainer.getId(), privateContainer.getContainerType());
  }

  // *******************************************************************************************************************
  // PersistableModel2 Overrides
  // *******************************************************************************************************************

  @Override
  public SharedContainerRecord createRecord2(final DSLContext dslContext) {
    final SharedContainerRecord sharedContainerRecord = dslContext.newRecord(SHARED_CONTAINER);
    return sharedContainerRecord.setIdContainer(getId());
  }

  @Override
  public void refresh2(final SharedContainerRecord sharedContainerRecord) { /* Nothing to do */ }

}

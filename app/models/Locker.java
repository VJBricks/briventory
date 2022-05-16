package models;

import jooq.tables.records.LockerRecord;
import org.jooq.DSLContext;
import orm.LazyLoader;
import orm.Model;
import orm.RepositoriesHandler;
import orm.models.PersistableModel1;
import repositories.LockerSizesRepository;

import static jooq.Tables.LOCKER;

public final class Locker extends Model implements PersistableModel1<LockerRecord> {

  private Long id;

  private final LazyLoader<Long, LockerSize> lockerSizeLazyLoader =
      new LazyLoader<>(key -> RepositoriesHandler.of(LockerSizesRepository.class)
                                                 .getById(key));

  private Short position;

  public Locker() { /* No-Op */ }

  public Locker(final long id, final long idLockerSize, final short position) {
    this.id = id;
    lockerSizeLazyLoader.setKey(idLockerSize);
    this.position = position;
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  @Override
  public LockerRecord getUpdatableRecord(final DSLContext dslContext) {
    final LockerRecord lockerRecord = dslContext.newRecord(LOCKER);
    return lockerRecord.setId(id)
                       .setIdLockerSize(lockerSizeLazyLoader.getKey());
  }

  public void lastRefresh(final LockerRecord lockerRecord) { id = lockerRecord.getId(); }

  void setId(final Long id) { this.id = id; }

  public void setIdLockerSize(final Long idLockerSize) { lockerSizeLazyLoader.setKey(idLockerSize); }

  public void setPosition(final Short position) { this.position = position; }

}

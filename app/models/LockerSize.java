package models;

import jooq.tables.records.LockerSizeRecord;
import org.jooq.DSLContext;
import orm.Model;
import orm.models.PersistableModel1;

import static jooq.Tables.LOCKER_SIZE;

public final class LockerSize extends Model implements PersistableModel1<LockerSizeRecord> {
  private Long id;
  private String name;
  private Double width;
  private Double length;
  private Double height;

  public LockerSize(final Long id, final String name, final Double width, final Double length, final Double height) {
    this.id = id;
    this.name = name;
    this.width = width;
    this.length = length;
    this.height = height;
  }

  // *******************************************************************************************************************
  // PersistableModel1 Overrides
  // *******************************************************************************************************************

  @Override
  public LockerSizeRecord createRecord1(final DSLContext dslContext) {
    final LockerSizeRecord lockerSizeRecord = dslContext.newRecord(LOCKER_SIZE);
    return lockerSizeRecord.setId(id)
                           .setName(name)
                           .setWidth(width)
                           .setLength(length)
                           .setHeight(height);
  }

  public void refresh1(final LockerSizeRecord lockerSizeRecord) { id = lockerSizeRecord.getId(); }

  public Long getId() { return id; }

  void setId(final Long id) { this.id = id; }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Double getWidth() {
    return width;
  }

  public void setWidth(final Double width) {
    this.width = width;
  }

  public Double getLength() {
    return length;
  }

  public void setLength(final Double length) {
    this.length = length;
  }

  public Double getHeight() {
    return height;
  }

  public void setHeight(final Double height) {
    this.height = height;
  }

}

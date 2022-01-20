package repositories;

import database.BriventoryDB;
import jooq.tables.records.ColorSourceRecord;
import models.ColorsSource;
import org.jooq.RecordMapper;

import javax.inject.Inject;

import static jooq.tables.ColorSource.COLOR_SOURCE;

public final class ColorSourcesRepository extends Repository implements RecordMapper<ColorSourceRecord, ColorsSource> {

  /**
   * Creates a new {@link ColorSourcesRepository} using injection.
   *
   * @param briventoryDB the {@link BriventoryDB} instance.
   */
  @Inject
  private ColorSourcesRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  public ColorsSource findById(final long id) {
    return query(dslContext -> dslContext.selectFrom(COLOR_SOURCE)
                                         .where(COLOR_SOURCE.ID.eq(id))
                                         .fetchOne(this));
  }

  /**
   * Map a record into a POJO.
   *
   * @param record The record to be mapped. This is never null.
   *
   * @return The mapped value, which may be <code>null</code> in some implementations.
   */
  @Override
  public ColorsSource map(final ColorSourceRecord record) {
    return new ColorsSource(this).setId(record.getId())
                                 .setName(record.getName())
                                 .setUrl(record.getUrl());
  }

}

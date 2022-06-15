package repositories;

import database.BriventoryDB;
import jooq.tables.records.ColorSourceRecord;
import models.ColorsSource;
import org.jooq.DSLContext;
import orm.Mapper;
import orm.OptionalModelLoader;
import orm.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Optional;

import static jooq.tables.ColorSource.COLOR_SOURCE;

@Singleton
public final class ColorsSourcesRepository extends Repository<ColorsSource> {
  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link ColorsSource} from an instance of
   * {@link ColorSourceRecord}.
   */
  private static final Mapper<ColorSourceRecord, ColorsSource> COLORS_SOURCE_MAPPER =
      colorSourceRecord -> new ColorsSource(colorSourceRecord.getId(),
                                            colorSourceRecord.getName(),
                                            colorSourceRecord.getUrl());

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link ColorsSourcesRepository} using injection.
   *
   * @param briventoryDB the {@link BriventoryDB} instance.
   */
  @Inject
  private ColorsSourcesRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // Lazy Loaders Creation
  // *******************************************************************************************************************

  /** @return the {@link OptionalModelLoader} that will handle the lazy loading of a {@link ColorsSource}. */
  public OptionalModelLoader<Long, ColorsSource> createOptionalModelLoader() {
    return createOptionalModelLoader(this::findById,
                                     (dslContext, idAccount, optionalColorsSource) -> Collections.emptyList());
  }

  // *******************************************************************************************************************
  // General Data Retrieval
  // *******************************************************************************************************************

  /**
   * Find the {@link ColorsSource} corresponding to the identifier given.
   *
   * @param dslContext the {@link DSLContext}.
   * @param id the identifier of the {@link ColorsSource} to find.
   *
   * @return an {@link Optional} containing the instance of {@link ColorsSource}, or {@code null} if the identifier does
   * not correspond to any color source.
   */
  private Optional<ColorsSource> findById(final DSLContext dslContext, final long id) {
    return Optional.ofNullable(fetchOne(COLORS_SOURCE_MAPPER,
                                        dslContext,
                                        ctx -> ctx.selectFrom(COLOR_SOURCE)
                                                  .where(COLOR_SOURCE.ID.eq(id))));
  }

}

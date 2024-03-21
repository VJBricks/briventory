package repositories;

import database.BriventoryDB;
import jooq.tables.records.ConfigurationRecord;
import models.Configuration;
import org.semver.Version;
import orm.Mapper;
import orm.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import static jooq.Tables.CONFIGURATION;

/**
 * The {@code ConfigurationRepository} handle all the database actions on the table {@link jooq.tables.Configuration}.
 * This table is a read-only one.
 */
@Singleton
public final class ConfigurationRepository extends Repository<Configuration> {

  // *******************************************************************************************************************
  // Instance factory
  // *******************************************************************************************************************
  /**
   * the {@link Mapper} that will create an instance of {@link Configuration} from an instance of
   * {@link ConfigurationRecord}.
   */
  private static final Mapper<ConfigurationRecord, Configuration> CONFIGURATION_MAPPER =
      configurationRecord -> new Configuration(configurationRecord.getDatabaseRevision());

  // *******************************************************************************************************************
  // Constants
  // *******************************************************************************************************************
  /** The app version, taken from the {@code build.sbt} file. */
  private static final Version APP_VERSION = Version.parse(utils.BriventoryBuildInfo.version());

  // *******************************************************************************************************************
  // Attributes
  // *******************************************************************************************************************
  private final Configuration configuration;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link ConfigurationRepository} using injection.
   *
   * @param briventoryDB the {@link BriventoryDB} instance.
   */
  @Inject
  public ConfigurationRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
    configuration = fetchSingle(CONFIGURATION_MAPPER,
                                dslContext -> dslContext.selectFrom(CONFIGURATION));
  }

  // *******************************************************************************************************************
  // Methods related to the database version
  // *******************************************************************************************************************

  /**
   * @return {@code true} if the database is initialized and the version correspond to the App major version, otherwise
   * {@code false}.
   */
  public boolean isDatabaseInitialized() {
    if (configuration.getDatabaseRevision() == null) return false;
    return Version.parse(configuration.getDatabaseRevision()).isCompatible(APP_VERSION);
  }

}

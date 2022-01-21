package repositories;

import database.BriventoryDB;
import org.semver.Version;
import orm.repositories.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import static jooq.Tables.CONFIGURATION;

/**
 * The {@code ConfigurationRepository} handle all the database actions on the table {@link jooq.tables.Configuration}.
 * This table is a read-only one.
 */
@Singleton
public class ConfigurationRepository extends Repository {

  // *******************************************************************************************************************
  // Constants
  // *******************************************************************************************************************
  /** The app version, taken from the {@code build.sbt} file. */
  private static final Version APP_VERSION = Version.parse(utils.BriventoryBuildInfo.version());

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
  }

  // *******************************************************************************************************************
  // Methods related to the database version
  // *******************************************************************************************************************

  /**
   * @return {@code true} if the database is initialized and the version correspond to the App major version, otherwise
   * {@code false}.
   */
  public boolean isDatabaseInitialized() {
    try {
      final var optionalRevision = query(
          dslContext -> dslContext.select(CONFIGURATION.DATABASE_REVISION)
                                  .from(CONFIGURATION)
                                  .fetchOptionalInto(String.class));
      return optionalRevision.filter(revision -> Version.parse(revision).isCompatible(APP_VERSION)).isPresent();
    } catch (Exception e) {
      return false;
    }
  }

}

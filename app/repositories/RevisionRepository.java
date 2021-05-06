package repositories;

import database.BriventoryDB;
import models.Revision;
import org.hibernate.CacheMode;
import org.semver.Version;

import javax.inject.Inject;
import javax.inject.Singleton;

import static database.BriventoryDB.CACHE_REGION;

@Singleton
public class RevisionRepository extends MutableRepository<Revision> {

  // *******************************************************************************************************************
  // Constants
  // *******************************************************************************************************************
  /** The app version. */
  private static final Version APP_VERSION = Version.parse(ch.varani.briventory.BriventoryBuildInfo.version());

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  @Inject
  public RevisionRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  /**
   * @return {@code true} if the database is initialized and the version correspond to the App major version, otherwise
   * {@code false}.
   */
  public boolean isDatabaseInitialized() {
    try {
      return getBriventoryDB().query(session -> {
        final var revision = session.createQuery("select r from Revision r", Revision.class)
                                    .setCacheable(true)
                                    .setCacheRegion(CACHE_REGION)
                                    .setCacheMode(CacheMode.NORMAL)
                                    .getSingleResult();
        final var dbVersion = new Version(revision.getDatabase(), 0, 0);
        return dbVersion.isCompatible(APP_VERSION);
      }).join();
    } catch (Exception e) {
      return false;
    }
  }

}

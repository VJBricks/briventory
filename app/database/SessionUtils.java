package database;

import org.hibernate.Session;

/** Utility class to provides various helpers while using an Hibernate {@link Session}. */
public final class SessionUtils {

  /** The Hibernate {@link Session}. */
  private final Session session;

  /**
   * Creates a new {@link SessionUtils} helper.
   *
   * @param session the Hibernate {@link Session}.
   */
  SessionUtils(final Session session) {
    this.session = session;
  }

}

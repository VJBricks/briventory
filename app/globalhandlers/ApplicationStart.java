package globalhandlers;

import play.db.DBApi;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@code ApplicationStart} is a singleton that is launched every time the App is started. It performs the following
 * operations:
 * <ul>
 *   <li>migrate the database if a new version is available.</li>
 * </ul>
 */
@Singleton
public final class ApplicationStart {

  /**
   * Creates a new instance of {@link ApplicationStart} using the injected parameters.
   *
   * @param lifecycle the {@link ApplicationLifecycle} instance.
   * @param dbApi the {@link DBApi} instance.
   */
  @Inject
  public ApplicationStart(final ApplicationLifecycle lifecycle, final DBApi dbApi) {
    lifecycle.addStopHook(() -> {
      dbApi.shutdown();
      return null;
    });
  }

}

package globalhandlers;

import play.Environment;
import play.db.jpa.JPAApi;
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
   * @param environment the {@link Environment} instance.
   * @param jpaApi the {@link JPAApi} instance.
   */
  @Inject
  public ApplicationStart(final ApplicationLifecycle lifecycle, final Environment environment, final JPAApi jpaApi) {

  }

}
